package com.azry.lms.dto.response;

import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String details) {
}
