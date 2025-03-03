package com.team3.assign_back.domain.food.repository;


import com.team3.assign_back.domain.recommendation.dto.RecommendationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class FoodRedisRepository {

    private final StringRedisTemplate redisTemplate;
    private final RedisTemplate<String, Object> redisTemplateObject;


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


    public RecommendationResponseDto getFoodToday(Long userId) {
        String key = "users:" + userId + ":today";
        return (RecommendationResponseDto) redisTemplateObject.opsForValue().get(key);
    }


    public void saveFoodToday(Long userId, RecommendationResponseDto recommendationResponseDto) {
        String key = "users:" + userId + ":today";
        redisTemplateObject.opsForValue().set(key, recommendationResponseDto, 20, TimeUnit.HOURS);

    }
}
