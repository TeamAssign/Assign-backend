package com.team3.assign_back.global.common;


import com.team3.assign_back.global.enums.FoodEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.team3.assign_back.global.enums.FoodEnum.FoodType.COMPANYDINNER;
import static com.team3.assign_back.global.enums.FoodEnum.FoodType.SOLO;

@RestController
@RequestMapping("/api")
@Tag(name = "Dummy API", description = "테스트용 Dummy API")
public class DummyController {

    //메인페이지 사내 한달 식사 통계
    @Operation(
            summary = "사내 한달 식사 통계 조회",
            description = "사내에서 한달 동안 선택된 음식 유형의 통계를 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 데이터를 반환합니다.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseDto.class)
            )
    )
    @GetMapping("/summary/company")
    public ResponseEntity<ApiResponseDto<Map<String, Integer>>> stats(){

        Map<String, Integer> dataMap = new HashMap<>();
        dataMap.put("한식", 35);
        dataMap.put("중식", 15);
        dataMap.put("일식", 12);
        dataMap.put("양식", 10);
        dataMap.put("분식", 10);
        dataMap.put("아시안", 8);
        dataMap.put("패스트푸드", 6);
        dataMap.put("기타", 4);

        return ApiResponseDto.from(HttpStatus.OK, "사내 한달 통계데이터입니다.", dataMap);
    }

    @Operation(
            summary = "오늘의 추천 메뉴 조회",
            description = "AI 기반으로 추천된 오늘의 점심 메뉴를 제공합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "추천된 메뉴를 반환합니다.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseDto.class)
            )
    )
    @GetMapping("/recommendation/today")
    public ResponseEntity<ApiResponseDto<Map<String, String>>> recommendationToday(){

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("name", "짜장면");
        dataMap.put("imageUrl", "https://i.ibb.co/DfzGjPSr/butterfly-fairies-3840x2160-17273.jpg");

        return ApiResponseDto.from(HttpStatus.OK, "오늘의 추천입니다.", dataMap);
    }

    @Operation(
            summary = "사용자 선호도 조회",
            description = "사용자의 선호 가격대, 키워드, 추천 정확도 등의 정보를 제공합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "사용자 선호도를 반환합니다.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseDto.class)
            )
    )
    @GetMapping("/users/preferences")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> usersPreferences(){

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("price", "1~2만원");
        dataMap.put("keyword", "가성비");
        dataMap.put("accuracy", 92.0);
        dataMap.put("price", 86.8);

        return ApiResponseDto.from(HttpStatus.OK, "회원의 취향입니다.", dataMap);
    }

//    @GetMapping("/recommendations/menu")
//    public ResponseEntity<ApiResponseDto<Map<String, Object>>> recommendationsMenu(@RequestParam("random") boolean isRandom, @RequestParam("order") Integer order){
//
//        Map<String, Object> dataMap = new HashMap<>();
//        dataMap.put("name", "짜장면");
//        dataMap.put("imageUrl", "https://i.ibb.co/DfzGjPSr/butterfly-fairies-3840x2160-17273.jpg");
//        dataMap.put("accuracy", 86.3);
//
//        return ApiResponseDto.from(HttpStatus.OK, "추천 메뉴를 보내드립니다.", dataMap);
//
//    }

    @Operation(
            summary = "추천 메뉴에 해당하는 맛집 리스트 조회",
            description = "사용자가 선택한 메뉴와 관련된 맛집 리스트를 제공합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "맛집 리스트 반환 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
    )
    @Parameter(name = "menu", description = "조회할 메뉴 이름", required = true, example = "짜장면")
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

    @Operation(
            summary = "추천 메뉴 수락",
            description = "사용자가 특정 추천을 수락할 때 호출하는 API입니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "추천 수락 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
    )
    @PostMapping("/recommendations/menu")
    public ResponseEntity<ApiResponseDto<Void>> recommendationsMenuPost(@RequestBody Object o){
        return ApiResponseDto.from(HttpStatus.OK, "추천 수락을 하셨습니다.", null);
    }

    @Operation(
            summary = "추천 기록 조회",
            description = "사용자의 추천 기록을 페이지네이션하여 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "추천 기록 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
    )
    @GetMapping("/recommendations")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> recommendations
            (@RequestParam(value = "page", defaultValue = "1") int page,
             @RequestParam(value = "size", defaultValue = "10") int size){
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

        List<Map<String, Object>> participantList = new ArrayList<>();
        for(int i = 0; i < 3; i ++){
            Map<String, Object> participantMap = new HashMap<>();
            participantMap.put("id", i + 1);
            participantMap.put("name", "김말단" + (i + 1));
            participantMap.put("team", "개발" + (i + 1) + "팀");
            participantMap.put("profileImage", "https://search2.kakaocdn.net/argon/130x130_85_c/36hQpoTrVZp");

            participantList.add(participantMap);
        }

        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("recommendationId", 11L);
        contentMap.put("type", FoodEnum.FoodType.GROUP);
        contentMap.put("category", FoodEnum.FoodCategory.CHINESE);
        contentMap.put("accuracy", 78.3);
        contentMap.put("imageUrl", "https://i.ibb.co/DfzGjPSr/butterfly-fairies-3840x2160-17273.jpg");
        contentMap.put("isReviewed", false);
        contentMap.put("participants", participantList);

        List<Map<String, Object>> contents = new ArrayList<>();

        for(int i = 0; i < totalElements; i++){
            contents.add(contentMap);
        }

        dataMap.put("content", contents);

        return ApiResponseDto.from(HttpStatus.OK, "추천 기록이 생성되었습니다.", dataMap);
    }

    @Operation(
            summary = "후기 작성",
            description = "사용자가 특정 추천에 대한 후기를 작성합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "후기 작성 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
    )
    @PostMapping("/reviews")
    public ResponseEntity<ApiResponseDto<Void>> review(@RequestBody Object o){
        return ApiResponseDto.from(HttpStatus.OK, "후기 작성을 하셨습니다.", null);
    }

    @Operation(
            summary = "사용자 프로필 조회",
            description = "사용자의 프로필 정보를 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "사용자 프로필 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
    )
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

    @Operation(
            summary = "유저의 일주일 통계 데이터 조회",
            description = "유저가 일주일 동안 선택한 음식 유형의 통계를 제공합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "유저의 통계 데이터 반환 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
    )
    @GetMapping("/summary/users")
    public ResponseEntity<ApiResponseDto<Map<String, Integer>>> usersStats(){

        Map<String, Integer> dataMap = new HashMap<>();
        dataMap.put("한식", 35);
        dataMap.put("중식", 15);
        dataMap.put("일식", 12);
        dataMap.put("양식", 10);
        dataMap.put("분식", 10);
        dataMap.put("아시안", 8);
        dataMap.put("패스트푸드", 6);
        dataMap.put("기타", 4);

        return ApiResponseDto.from(HttpStatus.OK, "유저의 일주일 통계데이터입니다.", dataMap);
    }

    @Operation(
            summary = "사용자 후기 조회",
            description = "사용자의 후기를 페이지네이션하여 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "사용자 후기 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
    )
    @GetMapping("/users/reviews")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> usersReviews
            (@RequestParam(value = "page", defaultValue = "1") int page,
             @RequestParam(value = "size", defaultValue = "10") int size){
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
        contentMap.put("participants", null);

        List<Map<String, Object>> contents = new ArrayList<>();
        for(int i = 0; i < totalElements; i++){
            contents.add(contentMap);
        }

        dataMap.put("content", contents);

        return ApiResponseDto.from(HttpStatus.OK, "회원의 후기 기록을 조회하셨습니다.", dataMap);
    }

//    @GetMapping("/teams/{teamId}/profile")
//    public ResponseEntity<ApiResponseDto<Map<String, Object>>> teamsProfile(@PathVariable("teamId") Long teamId){
//        Map<String, Object> dataMap = new HashMap<>();
//
//        dataMap.put("team", "개발1팀");
//        dataMap.put("spicy", 3.2);
//        dataMap.put("sweet", 1.5);
//        dataMap.put("salty", 2.4);
//        dataMap.put("pros", "저는 짜장면 좋음");
//        dataMap.put("cons", "짬뽕 싫음");
//
//        return ApiResponseDto.from(HttpStatus.OK, "팀의 정보입니다.", dataMap);
//    }
//
//    @PutMapping("/teams/{teamId}/profile")
//    public ResponseEntity<ApiResponseDto<Void>> teamsProfilePut(@PathVariable("teamId") Long teamId){
//
//        return ApiResponseDto.from(HttpStatus.OK, "팀의 정보를 수정하셨습니다.", null);
//    }

    @Operation(
            summary = "팀의 일주일 통계 조회",
            description = "특정 팀의 일주일 동안 선택한 음식 유형의 통계를 제공합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "팀의 통계 데이터 반환 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
    )
    @Parameter(name = "teamId", description = "조회할 팀 ID", required = true, example = "1")
    @GetMapping("/summary/teams/{teamId}")
    public ResponseEntity<ApiResponseDto<Map<String, Integer>>> teamStats(@PathVariable("teamId") Long teamId){
        Map<String, Integer> dataMap = new HashMap<>();

        dataMap.put("한식", 35);
        dataMap.put("중식", 15);
        dataMap.put("일식", 12);
        dataMap.put("양식", 10);
        dataMap.put("분식", 10);
        dataMap.put("아시안", 8);
        dataMap.put("패스트푸드", 6);
        dataMap.put("기타", 4);
        dataMap.put("치킨", 87);
        dataMap.put("피자", 66);
        dataMap.put("햄버거", 50);
        dataMap.put("떡볶이", 32);
        dataMap.put("샐러드", 10);

        return ApiResponseDto.from(HttpStatus.OK, "팀의 일주일 통계데이터입니다.", dataMap);
    }

    @Operation(
            summary = "팀의 후기 조회",
            description = "특정 팀의 후기를 페이지네이션하여 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "팀 후기 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDto.class))
    )
    @Parameter(name = "teamId", description = "조회할 팀 ID", required = true, example = "1")
    @Parameter(name = "page", description = "페이지 번호", required = true, example = "1")
    @Parameter(name = "size", description = "페이지 크기", required = true, example = "10")
    @GetMapping("/teams/{teamId}/reviews")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> teamsReviews
            (@PathVariable("teamId") Long teamId,
             @RequestParam(value = "page", defaultValue = "1") int page,
             @RequestParam(value = "size", defaultValue = "10") int size){
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

        List<Map<String, Object>> participantList = new ArrayList<>();
        for(int i = 0; i < 7; i ++){
            Map<String, Object> participantMap = new HashMap<>();
            participantMap.put("id", i + 1);
            participantMap.put("name", "김말단" + (i + 1));
            participantMap.put("team", "개발1팀");
            participantMap.put("profileImage", "https://search2.kakaocdn.net/argon/130x130_85_c/36hQpoTrVZp");

            participantList.add(participantMap);
        }

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

//    @GetMapping("/team")
//    public ResponseEntity<ApiResponseDto<List<Map<String, Object>>>> teams(){
//
//        List<Map<String, Object>> dataList = new ArrayList<>();
//        for(int i = 0; i < 15; i ++){
//            Map<String, Object> dataMap = new HashMap<>();
//            dataMap.put("teamId", i + 1);
//            dataMap.put("teamName", "개발" + (i + 1) + "팀");
//            dataList.add(dataMap);
//        }
//
//        return ApiResponseDto.from(HttpStatus.OK, "팀 목록을 보내드립니다.", dataList);
//
//    }











}
