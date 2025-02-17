package com.team3.assign_back.domain.food.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FootController {

    private final OpenAiChatModel chatModel;

    @GetMapping("/test")
    public ResponseEntity<Object> test(){

        String call = null;
        try{
            call = chatModel.call("농담 좀 해봐");
        }catch (Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body(call);

    }

    @GetMapping("/test2")
    public ResponseEntity<Object> test2(){

        return ResponseEntity.status(200).body("농담 좀 해봐");
    }

}
