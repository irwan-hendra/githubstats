package com.sevnis.githubstats.repository.github.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Repo {

  private Long id;
  private String name;

  @JsonProperty("full_name")
  private String fullName;

  @JsonProperty("html_url")
  private String htmlUrl;

  private String url;

}
