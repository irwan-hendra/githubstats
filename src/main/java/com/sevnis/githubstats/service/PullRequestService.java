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

  public Integer getPullRequests() {

    List<PullRequest> pullRequests = githubRepository.getPullRequests().block();
    return pullRequests.size();
  }

  public Integer getPullRequestReviews() {

    List<PullRequest> pullRequests = githubRepository.getPullRequests().block();
    return pullRequests.size();
  }

  public Integer getPullRequestReviewComments() {

    List<PullRequest> pullRequests = githubRepository.getPullRequests().block();
    return pullRequests.size();
  }
}
