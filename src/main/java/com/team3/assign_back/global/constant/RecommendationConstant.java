package com.team3.assign_back.global.constant;


import org.springframework.stereotype.Component;

@Component
public class RecommendationConstant {

    public static final int RECOMMENDATION_QUERY_LIMIT_COUNT = 5;

    public static final float LIKE_EMBEDDING_INITIAL_LEARNING_RATE = 0.07f;
    public static final float DISLIKE_EMBEDDING_INITIAL_LEARNING_RATE = 0.05f;
    public static final float EMBEDDING_LEARNING_RATE_MINIMUM = 0.001f;
    public static final float LIKE_EMBEDDING_DECAY_BASE = 0.95f;
    public static final float DISLIKE_EMBEDDING_DECAY_BASE = 0.95f;
    public static final float LIKE_EMBEDDING_DECAY_RATE = 0.0333f;
    public static final float DISLIKE_EMBEDDING_DECAY_RATE = 0.0333f;

    public static final float LIKE_EMBEDDING_DECAY_FACTOR = (float)Math.pow(LIKE_EMBEDDING_DECAY_BASE, LIKE_EMBEDDING_DECAY_RATE);
    public static final float DISLIKE_EMBEDDING_DECAY_FACTOR = (float)Math.pow(DISLIKE_EMBEDDING_DECAY_BASE, DISLIKE_EMBEDDING_DECAY_RATE);


}
