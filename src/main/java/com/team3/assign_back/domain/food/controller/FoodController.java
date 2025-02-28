package com.team3.assign_back.domain.food.controller;


import com.team3.assign_back.domain.food.dto.FoodAnalysisRequestDto;
import com.team3.assign_back.domain.food.service.FoodService;
import com.team3.assign_back.domain.tastePreference.service.TastePreferenceEmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

//    @PostMapping
//    public ResponseEntity<Void> createFoods(@RequestBody FoodAnalysisRequestDto foodAnalysisRequestDto){
//        foodService.createFoods(foodAnalysisRequestDto);
//
//        return ResponseEntity.status(200).build();
//
//    }
//
//
//    @PostMapping("/taste")
//    public ResponseEntity<Void> createTasteMetrics(){
//        foodService.createTasteMetrics();
//
//        return ResponseEntity.status(200).build();
//
//    }
//
//    @PostMapping("/embedding")
//    public ResponseEntity<Void> createTasteMetricsEmbedding(){
//        foodService.createTasteMetricsEmbedding();
//
//        return ResponseEntity.status(200).build();
//
//    }

}
