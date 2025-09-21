package com.shchff.producer_service.exception;

import java.util.List;

public record ErrorResponse(String error, List<FieldErrorResponse> details) {}
