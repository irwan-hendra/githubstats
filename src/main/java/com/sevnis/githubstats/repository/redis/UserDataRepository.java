package com.sevnis.githubstats.repository.redis;

import com.sevnis.githubstats.repository.redis.entity.UserData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepository extends CrudRepository<UserData, String> {

}
