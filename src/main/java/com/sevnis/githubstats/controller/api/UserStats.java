package com.sevnis.githubstats.controller.api;

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
public class UserStats {

  private String name;
  private Long pullRequestCounts;
  private Long pullRequestCommentCounts;
}
