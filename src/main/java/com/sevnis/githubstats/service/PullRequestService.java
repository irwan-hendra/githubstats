package com.sevnis.githubstats.service;

import com.sevnis.githubstats.controller.api.UserStats;
import com.sevnis.githubstats.repository.GithubRepository;
import com.sevnis.githubstats.repository.api.PullRequest;
import com.sevnis.githubstats.repository.api.PullRequestComment;
import com.sevnis.githubstats.repository.api.Repo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PullRequestService {

  private final GithubRepository githubRepository;

  public List<UserStats> getPullRequestStats(String username) {

    List<Repo> repos = githubRepository.getRepos(username).block();

    Map<Long, UserStats> userStatsMap = Mono.zipDelayError(repos.stream()
        .map(r -> githubRepository.getPullRequests(username, r.getName()))
        .collect(Collectors.toList()), this::pullRequestStatsCombinator)
        .block();
    return new ArrayList<>(userStatsMap.values());
  }

  ///TEMP: store in redis later
  private Map<Long, UserStats> pullRequestStatsCombinator(Object[] results) {

    Map<Long, UserStats> userMap = new HashMap<>();
    for (int i = 0; i < results.length; i++) {
      List<PullRequest> pullRequests = (List<PullRequest>) results[i];

      pullRequests.forEach(pullRequest -> {
        UserStats userStats = userMap.get(pullRequest.getUser().getId());
        if (userStats == null) {
          userStats = UserStats.builder()
              .name(pullRequest.getUser().getLogin())
              .pullRequestCounts(new Long(0))
              .pullRequestCommentCounts(new Long(0))
              .build();
          userMap.put(pullRequest.getUser().getId(), userStats);
        }
        userStats.setPullRequestCounts(userStats.getPullRequestCounts() + 1);
      });
    }
    return userMap;
  }

  public List<UserStats> getPullRequestReviews(String username) {
    List<Repo> repos = githubRepository.getRepos(username).block();

    // get all pull requests of the entire repo
    List<PullRequest> pullRequests = Mono.zipDelayError(repos.stream()
            .map(r -> githubRepository.getPullRequests(username, r.getName()))
            .collect(Collectors.toList()),
        (results -> Stream.of(results)
            .flatMap(result -> ((List<PullRequest>) result).stream())
            .collect(Collectors.toList())))
        .block();

    // count user stats for all pull requests
    Map<Long, UserStats> userStatsMap = Mono.zipDelayError(pullRequests.stream()
        .map(pr -> githubRepository.getPullRequestComments(pr.getUrl()))
        .collect(Collectors.toList()), this::pullRequestCommentsCombinator).block();

    return new ArrayList<>(userStatsMap.values());
  }

  private Map<Long, UserStats> pullRequestCommentsCombinator(Object[] results) {

    Map<Long, UserStats> userMap = new HashMap<>();
    for (int i = 0; i < results.length; i++) {
      List<PullRequestComment> pullRequestComments = (List<PullRequestComment>) results[i];

      pullRequestComments.forEach(pullRequestComment -> {
        UserStats userStats = userMap.get(pullRequestComment.getUser().getId());
        if (userStats == null) {
          userStats = UserStats.builder()
              .name(pullRequestComment.getUser().getLogin())
              .pullRequestCounts(new Long(0))
              .pullRequestCommentCounts(new Long(0))
              .build();
          userMap.put(pullRequestComment.getUser().getId(), userStats);
        }
        userStats.setPullRequestCommentCounts(userStats.getPullRequestCommentCounts() + 1);
      });
    }
    return userMap;
  }
}
