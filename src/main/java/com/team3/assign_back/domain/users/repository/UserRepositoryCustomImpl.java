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

    @Override
    public List<UserResponseDto> searchUsersByFrequency(Long userId, int page, int size) {
        QUsers users = QUsers.users;
        QTeam team = QTeam.team;
        QParticipant participant = QParticipant.participant;

        return queryFactory
                .select(Projections.constructor(UserResponseDto.class,
                    users.id,
                        users.name,
                        team.name.as("teamName"),
                        users.profileImgUrl
                ))
                .from(users)
                .leftJoin(users.team, team)
                .leftJoin(users.participants, participant)
                .where(participant.users.id.eq(userId))
                .groupBy(users.id, team.name, users.profileImgUrl)
                .orderBy(participant.id.count().desc()) // 자주 같이 먹은 순 정렬
                .offset((page - 1) * size)
                .limit(size)
                .fetch();
    }
}