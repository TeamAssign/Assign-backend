package com.team3.assign_back.domain.food.repository;


import com.team3.assign_back.domain.food.entity.Food;
import com.team3.assign_back.domain.recommendation.dto.RecommendationResponseDto;
import com.team3.assign_back.global.enums.FoodEnum;
import com.team3.assign_back.global.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.team3.assign_back.global.enums.FoodEnum.FoodType.COMPANYDINNER;
import static com.team3.assign_back.global.exception.ErrorCode.INVALID_FOOD_NAME;

@Repository
@RequiredArgsConstructor
public class FoodRedisRepository {

    private final StringRedisTemplate redisTemplate;


    public void deleteRejectedFood(String keyPrefix) {
        String FoodIdsKey = keyPrefix + ":foodIds";
        String AccuraciesKey = keyPrefix + ":accuracies";
        redisTemplate.delete(FoodIdsKey);
        redisTemplate.delete(AccuraciesKey);
    }

    public void saveRejectedFood(String keyPrefix, Long foodId, BigDecimal accuracy) {


        String FoodIdsKey = keyPrefix + ":foodIds";
        String AccuraciesKey = keyPrefix + ":accuracies";
        redisTemplate.opsForList().rightPush(FoodIdsKey, foodId.toString());
        redisTemplate.opsForList().rightPush(AccuraciesKey, accuracy.toString());

    }

    public List<String> getRejectedFoodIds(String keyPrefix) {

        String FoodIdsKey = keyPrefix + ":foodIds";

        return redisTemplate.opsForList().range(FoodIdsKey, 0, -1);
    }

    public String getAccuracy(String keyPrefix, int index) {

        String AccuraciesKey = keyPrefix + ":accuracies";

        return redisTemplate.opsForList().index(AccuraciesKey, index);
    }

}
