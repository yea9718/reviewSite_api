package com.project.gansul.template.posting;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;;
import com.mysema.commons.lang.Assert;
import com.project.gansul.entity.Posting;
import com.project.gansul.entity.QPosting;
import com.project.gansul.exception.AppException;
import com.project.gansul.repository.PostingDto;
import com.project.gansul.repository.PostingRepository;
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
public class PostingService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private PostingRepository postingRepository;
    private final ModelMapper modelMapper;
    private final ImmutableList<String> enums = ImmutableList.<String>builder().build();
    private final ImmutableList<String> tables = ImmutableList.<String>builder().build();

    public List<String> getEunmsAndTables() {
        final List<String> equals = Lists.newArrayList(enums);
        equals.addAll(tables);
        return equals;
    }

    /* 등록 */
    public Posting createPosting (final PostingDto.Request create) {
        Assert.notNull(create, "error.non.evaluation.dto");
        final Posting posting = modelMapper.map(create, Posting.class);
        postingRepository.save(posting);

        return posting;
    }

    /* 수정 */
    public Posting updatePosting (final PostingDto.Request update) {
        Assert.notNull(update, "error.no.evaluation.dto");
        this.validatePostingFields(update, Boolean.FALSE);

        Posting posting = postingRepository.getById(update.getId());
        if (update.getCreateId() != null) {
            posting.setCreateId(update.getCreateId());
        }
        if (update.getSubject() != null) {
            posting.setSubject(update.getSubject());
        }
        if (update.getCn() != null) {
            posting.setCn(update.getCn());
        }
        if (update.getUpdatedId() != null) {
            posting.setUpdatedId(update.getUpdatedId());
        }
        if (update.getKindUp() != null) {
            posting.setKindUp(update.getKindUp());
        }
        if (update.getKindDown() != null) {
            posting.setKindDown(update.getKindDown());
        }
        if (update.getHoroscope() != null) {
            posting.setHoroscope(update.getHoroscope());
        }
        if (update.getViewCnt() != null) {
            posting.setViewCnt(update.getViewCnt());
        }

        postingRepository.save(posting);
        return posting;
    }

    /* 삭제 */
    public void deletePosting (final Long id) { postingRepository.deleteById(id); }

    /* 로우 1건 조회 */
    public Posting getPosting (final Long id) {
        final QPosting qPosting = QPosting.posting;
        BooleanBuilder expression = new BooleanBuilder();
        expression = expression.and(qPosting.id.eq(id));
        final Optional<Posting> posting = postingRepository.findOne(expression);
        if (!posting.isPresent()) {
            throw new AppException(BASE_ID_NOT_EXIST, "error.posting.id.no");
        }
        return posting.get();
    }

    /* 로우 여러건 조회 */
    public Page<Posting> findPagedPostings (BaseSearch.Search search, final Pageable pageable) {
        final QPosting qPosting = QPosting.posting;
        BooleanBuilder expression = new BooleanBuilder();

        if (search.getFilters() != null) {
            for (BaseSearch.Filter filter : search.getFilters()) {
                logger.debug("filter.name = [{}], filter.value = [{}], filter.ops = [{}]", filter.getName(),
                        filter.getValue(), filter.getOps());
                StringPath stringPath = Expressions.stringPath(filter.getName());
                if (!StringUtils.isEmpty(filter.getName()) && !StringUtils.isEmpty(filter.getValue())) {
                    Expression<?> value = null;
                    if ("postingId".equals(filter.getName())) {
                        stringPath = Expressions.stringPath("postingId");
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
        final Page<Posting> postings = postingRepository.findAll(expression, pageable);
        return postings;
    }

    private void validatePostingFields (final PostingDto.Request request, final Boolean isCreate) {
        final Long postingId = isCreate ? null : request.getId();
        if (!isCreate && request.getId() == null) {
            throw new AppException(BASE_ID_NOT_EXIST, "error.id.empty");
        }
    }
}
