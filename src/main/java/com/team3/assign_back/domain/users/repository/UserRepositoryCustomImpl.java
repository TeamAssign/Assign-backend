package com.team3.assign_back.domain.users.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.assign_back.domain.intermediate.entity.QParticipant;
import com.team3.assign_back.domain.team.entity.QTeam;
import com.team3.assign_back.domain.users.dto.UserResponseDto;
import com.team3.assign_back.domain.users.entity.QUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private final QUsers users = QUsers.users;
    private final QTeam team = QTeam.team;
    private final QParticipant participant = QParticipant.participant;

    @Override
    public List<UserResponseDto> searchUsersByFrequency(Long userId, int page, int size) {

        return queryFactory
                .select(Projections.constructor(UserResponseDto.class,
                    users.id,
                        users.name,
                        team.name.as("teamName"),
                        users.profileImgUrl
                ))
                .from(users)
                .join(users.team, team)
                .leftJoin(users.participants, participant)
                .where(participant.users.id.eq(userId))
                .groupBy(users.id, team.name, users.profileImgUrl)
                .orderBy(
                        participant.id.count().desc().nullsLast(), // 1. 자주 같이 먹은 순 정렬
                        team.name.asc().nullsLast() // 2. 팀별 정렬
                )
                .offset((page - 1) * size)
                .limit(size)
                .fetch();
    }
}