package com.team3.assign_back.domain.stats.repository;

import com.team3.assign_back.domain.stats.entity.CompanySummaryMonthly;
import com.team3.assign_back.domain.stats.entity.TeamSummaryMonthly;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamSummaryRepository extends MongoRepository<TeamSummaryMonthly,String> {
    TeamSummaryMonthly findByYearAndMonth(int year, int month);
}
