package com.team3.assign_back.domain.intermediate.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.assign_back.domain.intermediate.entity.Tag;
import com.team3.assign_back.domain.intermediate.repository.TagRepository;
import com.team3.assign_back.domain.recommendation.repository.CustomRecommendationRepository;
import com.team3.assign_back.domain.users.entity.Users;
import com.team3.assign_back.global.enums.TagEnum;
import com.team3.assign_back.global.exception.ErrorCode;
import com.team3.assign_back.global.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static com.team3.assign_back.domain.intermediate.prompt.TagPrompt.TAG_PROMPT;
import static com.team3.assign_back.global.constant.TagConstant.FOOD_LIST_SIZE_FOR_TAG;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagService {

    private final CustomRecommendationRepository customRecommendationRepository;
    private final TagRepository tagRepository;

    private final OpenAiChatModel chatModel;
    private final ObjectMapper objectMapper;

    @Transactional
    public void saveUserTag(Users user){

        List<String> foodNames = customRecommendationRepository.getRecommendationForTag(user.getId(), FOOD_LIST_SIZE_FOR_TAG);

        TagEnum.MealTag[] mealTags = null;
        try {
            mealTags = objectMapper.readValue(ChatClient.create(chatModel).prompt().system(TAG_PROMPT).user(foodNames.toString()).call().content(), TagEnum.MealTag[].class);
        } catch (JsonProcessingException e) {
            log.info("{},{}", foodNames, e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        if(mealTags == null || mealTags.length != 3){
            log.info("{},{}", Arrays.toString(mealTags), "ai파싱에러");
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        tagRepository.deleteByUsersId(user.getId());

        tagRepository.saveAll(Arrays.stream(mealTags).map(mealTag-> Tag.builder().mealTag(mealTag).users(user).build()).toList());

    }
}
