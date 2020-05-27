package com.sevnis.githubstats.service;

import com.sevnis.githubstats.controller.api.UserStats;
import com.sevnis.githubstats.repository.github.GithubRepository;
import com.sevnis.githubstats.repository.github.api.PullRequest;
import com.sevnis.githubstats.repository.github.api.PullRequestComment;
import com.sevnis.githubstats.repository.github.api.Repo;
import com.sevnis.githubstats.repository.redis.UserDataRepository;
import com.sevnis.githubstats.repository.redis.entity.UserData;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PullRequestService {

  private final GithubRepository githubRepository;
  private final UserDataRepository userDataRepository;

  public List<UserStats> getPullRequestComments(String owner) {

    List<UserStats> userStats = checkDatabase(15, ChronoUnit.MINUTES);
    if (userStats != null) {
      return userStats;
    } else {
      List<Repo> repos = githubRepository.getRepos(owner).block();

      // get all pull requests of the entire repo
      List<PullRequest> pullRequests = Mono.zipDelayError(repos.stream()
              .map(r -> githubRepository.getPullRequests(owner, r.getName()))
              .collect(Collectors.toList()),
          (results -> Stream.of(results)
              .flatMap(result -> ((List<PullRequest>) result).stream())
              .collect(Collectors.toList())))
          .block();

      // count user stats for all pull requests
      Map<Long, UserData> userDataMap = Mono.zipDelayError(pullRequests.stream()
          .map(pr -> githubRepository.getPullRequestComments(pr.getUrl()))
          .collect(Collectors.toList()), this::pullRequestCommentsCombinator)
          .block();

      pullRequests.stream().forEach(pr -> {
        UserData userData = userDataMap.get(pr.getUser().getId());
        userData.setPullRequestCounts(userData.getPullRequestCounts() + 1);
      });

      userDataMap.values().stream().forEach(userData -> userData.setLastUpdatedDtm(ZonedDateTime.now()));
      userDataRepository.saveAll(userDataMap.values());

      return convertToUserStatsList(userDataMap.values());
    }
  }

  private List<UserStats> checkDatabase(long time, TemporalUnit temporalUnit) {

    ZonedDateTime dateTime = ZonedDateTime.now().minus(time, temporalUnit);
    List<UserData> userDataList = StreamSupport.stream(userDataRepository.findAll().spliterator(), false)
        .collect(Collectors.toList());
    return userDataList.stream()
        .anyMatch(userData -> userData.getLastUpdatedDtm().isBefore(dateTime)) ? null
        : convertToUserStatsList(userDataList);
  }

  private List<UserStats> convertToUserStatsList(Collection<UserData> userDataList) {

    return userDataList.stream()
        .map(userData -> UserStats.builder()
            .name(userData.getName())
            .pullRequestCounts(userData.getPullRequestCounts())
            .pullRequestCommentCounts(userData.getPullRequestCommentCounts())
            .build())
        .collect(Collectors.toList());
  }

  private Map<Long, UserData> pullRequestCommentsCombinator(Object[] results) {

    Map<Long, UserData> userMap = new HashMap<>();
    for (int i = 0; i < results.length; i++) {
      List<PullRequestComment> pullRequestComments = (List<PullRequestComment>) results[i];

      pullRequestComments.forEach(pullRequestComment -> {
        UserData userData = userMap.get(pullRequestComment.getUser().getId());
        if (userData == null) {
          userData = UserData.builder()
              .id(pullRequestComment.getUser().getId())
              .name(pullRequestComment.getUser().getLogin())
              .pullRequestCounts(new Long(0))
              .pullRequestCommentCounts(new Long(0))
              .build();
          userMap.put(pullRequestComment.getUser().getId(), userData);
        }
        userData.setPullRequestCommentCounts(userData.getPullRequestCommentCounts() + 1);
      });
    }
    return userMap;
  }
}
