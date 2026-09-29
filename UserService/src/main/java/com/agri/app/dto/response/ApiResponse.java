package com.agri.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL) // Omits null fields from outgoing JSON
public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        Object errors,
        LocalDateTime localDateTime

) {
    public static <T> ApiResponse<T> ok(String message,T data){
        return new ApiResponse<>(true,message,data,null,LocalDateTime.now());
    }

    public static <T> ApiResponse<T> ok(String message){
        return new ApiResponse<>(true,message,null,null,LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(String message,Object error){
        return new ApiResponse<>(false,message,null,error,LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(String message){
        return new ApiResponse<>(false,message,null,null,LocalDateTime.now());
    }
}
