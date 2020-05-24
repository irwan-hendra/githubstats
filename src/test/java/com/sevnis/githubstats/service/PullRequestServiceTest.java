package com.sevnis.githubstats.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PullRequestServiceTest {


  @Autowired
  private PullRequestService pullRequestService;

  @Test
  public void test() {

    pullRequestService.getPullRequests();

  }
}
