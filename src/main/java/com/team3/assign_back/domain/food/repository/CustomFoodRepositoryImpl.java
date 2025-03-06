package com.team3.assign_back.domain.food.repository;


import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQueryFactory;
import com.team3.assign_back.domain.food.dto.FoodNotHavingImageRequestDto;
import com.team3.assign_back.domain.food.dto.FoodNotHavingImageResponseDto;
import com.team3.assign_back.domain.food.entity.QFood;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomFoodRepositoryImpl implements CustomFoodRepository{

    private final JdbcTemplate jdbcTemplate;

    private final JPQLQueryFactory query;
    private final QFood food = QFood.food;

    @Override
    public List<FoodNotHavingImageRequestDto> customFindFoodsWithoutImage() {
        List<Tuple> tuples= query.from(food)
                .select(
                        food.id,
                        food.name,
                        food.category)
                .where(food.imgUrl.isNull())
                .fetch();

        return tuples.stream()
                .map(tuple ->
                        new FoodNotHavingImageRequestDto(tuple.get(food.id), tuple.get(food.name), tuple.get(food.category).getKoreanName())
                )
                .toList();

    }

    @Override
    public void customSaveFoodImages(List<FoodNotHavingImageResponseDto> foodNotHavingImageResponseDtos) {

        String sql = "UPDATE food SET img_url = ? WHERE id = ?";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, foodNotHavingImageResponseDtos.get(i).getImgUrl());
                ps.setLong(2, foodNotHavingImageResponseDtos.get(i).getFoodId());

            }

            @Override
            public int getBatchSize() {
                return foodNotHavingImageResponseDtos.size();
            }
        });

    }
}
