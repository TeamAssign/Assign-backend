package com.team3.assign_back.domain.food.repository;


import com.team3.assign_back.domain.food.entity.Food;
import com.team3.assign_back.domain.recommendation.dto.RecommendationResponseDto;
import com.team3.assign_back.global.enums.FoodEnum;
import com.team3.assign_back.global.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Duration;
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
        String foodIdsKey = keyPrefix + ":foodIds";
        String accuraciesKey = keyPrefix + ":accuracies";

        redisTemplate.executePipelined((RedisCallback<Object>) status-> {
            StringRedisConnection connection = (StringRedisConnection) status;
            connection.del(foodIdsKey);
            connection.del(accuraciesKey);

            return null;
        } );
    }

    public void saveRejectedFood(String keyPrefix, Long foodId, BigDecimal accuracy) {


        String foodIdsKey = keyPrefix + ":foodIds";
        String accuraciesKey = keyPrefix + ":accuracies";

        redisTemplate.executePipelined((RedisCallback<Object>) status-> {
            StringRedisConnection connection = (StringRedisConnection) status;
            connection.rPush(foodIdsKey, foodId.toString());
            connection.rPush(accuraciesKey, accuracy.toString());
            connection.expire(foodIdsKey, Duration.ofMinutes(10).toSeconds());
            connection.expire(accuraciesKey, Duration.ofMinutes(10).toSeconds());

            return null;
        } );

    }

    public List<String> getRejectedFoodIds(String keyPrefix) {

        String foodIdsKey = keyPrefix + ":foodIds";

        return redisTemplate.opsForList().range(foodIdsKey, 0, -1);
    }

    public String getAccuracy(String keyPrefix, int index) {

        String accuraciesKey = keyPrefix + ":accuracies";

        return redisTemplate.opsForList().index(accuraciesKey, index);
    }

}
