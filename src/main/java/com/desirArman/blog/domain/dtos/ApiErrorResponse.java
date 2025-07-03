package com.desirArman.blog.domain.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {

    private int status;

    private String message;

    private List<FieldError> errors;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldError{
        private int status;
        private String messsage;
    }
}
