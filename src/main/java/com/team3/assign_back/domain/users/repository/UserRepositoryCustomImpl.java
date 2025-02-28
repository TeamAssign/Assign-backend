package com.team3.assign_back.domain.users.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.assign_back.domain.intermediate.entity.QParticipant;
import com.team3.assign_back.domain.team.entity.QTeam;
import com.team3.assign_back.domain.users.dto.UserSearchResponseDto;
import com.team3.assign_back.domain.users.entity.QUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private final QUsers users = QUsers.users;
    private final QTeam team = QTeam.team;
    private final QParticipant participant = QParticipant.participant;

    @Override
    public Page<UserSearchResponseDto> searchUsersByFrequency(Long userId, String keyword, Pageable pageable) {
        NumberExpression<Long> frequencyCount = participant.id.count(); // 같이 먹은 횟수 계산

        Long userTeamId = queryFactory
                .select(users.team.id)
                .from(users)
                .where(users.id.eq(userId))
                .fetchOne();

        List<UserSearchResponseDto> results = queryFactory
                .select(Projections.constructor(UserSearchResponseDto.class,
                        users.id,
                        users.name,
                        team.name.as("teamName"),
                        users.profileImgUrl
                ))
                .from(users)
                .join(users.team, team)
                .leftJoin(users.participants, participant)
                .where(
                        users.id.ne(userId)
                                .and(users.name.containsIgnoreCase(keyword))
                )
                .groupBy(users.id, users.name, team.id, team.name, users.profileImgUrl)
                .orderBy(
                        frequencyCount.desc().nullsLast(), // 1. 자주 같이 먹은 횟수 기준 정렬
                        new CaseBuilder()
                                .when(users.team.id.eq(userTeamId)).then(1)
                                .otherwise(0)
                                .desc()
                                .nullsLast(), // 같은 팀 우선 정렬
                        users.name.asc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수 조회
        long total = Optional.ofNullable(queryFactory
                .select(users.countDistinct())
                .from(users)
                .where(users.name.containsIgnoreCase(keyword).and(users.id.ne(userId)))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(results, pageable, total);
    }
}