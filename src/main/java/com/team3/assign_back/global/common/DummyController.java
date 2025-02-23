package com.team3.assign_back.global.common;


import com.team3.assign_back.global.enums.FoodEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.team3.assign_back.global.enums.FoodEnum.FoodType.COMPANYDINNER;
import static com.team3.assign_back.global.enums.FoodEnum.FoodType.SOLO;

@RestController
@RequestMapping("/api")
public class DummyController {


    //메인페이지 사내 일주일 식사 통계
    @GetMapping("/stats")
    public ResponseEntity<ApiResponseDto<Map<String, Integer>>> stats(){

        Map<String, Integer> dataMap = new HashMap<>();
        dataMap.put("한식", 35);
        dataMap.put("중식", 15);
        dataMap.put("일식", 12);
        dataMap.put("양식", 10);
        dataMap.put("분식", 10);
        dataMap.put("아시아", 8);
        dataMap.put("패스트푸드", 6);
        dataMap.put("기타", 4);

        return ApiResponseDto.from(HttpStatus.OK, "사내 일주일 통계데이터입니다.", dataMap);

    }

    @GetMapping("/recommendation/today")
    public ResponseEntity<ApiResponseDto<Map<String, String>>> recommendationToday(){

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("name", "짜장면");
        dataMap.put("imageUrl", "https://i.ibb.co/DfzGjPSr/butterfly-fairies-3840x2160-17273.jpg");

        return ApiResponseDto.from(HttpStatus.OK, "오늘의 추천입니다.", dataMap);

    }

    @GetMapping("/users/preferences")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> usersPreferences(){

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("price", "1~2만원");
        dataMap.put("keyword", "가성비");
        dataMap.put("accuracy", 92.0);
        dataMap.put("price", 86.8);

        return ApiResponseDto.from(HttpStatus.OK, "회원의 취향입니다.", dataMap);

    }

    @GetMapping("/recommendations/menu")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> recommendationsMenu(){

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", "짜장면");
        dataMap.put("imageUrl", "https://i.ibb.co/DfzGjPSr/butterfly-fairies-3840x2160-17273.jpg");
        dataMap.put("accuracy", 86.3);

        return ApiResponseDto.from(HttpStatus.OK, "추천 메뉴를 보내드립니다.", dataMap);

    }

    @GetMapping("/recommendations/menu/{menu}")
    public ResponseEntity<ApiResponseDto<List<Map<String, Object>>>> recommendationsMenuMenuGet(@PathVariable("menu") String menu){


        List<Map<String, Object>> dataList = new ArrayList<>();
        for(int i = 0; i < 15; i ++){
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("place_name", "카카오프렌즈 코엑스점");
            dataMap.put("distance", 418);
            dataMap.put("road_address_name", "서울 강남구 영동대로 513");
            dataMap.put("place_url", "http://place.map.kakao.com/26338954");
            dataMap.put("imageUrl", "https://search2.kakaocdn.net/argon/130x130_85_c/36hQpoTrVZp");
            dataList.add(dataMap);
        }

        return ApiResponseDto.from(HttpStatus.OK, "추천 메뉴에 해당하는 맛집 리스트를 보내드립니다.", dataList);

    }

    @PostMapping("/recommendations/menu")
    public ResponseEntity<ApiResponseDto<Void>> recommendationsMenuPost(@RequestBody Object o){



        return ApiResponseDto.from(HttpStatus.OK, "추천 수락을 하셨습니다.", null);

    }


    @GetMapping("/recommendations")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> recommendations(@RequestParam("page") int page, @RequestParam("size") int size){

        Map<String, Object> dataMap = new HashMap<>();

        long totalElements = 300L;
        int totalPages = totalElements == 0 ? 1 : (int) Math.ceil((double) totalElements / size);

        Map<String, Object> pageInfoMap = new HashMap<>();
        pageInfoMap.put("hasNextPage", page < totalPages);
        pageInfoMap.put("hasPrevPage", page > 1);
        pageInfoMap.put("totalPages", totalPages);
        pageInfoMap.put("totalElements", totalElements);
        pageInfoMap.put("currentPage", page);
        pageInfoMap.put("size", size);

        dataMap.put("pageInfo", pageInfoMap);

        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("recommendationId", 11L);
        contentMap.put("type", FoodEnum.FoodType.GROUP);
        contentMap.put("category", FoodEnum.FoodCategory.CHINESE);
        contentMap.put("accuracy", 78.3);
        contentMap.put("imageUrl", "https://i.ibb.co/DfzGjPSr/butterfly-fairies-3840x2160-17273.jpg");
        contentMap.put("isReviewed", false);
        contentMap.put("participants", List.of(1, 2, 3));

        List<Map<String, Object>> contents = new ArrayList<>();
        for(int i = 0; i < totalElements; i++){
            contents.add(contentMap);
        }

        dataMap.put("content", contents);



        return ApiResponseDto.from(HttpStatus.OK, "추천 기록이 생성되었습니다.", dataMap);

    }


    @PostMapping("/reviews")
    public ResponseEntity<ApiResponseDto<Void>> review(@RequestBody Object o){



        return ApiResponseDto.from(HttpStatus.OK, "후기 작성을 하셨습니다.", null);

    }


    @GetMapping("/users/profile")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> usersProfile(){

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", "김말단");
        dataMap.put("team", "개발1팀");
        dataMap.put("spicy", 3.2);
        dataMap.put("sweet", 1.5);
        dataMap.put("salty", 2.4);
        dataMap.put("pros", "저는 짜장면 좋음");
        dataMap.put("cons", "짬뽕 싫음");
        dataMap.put("profileImageUrl", "https://i.ibb.co/DfzGjPSr/butterfly-fairies-3840x2160-17273.jpg");

        return ApiResponseDto.from(HttpStatus.OK, "회원의 개인정보입니다.", dataMap);

    }

    @GetMapping("/users/stats")
    public ResponseEntity<ApiResponseDto<Map<String, Integer>>> usersStats(){

        Map<String, Integer> dataMap = new HashMap<>();
        dataMap.put("한식", 35);
        dataMap.put("중식", 15);
        dataMap.put("일식", 12);
        dataMap.put("양식", 10);
        dataMap.put("분식", 10);
        dataMap.put("아시아", 8);
        dataMap.put("패스트푸드", 6);
        dataMap.put("기타", 4);

        return ApiResponseDto.from(HttpStatus.OK, "유저의 일주일 통계데이터입니다.", dataMap);

    }

    @GetMapping("/users/reviews")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> usersReviews(@RequestParam("page") int page, @RequestParam("size") int size){

        Map<String, Object> dataMap = new HashMap<>();

        long totalElements = 300L;
        int totalPages = totalElements == 0 ? 1 : (int) Math.ceil((double) totalElements / size);

        Map<String, Object> pageInfoMap = new HashMap<>();
        pageInfoMap.put("hasNextPage", page < totalPages);
        pageInfoMap.put("hasPrevPage", page > 1);
        pageInfoMap.put("totalPages", totalPages);
        pageInfoMap.put("totalElements", totalElements);
        pageInfoMap.put("currentPage", page);
        pageInfoMap.put("size", size);

        dataMap.put("pageInfo", pageInfoMap);

        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("recommendationId", 11L);
        contentMap.put("type", SOLO);
        contentMap.put("menu", "짜장면");
        contentMap.put("category", FoodEnum.FoodCategory.CHINESE);
        contentMap.put("comment", "노맛");
        contentMap.put("imageUrl", "https://i.ibb.co/DfzGjPSr/butterfly-fairies-3840x2160-17273.jpg");
        contentMap.put("star", 1);
        contentMap.put("participants", List.of(1));

        List<Map<String, Object>> contents = new ArrayList<>();
        for(int i = 0; i < totalElements; i++){
            contents.add(contentMap);
        }

        dataMap.put("content", contents);



        return ApiResponseDto.from(HttpStatus.OK, "회원의 후기 기록을 조회하셨습니다.", dataMap);

    }



    @GetMapping("/teams/{teamId}/profile")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> teamsProfile(@PathVariable("teamId") Long teamId){

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("team", "개발1팀");
        dataMap.put("spicy", 3.2);
        dataMap.put("sweet", 1.5);
        dataMap.put("salty", 2.4);
        dataMap.put("pros", "저는 짜장면 좋음");
        dataMap.put("cons", "짬뽕 싫음");

        return ApiResponseDto.from(HttpStatus.OK, "팀의 정보입니다.", dataMap);

    }

    @PutMapping("/teams/{teamId}/profile")
    public ResponseEntity<ApiResponseDto<Void>> teamsProfilePut(@PathVariable("teamId") Long teamId){


        return ApiResponseDto.from(HttpStatus.OK, "팀의 정보를 수정하셨습니다.", null);

    }


    @GetMapping("/teams/{teamId}/stats")
    public ResponseEntity<ApiResponseDto<Map<String, Integer>>> teamStats(@PathVariable("teamId") Long teamId){

        Map<String, Integer> dataMap = new HashMap<>();
        dataMap.put("한식", 35);
        dataMap.put("중식", 15);
        dataMap.put("일식", 12);
        dataMap.put("양식", 10);
        dataMap.put("분식", 10);
        dataMap.put("아시아", 8);
        dataMap.put("패스트푸드", 6);
        dataMap.put("기타", 4);
        dataMap.put("치킨", 87);
        dataMap.put("피자", 66);
        dataMap.put("햄버거", 50);
        dataMap.put("떡볶이", 32);
        dataMap.put("샐러드", 10);

        return ApiResponseDto.from(HttpStatus.OK, "팀의 일주일 통계데이터입니다.", dataMap);

    }


    @GetMapping("/teams/{teamId}/reviews")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> teamsReviews(@PathVariable("teamId") Long teamId, @RequestParam("page") int page, @RequestParam("size") int size){

        Map<String, Object> dataMap = new HashMap<>();

        long totalElements = 300L;
        int totalPages = totalElements == 0 ? 1 : (int) Math.ceil((double) totalElements / size);

        Map<String, Object> pageInfoMap = new HashMap<>();
        pageInfoMap.put("hasNextPage", page < totalPages);
        pageInfoMap.put("hasPrevPage", page > 1);
        pageInfoMap.put("totalPages", totalPages);
        pageInfoMap.put("totalElements", totalElements);
        pageInfoMap.put("currentPage", page);
        pageInfoMap.put("size", size);

        dataMap.put("pageInfo", pageInfoMap);

        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("recommendationId", 11L);
        contentMap.put("type", COMPANYDINNER);
        contentMap.put("menu", "짜장면");
        contentMap.put("category", FoodEnum.FoodCategory.CHINESE);
        contentMap.put("comment", "노맛");
        contentMap.put("imageUrl", "https://i.ibb.co/DfzGjPSr/butterfly-fairies-3840x2160-17273.jpg");
        contentMap.put("star", 1);
        contentMap.put("participants", List.of(1, 2, 3, 4, 5, 6, 7));

        List<Map<String, Object>> contents = new ArrayList<>();
        for(int i = 0; i < totalElements; i++){
            contents.add(contentMap);
        }

        dataMap.put("content", contents);



        return ApiResponseDto.from(HttpStatus.OK, "팀의 후기 기록을 조회하셨습니다.", dataMap);

    }

    @GetMapping("/team")
    public ResponseEntity<ApiResponseDto<List<Map<String, Object>>>> teams(){

        List<Map<String, Object>> dataList = new ArrayList<>();
        for(int i = 0; i < 15; i ++){
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("teamId", i + 1);
            dataMap.put("teamName", "개발" + (i + 1) + "팀");
            dataList.add(dataMap);
        }

        return ApiResponseDto.from(HttpStatus.OK, "팀 목록을 보내드립니다.", dataList);

    }











}
