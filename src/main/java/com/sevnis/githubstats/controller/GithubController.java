package com.sevnis.githubstats.controller;


import com.sevnis.githubstats.controller.api.UserStats;
import com.sevnis.githubstats.service.PullRequestService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GithubController {

  private final PullRequestService pullRequestService;

  @GetMapping("/{owner}/pullrequeststats")
  public List<UserStats> getPullRequestCommentss(@PathVariable final String owner) {
    return pullRequestService.getPullRequestComments(owner);
  }
}
