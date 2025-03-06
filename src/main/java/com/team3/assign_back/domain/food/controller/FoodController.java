package com.team3.assign_back.domain.food.controller;


import com.team3.assign_back.domain.food.dto.FileInfoDto;
import com.team3.assign_back.domain.food.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @GetMapping("/image")
    public ResponseEntity<Void> getFoodImages(){
        foodService.batchSaveFoodImageUrl();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<Void> test(List<FileInfoDto> fileInfoDtos){
        foodService.test(fileInfoDtos);
        return new ResponseEntity<>(HttpStatus.OK);
    }


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
