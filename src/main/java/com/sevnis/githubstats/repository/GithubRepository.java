package com.sevnis.githubstats.repository;

import com.sevnis.githubstats.repository.api.PullRequest;
import com.sevnis.githubstats.repository.api.PullRequestComment;
import com.sevnis.githubstats.repository.api.Repo;
import java.net.URI;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class GithubRepository {

  private final WebClient client = WebClient.create("https://api.github.com");


  public Mono<List<Repo>> getRepos(String username) {
    return client.get()
        .uri(uriBuilder -> uriBuilder.path("/users/{username}/repos")
            .build(username))
        .header("Accept", "application/vnd.github.v3+json")
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<Repo>>() {
        });
  }

  public Mono<List<PullRequest>> getPullRequests(String username, String repoName) {

    return client.get()
        .uri(uriBuilder -> uriBuilder.path("/repos/{username}/{repoName}/pulls")
            .build(username, repoName))
        .header("Accept", "application/vnd.github.v3+json")
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<PullRequest>>() {
        });
  }

  public Mono<List<PullRequestComment>> getPullRequestComments(String pullRequestUri) {

    return client.get()
        .uri(URI.create(pullRequestUri + "/comments"))
        .header("Accept", "application/vnd.github.v3+json")
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<PullRequestComment>>() {
        });
  }
}
