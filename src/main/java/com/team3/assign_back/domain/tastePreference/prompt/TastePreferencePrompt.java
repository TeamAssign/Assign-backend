package com.team3.assign_back.domain.tastePreference.prompt;

import org.springframework.stereotype.Component;


@Component
public class TastePreferencePrompt {


    public final static String USER_PROMPT_LIKES = """
     [좋아하는 음식 혹은 취향]
     %s
     
     [맛 취향]
     내가 선호하는 맛은 짠맛이 %.2f, 단맛이 %.2f, 매운맛이 %.2f입니다.
     """;

    public final static String USER_PROMPT_DISLIKES = """     
     [싫어하는 음식 혹은 취향]
     %s
     """;

}


