package com.team3.assign_back.domain.statistics.repository;

import com.team3.assign_back.domain.statistics.entity.TeamSummaryMonthly;
import com.team3.assign_back.global.annotation.CustomMongoRepository;
import org.springframework.data.mongodb.repository.MongoRepository;


@CustomMongoRepository
public interface TeamSummaryRepository extends MongoRepository<TeamSummaryMonthly,String>{

    TeamSummaryMonthly findFirstByTeamIdOrderByYearDescMonthDescDayDesc(long teamId);
}
