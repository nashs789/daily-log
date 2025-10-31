package com.nashs.daily_log.infra.common.converter;

import jakarta.persistence.AttributeConverter;

import java.util.Map;

public class StringToMapConverter implements AttributeConverter<Map<String, String>, String> {
    @Override
    public String convertToDatabaseColumn(Map<String, String> attribute) {
        return "";
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String dbData) {
        return Map.of();
    }
}
