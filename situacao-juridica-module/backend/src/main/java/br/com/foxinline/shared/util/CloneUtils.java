package br.com.foxinline.shared.util;

import com.fasterxml.jackson.databind.ObjectMapper;


public class CloneUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T clonar(T entidade, Class<T> clazz) {
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(entidade);
            return objectMapper.readValue(bytes, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao clonar entidade", e);
        }
    }
}
