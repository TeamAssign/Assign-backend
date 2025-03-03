package com.team3.assign_back.domain.tastePreference.repository;



import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.assign_back.domain.intermediate.entity.QTeamTastePreference;
import com.team3.assign_back.domain.intermediate.entity.QUserTastePreference;
import com.team3.assign_back.domain.tastePreference.dao.TastePreferenceEmbeddingDao;
import com.team3.assign_back.domain.tastePreference.entity.QTastePreference;
import com.team3.assign_back.domain.tastePreference.entity.QTastePreferenceEmbedding;
import com.team3.assign_back.domain.team.entity.QTeam;
import com.team3.assign_back.domain.users.entity.QUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomTastePreferenceEmbeddingRepositoryImpl implements CustomTastePreferenceEmbeddingRepository {

    private final JPAQueryFactory query;
    private static final QUsers users = QUsers.users;
    private static final QUserTastePreference userTastePreference = QUserTastePreference.userTastePreference;
    private static final QTeamTastePreference teamTastePreference = QTeamTastePreference.teamTastePreference;
    private static final QTastePreference tastePreference = QTastePreference.tastePreference;
    private static final QTastePreferenceEmbedding tastePreferenceEmbedding = QTastePreferenceEmbedding.tastePreferenceEmbedding;
    private static final QTeam team = QTeam.team;


    public List<TastePreferenceEmbeddingDao> findLikeEmbeddingAndRateByUserIds(List<Long> participants){

        return findEmbeddingAndRateByUserIdsSubquery(participants)
                .select(
                        Projections.constructor(TastePreferenceEmbeddingDao.class, tastePreferenceEmbedding.id, tastePreferenceEmbedding.likeEmbedding, tastePreferenceEmbedding.likeLearningRate))
                .fetch();
    }

    public List<TastePreferenceEmbeddingDao> findDislikeEmbeddingAndRateByUserIds(List<Long> participants){

        return findEmbeddingAndRateByUserIdsSubquery(participants)
                .select(
                        Projections.constructor(TastePreferenceEmbeddingDao.class, tastePreferenceEmbedding.id, tastePreferenceEmbedding.dislikeEmbedding, tastePreferenceEmbedding.dislikeLearningRate))
                .fetch();
    }

    private JPAQuery<?> findEmbeddingAndRateByUserIdsSubquery(List<Long> participants) {
        return query
                .from(users)
                .join(userTastePreference)
                .on(userTastePreference.users.eq(users))
                .join(tastePreference)
                .on(userTastePreference.tastePreference.eq(tastePreference))
                .join(tastePreferenceEmbedding)
                .on(tastePreferenceEmbedding.tastePreference.eq(tastePreference))
                .where(users.id.in(participants));
    }

    public List<TastePreferenceEmbeddingDao> findLikeEmbeddingAndRateForTeam(Long teamId){

        return findEmbeddingAndRateForTeamSubquery(teamId)
                .select(
                        Projections.constructor(TastePreferenceEmbeddingDao.class, tastePreferenceEmbedding.likeEmbedding, tastePreferenceEmbedding.likeLearningRate))
                .fetch();
    }
    public List<TastePreferenceEmbeddingDao> findDislikeEmbeddingAndRateForTeam(Long teamId){

        return findEmbeddingAndRateForTeamSubquery(teamId)
                .select(
                        Projections.constructor(TastePreferenceEmbeddingDao.class, tastePreferenceEmbedding.dislikeEmbedding, tastePreferenceEmbedding.dislikeLearningRate))
                .fetch();
    }


    private JPAQuery<?> findEmbeddingAndRateForTeamSubquery(Long teamId) {
        return query
                .from(team)
                .join(teamTastePreference)
                .on(teamTastePreference.team.eq(team))
                .join(tastePreference)
                .on(teamTastePreference.tastePreference.eq(tastePreference))
                .join(tastePreferenceEmbedding)
                .on(tastePreferenceEmbedding.tastePreference.eq(tastePreference))
                .where(team.id.eq(teamId));
    }

    public void saveLikeEmbeddingAndRate(List<TastePreferenceEmbeddingDao> tastePreferenceEmbeddingDaos){

        for(TastePreferenceEmbeddingDao dao : tastePreferenceEmbeddingDaos){
            query
                    .update(tastePreferenceEmbedding)
                    .set(tastePreferenceEmbedding.likeEmbedding, dao.getEmbedding())
                    .set(tastePreferenceEmbedding.likeLearningRate, dao.getLearningRate())
                    .where(tastePreferenceEmbedding.id.eq(dao.getId()))
                    .execute();
        }

    }

    public void saveDislikeEmbeddingAndRate(List<TastePreferenceEmbeddingDao> tastePreferenceEmbeddingDaos){

        for(TastePreferenceEmbeddingDao dao : tastePreferenceEmbeddingDaos){
            query
                    .update(tastePreferenceEmbedding)
                    .set(tastePreferenceEmbedding.dislikeEmbedding, dao.getEmbedding())
                    .set(tastePreferenceEmbedding.dislikeLearningRate, dao.getLearningRate())
                    .where(tastePreferenceEmbedding.id.eq(dao.getId()))
                    .execute();
        }

    }




}
