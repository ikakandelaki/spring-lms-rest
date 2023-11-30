package com.azry.lms.entity.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    AVAILABLE("available"), BORROWED("borrowed");

    private final String name;
}
