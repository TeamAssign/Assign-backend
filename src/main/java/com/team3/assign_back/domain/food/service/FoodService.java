package com.team3.assign_back.domain.food.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.assign_back.domain.food.dto.FoodAnalysisDto;
import com.team3.assign_back.domain.food.dto.FoodAnalysisRequestDto;
import com.team3.assign_back.domain.food.entity.Food;
import com.team3.assign_back.domain.food.entity.TasteMetrics;
import com.team3.assign_back.domain.food.repository.FoodRepository;
import com.team3.assign_back.domain.food.repository.TasteMetricsRepository;
import groovy.util.logging.Slf4j;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.team3.assign_back.domain.food.prompt.FoodPrompt.FOOD_ANALYSIS;
import static com.team3.assign_back.global.constant.FoodConstant.FOOD_LIST_BATCH_SIZE;

@lombok.extern.slf4j.Slf4j
@Service
@Slf4j
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    private final OpenAiChatModel chatModel;
    private final ObjectMapper objectMapper;

    private final JdbcTemplate jdbcTemplate;


    public void createFoods(FoodAnalysisRequestDto foodAnalysisRequestDto){

        String category = foodAnalysisRequestDto.getCategory().name();
        List<String> foodNames = foodAnalysisRequestDto.getFoodNames();
        for (int fromIndex = 0; fromIndex < foodNames.size(); fromIndex += FOOD_LIST_BATCH_SIZE) {
            int toIndex = Math.min(fromIndex + FOOD_LIST_BATCH_SIZE, foodNames.size());
            List<String> batch = foodNames.subList(fromIndex, toIndex);

            batchCreateFoods(category, batch);
        }


    }

    @Async
    @Transactional
    private void batchCreateFoods(String category, List<String> batch) {

        String sql = "INSERT INTO food (category, name, created_at, updated_at) VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, category);
                ps.setString(2, batch.get(i));
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            }

            @Override
            public int getBatchSize() {
                return batch.size();
            }
        });
    }

    public void createTasteMetrics(){



        List<Food> foodsWithoutTasteMetrics= foodRepository.customFindFoodsWithoutTasteMetrics();

        if(foodsWithoutTasteMetrics.isEmpty()){
            return;
        }

        List<CompletableFuture<FoodAnalysisDto>> futures = foodsWithoutTasteMetrics
                .stream()
                .map(
                        food-> CompletableFuture.supplyAsync( () -> {
                            try {
                                return objectMapper.readValue(ChatClient.create(chatModel).prompt().system(FOOD_ANALYSIS).user(food.getName()).call().content(), FoodAnalysisDto.class);
                            } catch (JsonProcessingException e) {
                                log.info("{},{}", food.getName(), e.getMessage());
                                return null;
                            }
                        })
                ).toList();


        for (int fromIndex = 0; fromIndex < foodsWithoutTasteMetrics.size(); fromIndex += FOOD_LIST_BATCH_SIZE) {
            int toIndex = Math.min(fromIndex + FOOD_LIST_BATCH_SIZE, foodsWithoutTasteMetrics.size());

            List<CompletableFuture<FoodAnalysisDto>> batch = futures.subList(fromIndex, toIndex);

            List<Food> batchFoods = foodsWithoutTasteMetrics.subList(fromIndex, toIndex);
            List<FoodAnalysisDto> batchFutureResult = batch.stream().map(CompletableFuture::join).toList();

            Iterator<Food> foodIterator = batchFoods.iterator();
            Iterator<FoodAnalysisDto> resultIterator = batchFutureResult.iterator();

            while (foodIterator.hasNext() && resultIterator.hasNext()) {
                foodIterator.next();
                if (resultIterator.next() == null) {
                    foodIterator.remove();
                    resultIterator.remove();
                }
            }


            batchCreateTasteMetrics(batchFutureResult, batchFoods);
            batchUpdateFoods(batchFutureResult, batchFoods);
        }

    }

    @Async
    @Transactional
    private void batchCreateTasteMetrics(List<FoodAnalysisDto> batch, List<Food> batchFoods) {

        String sql = "INSERT INTO taste_metrics (food_id, description, spicy, salty, sweet, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, batchFoods.get(i).getId());
                ps.setString(2, batch.get(i).getDescription());
                ps.setBigDecimal(3, batch.get(i).getSpicy());
                ps.setBigDecimal(4, batch.get(i).getSalty());
                ps.setBigDecimal(5, batch.get(i).getSweet());
                ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));

            }

            @Override
            public int getBatchSize() {
                return batch.size();
            }
        });
    }


    @Async
    @Transactional
    private void batchUpdateFoods(List<FoodAnalysisDto> batch, List<Food> batchFoods) {
        String sql = "UPDATE food SET price = ?, updated_at = ? WHERE id = ?";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, batch.get(i).getPrice());
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                ps.setLong(3, batchFoods.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return batch.size();
            }
        });
    }

}
