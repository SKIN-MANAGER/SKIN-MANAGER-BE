package com.skin_manager.skin_manager.model.dto.anonymous_forum.response.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnmFrmPagingResultDTO<T> {

    private LocalDateTime transactionTime;
    private String resultCode;
    private String description;
    private T data;
    private AnmFrmPaginationDTO anmFrmPaginationDTO;

    public static <T> AnmFrmPagingResultDTO<T> OK() {
        return (AnmFrmPagingResultDTO<T>) AnmFrmPagingResultDTO.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("OK")
                .description("OK")
                .build();
    }

    // DATA OK
    public static <T> AnmFrmPagingResultDTO<T> OK(T data) {
        return (AnmFrmPagingResultDTO<T>) AnmFrmPagingResultDTO.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("OK")
                .description("OK")
                .data(data)
                .build();
    }

    public static <T> AnmFrmPagingResultDTO<T> OK(T data, AnmFrmPaginationDTO anmFrmPaginationDTO) {
        return (AnmFrmPagingResultDTO<T>) AnmFrmPagingResultDTO.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("OK")
                .description("OK")
                .data(data)
                .anmFrmPaginationDTO(anmFrmPaginationDTO)
                .build();
    }

    public static <T> AnmFrmPagingResultDTO<T> ERROR(String description) {
        return (AnmFrmPagingResultDTO<T>) AnmFrmPagingResultDTO.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("ERROR")
                .description(description)
                .build();
    }
}
