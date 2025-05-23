package com.team3.assign_back.domain.food.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.assign_back.domain.food.dto.FoodAnalysisDto;
import com.team3.assign_back.domain.food.dto.FoodAnalysisRequestDto;
import com.team3.assign_back.domain.food.entity.Food;
import com.team3.assign_back.domain.food.entity.TasteMetrics;
import com.team3.assign_back.domain.food.repository.FoodRepository;
import com.team3.assign_back.domain.food.repository.TasteMetricsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.team3.assign_back.domain.food.prompt.FoodPrompt.FOOD_ANALYSIS;
import static com.team3.assign_back.global.constant.FoodConstant.FOOD_LIST_BATCH_SIZE;

@Service
@Slf4j
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final TasteMetricsRepository tasteMetricsRepository;

    private final OpenAiChatModel chatModel;
    private final OpenAiEmbeddingModel embeddingModel;

    private final ObjectMapper objectMapper;

    private final JdbcTemplate jdbcTemplate;


    public void createFoods(FoodAnalysisRequestDto foodAnalysisRequestDto){

        String category = foodAnalysisRequestDto.getCategory().name();
        List<String> foodNames = foodAnalysisRequestDto.getFoodNames();
        foodNames.removeAll(foodRepository.findNamesIn(foodNames));



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


            filterTwoLists(batchFoods, batchFutureResult);

            batchCreateTasteMetrics(batchFutureResult, batchFoods);
            batchUpdateFoods(batchFutureResult, batchFoods);
        }

    }

    @Async
    @Transactional
    private void batchCreateTasteMetrics(List<FoodAnalysisDto> batch, List<Food> batchFoods) {

        String sql = "INSERT INTO taste_metrics (food_id, description, description_for_company_dinner, spicy, salty, sweet, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, batchFoods.get(i).getId());
                ps.setString(2, batch.get(i).getDescription());
                ps.setString(3, batch.get(i).getDescriptionForCompanyDinner());
                ps.setBigDecimal(4, batch.get(i).getSpicy());
                ps.setBigDecimal(5, batch.get(i).getSalty());
                ps.setBigDecimal(6, batch.get(i).getSweet());
                ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
                ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));

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



    public void createTasteMetricsEmbedding(){

        List<TasteMetrics> tasteMetricsWithoutEmbedding= tasteMetricsRepository.customFindTasteMetricsWithoutEmbedding();

        List<String> embeddingTexts = tasteMetricsWithoutEmbedding.stream()
                .map(
                        tasteMetrics -> String
                                .format("%s 이 음식은 짠맛이 %.2f, 단맛이 %.2f, 매운맛이 %.2f입니다.",
                                        tasteMetrics.getDescription(),
                                        tasteMetrics.getSpicy(),
                                        tasteMetrics.getSweet(),
                                        tasteMetrics.getSpicy())
                )
                .toList();
        List<String> embeddingTextsForCompanyDinner = tasteMetricsWithoutEmbedding.stream()
                .map(
                        tasteMetrics -> String
                                .format("%s 이 음식은 짠맛이 %.2f, 단맛이 %.2f, 매운맛이 %.2f입니다.",
                                        tasteMetrics.getDescriptionForCompanyDinner(),
                                        tasteMetrics.getSpicy(),
                                        tasteMetrics.getSweet(),
                                        tasteMetrics.getSpicy())
                )
                .toList();


        List<float[]> embeddingVectorList = embeddingTexts.stream().map(embeddingModel::embed).toList();
        List<float[]> embeddingVectorListForCompanyDinner = embeddingTextsForCompanyDinner.stream().map(embeddingModel::embed).toList();


        filterThreeLists(tasteMetricsWithoutEmbedding, embeddingVectorList, embeddingVectorListForCompanyDinner);

        List<String> embeddingVectorToStringList = embeddingVectorList.stream().map(vector -> Arrays.toString(vector)
                .replace(" ", "")).toList();
        List<String> embeddingVectorToStringListForCompanyDinner = embeddingVectorListForCompanyDinner.stream().map(vector -> Arrays.toString(vector)
                .replace(" ", "")).toList();

        batchCreateTasteMetricsEmbedding(embeddingVectorToStringList, embeddingVectorToStringListForCompanyDinner, tasteMetricsWithoutEmbedding);

    }



    @Async
    @Transactional
    private void batchCreateTasteMetricsEmbedding(List<String> embeddingVectorToStringList, List<String> embeddingVectorToStringListForCompanyDinner, List<TasteMetrics> tasteMetricsWithoutEmbedding) {

        String sql = "INSERT INTO taste_metrics_embedding (taste_metrics_id, text_embedding, text_for_company_dinner_embedding, created_at, updated_at) VALUES (?, ?::vector, ?::vector, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, tasteMetricsWithoutEmbedding.get(i).getFoodId());
                ps.setString(2, embeddingVectorToStringList.get(i));
                ps.setString(3, embeddingVectorToStringListForCompanyDinner.get(i));
                ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

            }

            @Override
            public int getBatchSize() {
                return tasteMetricsWithoutEmbedding.size();
            }
        });

    }




    private static void filterTwoLists(List<?> shouldBeFilteredList, List<?> possiblyNullableList) {
        Iterator<?> iterator1 = shouldBeFilteredList.iterator();
        Iterator<?> iterator2 = possiblyNullableList.iterator();

        while (iterator1.hasNext() && iterator2.hasNext()) {
            iterator1.next();
            if (iterator2.next() == null) {
                iterator1.remove();
                iterator2.remove();
            }
        }
    }

    private static void filterThreeLists(List<?> shouldBeFilteredList, List<?> possiblyNullableList, List<?> possiblyNullableList2) {
        Iterator<?> iterator1 = shouldBeFilteredList.iterator();
        Iterator<?> iterator2 = possiblyNullableList.iterator();
        Iterator<?> iterator3 = possiblyNullableList2.iterator();

        while (iterator1.hasNext() && iterator2.hasNext() && iterator3.hasNext()) {
            iterator1.next();
            if (iterator2.next() == null || iterator3.next() == null) {
                iterator1.remove();
                iterator2.remove();
                iterator3.remove();
            }
        }
    }



}
