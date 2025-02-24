package com.team3.assign_back.domain.statistics.repository;

import com.team3.assign_back.domain.statistics.entity.UserSummaryMonthly;
import com.team3.assign_back.global.annotation.CustomMongoRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

@CustomMongoRepository
public interface UserSummaryRepository extends MongoRepository<UserSummaryMonthly,String>{

    Optional<UserSummaryMonthly> findFirstByUserIdOrderByYearDescMonthDescDayDesc(long userId);


    List<UserSummaryMonthly> findByYearAndMonthAndDayBetween(int year, int monthValue, int dayOfMonth, int year1, int monthValue1, int dayOfMonth1);
}
