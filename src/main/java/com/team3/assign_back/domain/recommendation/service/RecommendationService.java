package com.team3.assign_back.domain.recommendation.service;


import com.team3.assign_back.domain.food.dto.FoodResponseDto;
import com.team3.assign_back.domain.recommendation.repository.CustomRecommendationRepository;
import com.team3.assign_back.global.enums.FoodEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendationService {

    private final CustomRecommendationRepository customRecommendationRepository;



    public FoodResponseDto getRecommendation(Long userId, FoodEnum.FoodCategory category, FoodEnum.FoodType type, List<Long> participants){

        if (type != FoodEnum.FoodType.GROUP) {
            throw new RuntimeException();
        }
        return customRecommendationRepository.getRecommendation(userId, category, participants);


    }

    public FoodResponseDto getRecommendation(Long userId, FoodEnum.FoodCategory category, FoodEnum.FoodType type){

        return switch (type){
            case SOLO -> customRecommendationRepository.getRecommendation(userId, category);
            case COMPANYDINNER ->customRecommendationRepository.getRecommendationForTeam(userId, category);
            default -> throw new RuntimeException();
        };


    }




}
