package com.skin_manager.skin_manager.repository.anonymous_forum;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.skin_manager.skin_manager.model.dto.anonymous_forum.request.search.AnmFrmSrchCondDTO;
import com.skin_manager.skin_manager.model.entity.AnonymousForum.AnmFrmEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.skin_manager.skin_manager.model.entity.AnonymousForum.QAnmFrmEntity.anmFrmEntity;

@RequiredArgsConstructor
@Repository
public class CstAnmFrmRepo {

    private final JPAQueryFactory queryFactory;

    public Page<AnmFrmEntity> findAllBySearchCondition(Pageable pageable, AnmFrmSrchCondDTO anmFrmSrchCondDTO) {

        JPAQuery<AnmFrmEntity> query =
                queryFactory.selectFrom(anmFrmEntity).where(
                        searchKeywords(anmFrmSrchCondDTO.getSearchCategory(), anmFrmSrchCondDTO.getSearchValue())
                );


        long total = query.stream().count();

        List<AnmFrmEntity> results = query
                .where(
                        searchKeywords(anmFrmSrchCondDTO.getSearchCategory(), anmFrmSrchCondDTO.getSearchValue()))
                .offset((long)(pageable.getPageNumber()-1) * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .orderBy(anmFrmEntity.registeredAt.desc())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression searchKeywords(String searchCategory, String searchValue) {
        if("title".equals(searchCategory)) {
            if(StringUtils.hasLength(searchValue)) {
                return anmFrmEntity.title.contains(searchValue);
            }
        }
        else if("author".equals(searchCategory)) {
            if(StringUtils.hasLength(searchValue)) {
                return anmFrmEntity.nickName.contains(searchValue);
            }
        }
        else if("content".equals(searchCategory)) {
            if(StringUtils.hasLength(searchCategory)) {
                return anmFrmEntity.content.in(searchValue.getBytes(StandardCharsets.UTF_8));
            }
        }

        return null;
    }
}
