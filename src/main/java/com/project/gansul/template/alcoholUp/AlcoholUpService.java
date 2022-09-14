package com.project.gansul.template.alcoholUp;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mysema.commons.lang.Assert;
import com.project.gansul.entity.AlcoholUp;
import com.project.gansul.entity.QAlcoholUp;
import com.project.gansul.exception.AppException;
import com.project.gansul.repository.AlcoholUpDto;
import com.project.gansul.repository.AlcoholUpRepository;
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
public class AlcoholUpService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AlcoholUpRepository alcoholUpRepository;
    private final ModelMapper modelMapper;
    private final ImmutableList<String> enums = ImmutableList.<String>builder().build();
    private final ImmutableList<String> tables = ImmutableList.<String>builder().build();

    public List<String> getEunmsAndTables() {
        final List<String> equals = Lists.newArrayList(enums);
        equals.addAll(tables);
        return equals;
    }

    /* 등록 */
    public AlcoholUp createAlcoholUp (final AlcoholUpDto.Request create) {
        Assert.notNull(create, "error.non.alcoholUp.dto");
        final AlcoholUp alcoholUp = modelMapper.map(create, AlcoholUp.class);
        alcoholUpRepository.save(alcoholUp);

        return alcoholUp;
    }

    /* 수정 */
    public AlcoholUp updateAlcoholUp (final AlcoholUpDto.Request update) {
        Assert.notNull(update, "error.no.alcoholUp.dto");
        this.validateAlcoholUpFields(update, Boolean.FALSE);

        AlcoholUp alcoholUp = alcoholUpRepository.getById(update.getId());
        if (update.getName() != null) {
            alcoholUp.setName(update.getName());
        }
        if (update.getClickCnt() != null) {
            alcoholUp.setClickCnt(update.getClickCnt());
        }

        alcoholUpRepository.save(alcoholUp);
        return alcoholUp;
    }

    /* 삭제 */
    public void deleteAlcoholUp (final Long id) { alcoholUpRepository.deleteById(id); }

    /* 로우 1건 조회 */
    public AlcoholUp getAlcoholUp (final Long id) {
        final QAlcoholUp qAlcoholUp = QAlcoholUp.alcoholUp;
        BooleanBuilder expression = new BooleanBuilder();
        expression = expression.and(qAlcoholUp.id.eq(id));
        final Optional<AlcoholUp> alcoholUp = alcoholUpRepository.findOne(expression);
        if (!alcoholUp.isPresent()) {
            throw new AppException(BASE_ID_NOT_EXIST, "error.alcoholUp.id.no");
        }
        return alcoholUp.get();
    }

    /* 로우 여러건 조회 */
    public Page<AlcoholUp> findPagedAlcoholUps (BaseSearch.Search search, final Pageable pageable) {
        final QAlcoholUp qAlcoholUp = QAlcoholUp.alcoholUp;
        BooleanBuilder expression = new BooleanBuilder();

        if (search.getFilters() != null) {
            for (BaseSearch.Filter filter : search.getFilters()) {
                logger.debug("filter.name = [{}], filter.value = [{}], filter.ops = [{}]", filter.getName(),
                        filter.getValue(), filter.getOps());
                StringPath stringPath = Expressions.stringPath(filter.getName());
                if (!StringUtils.isEmpty(filter.getName()) && !StringUtils.isEmpty(filter.getValue())) {
                    Expression<?> value = null;
                    if ("alcoholUpId".equals(filter.getName())) {
                        stringPath = Expressions.stringPath("alcoholUpId");
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
        final Page<AlcoholUp> alcoholUps = alcoholUpRepository.findAll(expression, pageable);
        return alcoholUps;
    }

    private void validateAlcoholUpFields (final AlcoholUpDto.Request request, final Boolean isCreate) {
        final Long alcoholUp = isCreate ? null : request.getId();
        if (!isCreate && request.getId() == null) {
            throw new AppException(BASE_ID_NOT_EXIST, "error.id.empty");
        }
    }
}
