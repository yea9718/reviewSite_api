package com.project.gansul.template.liked;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mysema.commons.lang.Assert;
import com.project.gansul.entity.Liked;
import com.project.gansul.entity.QLiked;
import com.project.gansul.exception.AppException;
import com.project.gansul.repository.LikedDto;
import com.project.gansul.repository.LikedRepository;
import com.project.gansul.util.BaseSearch;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

import static com.project.gansul.util.BaseResponseCode.BASE_ID_NOT_EXIST;

@RequiredArgsConstructor
@Transactional
@Service
public class LikedService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final LikedRepository likedRepository;
    private final ModelMapper modelMapper;
    private final ImmutableList<String> enums = ImmutableList.<String>builder().build();
    private final ImmutableList<String> tables = ImmutableList.<String>builder().build();

    public List<String> getEunmsAndTables() {
        final List<String> equals = Lists.newArrayList(enums);
        equals.addAll(tables);
        return equals;
    }

    /* 등록 */
    public Liked createLiked (final LikedDto.Request create) {
        Assert.notNull(create, "error.non.liked.dto");
        final Liked liked = modelMapper.map(create, Liked.class);
        likedRepository.save(liked);

        return liked;
    }

    /* 수정 */
    public Liked updateLiked (final LikedDto.Request update) {
        Assert.notNull(update, "error.no.evaluation.dto");
        this.validateLikedFields(update, Boolean.FALSE);

        Liked liked = likedRepository.getById(update.getId());
        if (update.getPostId() != null) {
            liked.setPostId(update.getPostId());
        }
        if (update.getLikeId() != null) {
            liked.setLikeId(update.getLikeId());
        }
        if (update.getLikeYn() != null) {
            liked.setLikeYn(update.getLikeYn());
        }

        likedRepository.save(liked);
        return liked;
    }

    /* 삭제 */
    public void deleteLiked (final Long id) { likedRepository.deleteById(id); }

    /* 로우 1건 조회 */
    public Liked getLiked (final Long id) {
        final QLiked qLiked = QLiked.liked;
        BooleanBuilder expression = new BooleanBuilder();
        expression = expression.and(qLiked.id.eq(id));
        final Optional<Liked> liked = likedRepository.findOne(expression);
        if (!liked.isPresent()) {
            throw new AppException(BASE_ID_NOT_EXIST, "error.liked.id.no");
        }
        return liked.get();
    }

    /* 로우 여러건 조회 */
    public Page<Liked> findPagedLikeds (BaseSearch.Search search, final Pageable pageable) {
        final QLiked qLiked = QLiked.liked;
        BooleanBuilder expression = new BooleanBuilder();

        if (search.getFilters() != null) {
            for (BaseSearch.Filter filter : search.getFilters()) {
                logger.debug("filter.name = [{}], filter.value = [{}], filter.ops = [{}]", filter.getName(),
                        filter.getValue(), filter.getOps());
                StringPath stringPath = Expressions.stringPath(filter.getName());
                if (!StringUtils.isEmpty(filter.getName()) && !StringUtils.isEmpty(filter.getValue())) {
                    Expression<?> value = null;
                    if ("likedId".equals(filter.getName())) {
                        stringPath = Expressions.stringPath("likedId");
                        value = Expressions.constant(Long.valueOf(filter.getValue() + ""));
                    } else if (filter.getName().contains("equal")) {
                        String searchKey = filter.getName().replace("equal", "");
                        stringPath = Expressions.stringPath(searchKey);
                        value = Expressions.constant(filter.getValue());
                    } else {
                        value = Expressions.constant(filter.getValue());
                    }

                    if (value != null) {
                        expression = expression.and(Expressions
                                .predicate(filter.getOps(), stringPath, value));
                    }
                }
            }
        }
        final Page<Liked> likeds = likedRepository.findAll(expression, pageable);
        return likeds;
    }

    private void validateLikedFields (final LikedDto.Request request, final Boolean isCreate) {
        final Long likedId = isCreate ? null : request.getId();
        if (!isCreate && request.getId() == null) {
            throw new AppException(BASE_ID_NOT_EXIST, "error.id.empty");
        }
    }
}
