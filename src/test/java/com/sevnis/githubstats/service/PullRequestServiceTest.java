package com.sevnis.githubstats.service;

import com.sevnis.githubstats.repository.redis.UserDataRepository;
import com.sevnis.githubstats.repository.redis.entity.UserData;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PullRequestServiceTest {


  @Autowired
  private PullRequestService pullRequestService;

  @Autowired
  private UserDataRepository userDataRepository;


  @Test
  public void test2() {

    pullRequestService.getPullRequestComments("overtakerx");

    List<UserData> userData = StreamSupport.stream(userDataRepository.findAll().spliterator(), false)
        .collect(Collectors.toList());
    System.out.println(userData);

  }

}
