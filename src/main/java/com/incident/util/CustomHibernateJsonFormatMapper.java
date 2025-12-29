package com.incident.util;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.format.AbstractJsonFormatMapper;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.json.JsonMapper;

import java.lang.reflect.Type;

/**
 * Hibernate JSON FormatMapper for Jackson 3 (tools.jackson.*).
 * Needed so Hibernate knows how to convert Java objects <-> JSON for JSON/JSONB columns.
 */
public final class CustomHibernateJsonFormatMapper extends AbstractJsonFormatMapper {

    private final JsonMapper jsonMapper;

    public CustomHibernateJsonFormatMapper() {
        this(JsonMapper.builder().build());
    }

    public CustomHibernateJsonFormatMapper(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public <T> void writeToTarget(T value, JavaType<T> javaType, Object target, WrapperOptions options) {
        try {
            jsonMapper.writerFor(jsonMapper.constructType(javaType.getJavaType()))
                    .writeValue((JsonGenerator) target, value);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to write JSON value", e);
        }
    }

    @Override
    public <T> T readFromSource(JavaType<T> javaType, Object source, WrapperOptions options) {
        try {
            return jsonMapper.readValue((JsonParser) source, jsonMapper.constructType(javaType.getJavaType()));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read JSON value", e);
        }
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return JsonParser.class.isAssignableFrom(sourceType);
    }

    @Override
    public boolean supportsTargetType(Class<?> targetType) {
        return JsonGenerator.class.isAssignableFrom(targetType);
    }

    @Override
    public <T> T fromString(CharSequence charSequence, Type type) {
        try {
            return jsonMapper.readValue(charSequence.toString(), jsonMapper.constructType(type));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse JSON string", e);
        }
    }

    @Override
    public <T> String toString(T value, Type type) {
        try {
            return jsonMapper.writerFor(jsonMapper.constructType(type)).writeValueAsString(value);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to serialize JSON value", e);
        }
    }
}
