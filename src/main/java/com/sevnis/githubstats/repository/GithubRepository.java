package com.sevnis.githubstats.repository;

import com.sevnis.githubstats.repository.api.PullRequest;
import java.net.URI;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class GithubRepository {

  private final WebClient client = WebClient.create("http://api.github.com");

  public Mono<List<PullRequest>> getPullRequests() {

    return client.get()
        .uri(URI.create("https://api.github.com/repos/overtakerx/webflux-parallelism/pulls"))
        .header("Accept", "application/vnd.github.v3+json")
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<PullRequest>>() {
        });
  }
}
