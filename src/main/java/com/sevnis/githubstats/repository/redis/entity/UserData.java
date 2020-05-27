package com.sevnis.githubstats.repository.redis.entity;


import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("UserData")
public class UserData implements Serializable {

  @Id
  private Long id;
  private String name;
  private Long pullRequestCounts;
  private Long pullRequestCommentCounts;

  private ZonedDateTime lastUpdatedDtm;
}
