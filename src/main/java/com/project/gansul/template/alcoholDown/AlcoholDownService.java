package com.project.gansul.template.alcoholDown;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mysema.commons.lang.Assert;
import com.project.gansul.entity.AlcoholDown;
import com.project.gansul.entity.QAlcoholDown;
import com.project.gansul.exception.AppException;
import com.project.gansul.repository.AlcoholDownDto;
import com.project.gansul.repository.AlcoholDownRepository;
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
public class AlcoholDownService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AlcoholDownRepository alcoholDownRepository;
    private final ModelMapper modelMapper;
    private final ImmutableList<String> enums = ImmutableList.<String>builder().build();
    private final ImmutableList<String> tables = ImmutableList.<String>builder().build();

    public List<String> getEnumsAndTables() {
        final List<String> equals = Lists.newArrayList(enums);
        equals.addAll(tables);
        return equals;
    }

    /* 등록 */
    public AlcoholDown createAlcoholDown(final AlcoholDownDto.Request create) {
        Assert.notNull(create, "error.non.alcoholDown.dto");

        final AlcoholDown alcoholDown = modelMapper.map(create, AlcoholDown.class);
        alcoholDownRepository.save(alcoholDown);

        return alcoholDown;
    }

    /* 수정 */
    public AlcoholDown updateAlcoholDown (final AlcoholDownDto.Request update) {
        Assert.notNull(update, "error.no.alcoholDown.dto");
        this.validateAlcoholDownFields(update, Boolean.FALSE);

        if(update.getId() == null) {
            throw new AppException(BASE_ID_NOT_EXIST, "error.id.empty");
        }

        AlcoholDown alcoholDown = alcoholDownRepository.getById(update.getId());

        if(update.getUpId() != null) {
            alcoholDown.setUpId(update.getUpId());
        }
        if(update.getName() != null) {
            alcoholDown.setName(update.getName());
        }
        if(update.getPrice() != null) {
            alcoholDown.setPrice(update.getPrice());
        }
        if(update.getSfe() != null) {
            alcoholDown.setSfe(update.getSfe());
        }
        if(update.getAlcohol() != null) {
            alcoholDown.setAlcohol(update.getAlcohol());
        }
        if(update.getImage() != null) {
            alcoholDown.setImage(update.getImage());
        }
        if(update.getClickCnt() != null) {
            alcoholDown.setClickCnt(update.getClickCnt());
        }

        alcoholDownRepository.save(alcoholDown);
        return alcoholDown;
    }

    /* 삭제 */
    public void deleteAlcoholDown (final Long id) { alcoholDownRepository.deleteById(id); }

    /* 로우 1건 조회 */
    public AlcoholDown getAlcoholDown (final Long id) {
        final QAlcoholDown qAlcoholDown = QAlcoholDown.alcoholDown;
        BooleanBuilder expression = new BooleanBuilder();
        expression = expression.and(qAlcoholDown.id.eq(id));
        final Optional<AlcoholDown> alcoholDown = alcoholDownRepository.findOne(expression);
        if (!alcoholDown.isPresent()) {
            throw new AppException(BASE_ID_NOT_EXIST, "error.alcoholDown.id.no");
        }
        return alcoholDown.get();
    }

    /* 로우 여러건 조회 */
    public Page<AlcoholDown> findPagedAlcoholDowns (final BaseSearch.Search search, final Pageable pageable) {
        final QAlcoholDown qAlcoholDown = QAlcoholDown.alcoholDown;
        BooleanBuilder expression = new BooleanBuilder();

        if (search.getFilters() != null) {
            for (BaseSearch.Filter filter : search.getFilters()) {
                logger.debug("filter.name = [{}], filter.value = [{}], filter.ops = [{}]", filter.getName(),
                        filter.getValue(), filter.getOps());
                StringPath stringPath = Expressions.stringPath(filter.getName());
                if (!StringUtils.isEmpty(filter.getName()) && !StringUtils.isEmpty(filter.getValue())) {
                    Expression<?> value = null;
                    if ("alcoholDownId".equals(filter.getName())) {
                        stringPath = Expressions.stringPath("alcoholDownId");
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
        final Page<AlcoholDown> alcoholDowns = alcoholDownRepository.findAll(expression, pageable);
        return alcoholDowns;
    }

    private void validateAlcoholDownFields (final AlcoholDownDto.Request request, final Boolean isCreate) {
        final Long alcoholDownId = isCreate ? null : request.getId();
        if(!isCreate && request.getId() == null) {
            throw new AppException(BASE_ID_NOT_EXIST, "error.id.empty");
        }
    }
}
