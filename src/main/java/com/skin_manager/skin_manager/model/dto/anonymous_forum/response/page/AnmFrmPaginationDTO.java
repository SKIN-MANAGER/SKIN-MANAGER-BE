package com.skin_manager.skin_manager.model.dto.anonymous_forum.response.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnmFrmPaginationDTO {

    // 페이지 당 보여지는 도서 정보 최대 갯수
    private int pageSize;

    // 현재 페이지
    int page;

    // 총 게시글 수
    int totalListCnt;

    // 총 페이지 수
    int totalPageCnt;

    // 시작 페이지
    int startPage;

    // 마지막 페이지
    int endPage;


    // 인덱스
    int startIndex;

    public AnmFrmPaginationDTO(Integer totalListCnt, Integer page, Integer pageSize) {
        this.pageSize = pageSize;

        // 현재 페이지
        this.page = page;

        // 총 게시글 수
        this.totalListCnt = totalListCnt;

        // 총 페이지 수
        totalPageCnt = (int) Math.ceil(totalListCnt * 1.0 / this.pageSize);

        startIndex = (this.page - 1) * this.pageSize;
    }
}
