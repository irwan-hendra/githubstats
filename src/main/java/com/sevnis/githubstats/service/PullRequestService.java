package com.sevnis.githubstats.service;

import com.sevnis.githubstats.repository.GithubRepository;
import com.sevnis.githubstats.repository.api.PullRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PullRequestService {

  private final GithubRepository githubRepository;

  public Integer getTotalNumberOfPullRequests() {

    List<PullRequest> pullRequests = githubRepository.getPullRequests().block();
    return pullRequests.size();
  }
}
