package com.team3.assign_back.global.common;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

@Converter
public class FloatArrayToVectorConverter implements AttributeConverter<float[], String> {
    @Override
    public String convertToDatabaseColumn(float[] attribute) {
        if (attribute == null) return null;
        return Arrays.toString(attribute).replace(" ", "");
    }

    @Override
    public float[] convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        String[] values = dbData.substring(1, dbData.length() - 1).split(",");
        float[] result = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = Float.parseFloat(values[i]);
        }
        return result;
    }
}
