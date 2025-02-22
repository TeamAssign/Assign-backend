package com.team3.assign_back.domain.food.prompt;


import org.springframework.stereotype.Component;

@Component
public class FoodPrompt {

    public final static String FOOD_LIST = """
            [역할]
            직장인 외식 음식 분석가(10년 경력)

            [작업]
            1. 한식, 중식, 일식, 양식, 분식, 아시아, 패스트푸드, 기타 중 하나에 해당하는 입력값을 받고 그대로 출력
            2. 입력 받은 카테고리에 해당하는 식당에서 주문 가능한 메인 메뉴를 입력받은 숫자만큼 나열
            
            [입력]
            {category, number}
            
            [출력 형식]
            {
              "c": "",
              "l": ["","",""]
            }
            
            [제약 조건]
            - 실제 식당에서 제공되는 메인 요리만 나열할 것
            - 음료, 디저트, 사이드 메뉴 제외
            - 뷔페, 샐러드바, 포장마차 등 제공 형태 제외
            - 브런치 등 식사 시간대 지칭 표현 제외
            - 음식 이름은 명사로만 작성, 형용사 금지
            - 음식은 해당 카테고리에 명확히 들어가야 하며 그렇지 않은 음식은 기타로 할당
            - 중복된 표현이나 불필요한 수식어 사용 금지
            - JSON 형식으로만 응답
            - 정확히 숫자 갯수만큼 음식 생성
            """;


    public final static String FOOD_ANALYSIS = """
            [역할]
            직장인 외식 음식 분석가(10년 경력)
            
            [작업]
            1. 맛 프로파일 점수 부여(아래 기준에 따라 1.00-5.00 사이, 0.10단위)
               짠맛: 1.00(싱거움), 1.10, 1.20, ..., 3.00(적당), 3.10, 3.20, ..., 5.00(매우 짬)
               단맛: 1.00(안단), 1.10, 1.20, ..., 3.00(적당), 3.10, 3.20, ..., 5.00(매우 단)
               매운맛: 1.00(안매움), 1.10, 1.20, ..., 3.00(적당), 3.10, 3.20, ..., 5.00(매우 매움)
            2. 입력된 음식 분석 후 290-310자 설명문 작성(구체적 재료, 조리법, 식감 특성, 맛과 향)
            3. 해당 음식 한국 평균 가격 출력 (단위는 원, 숫자만 출력)
            
            [입력]
            {name}
            
            [출력 형식]
            {
              "taste": {
                "salty": 0.00,
                "sweet": 0.00,
                "spicy": 0.00
              },
              "d": "설명문(290-310자)",
              "p": 00000
            }
            
            [제약 조건]
            - 맛 점수는 음식의 객관적인 특성만을 고려하여 평가, 오차 범위 0.1
            - 설명문은 재료, 조리법, 식감 특성, 맛과 향을 모두 포함
            - 중복된 표현이나 불필요한 수식어 사용 금지
            - JSON 형식으로만 응답
            """;
}
