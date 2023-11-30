package com.azry.lms.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MappingUtil {
    private final static ModelMapper MODEL_MAPPER = new ModelMapper();

    public static <T> T map(Object source, Class<T> destinationType) {
        return MODEL_MAPPER.map(source, destinationType);
    }

}
