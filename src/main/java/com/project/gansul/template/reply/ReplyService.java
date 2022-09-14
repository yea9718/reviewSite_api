package com.project.gansul.template.reply;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mysema.commons.lang.Assert;
import com.project.gansul.entity.QReply;
import com.project.gansul.entity.Reply;
import com.project.gansul.exception.AppException;
import com.project.gansul.repository.ReplyDto;
import com.project.gansul.repository.ReplyRepository;
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
public class ReplyService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ReplyRepository replyRepository;
    private final ModelMapper modelMapper;
    private final ImmutableList<String> enums = ImmutableList.<String>builder().build();
    private final ImmutableList<String> tables = ImmutableList.<String>builder().build();

    public List<String> getEunmsAndTables() {
        final List<String> equals = Lists.newArrayList(enums);
        equals.addAll(tables);
        return equals;
    }

    /* 등록 */
    public Reply createReply (final ReplyDto.Request create) {
        Assert.notNull(create, "error.non.evaluation.dto");
        final Reply reply = modelMapper.map(create, Reply.class);
        replyRepository.save(reply);

        return reply;
    }

    /* 수정 */
    public Reply updateReply (final ReplyDto.Request update) {
        Assert.notNull(update, "error.no.evaluation.dto");
        this.validateReplyFields(update, Boolean.FALSE);

        Reply reply = replyRepository.getById(update.getId());
        if (update.getPostId() != null) {
            reply.setPostId(update.getPostId());
        }
        if (update.getCreateId() != null) {
            reply.setCreatedId(update.getCreateId());
        }
        if (update.getUpdatedId() != null) {
            reply.setUpdatedId(update.getUpdatedId());
        }
        if (update.getCn() != null) {
            reply.setCn(update.getCn());
        }

        replyRepository.save(reply);
        return reply;
    }

    /* 삭제 */
    public void deleteReply (final Long id) { replyRepository.deleteById(id); }

    /* 로우 1건 조회 */
    public Reply getReply (final Long id) {
        final QReply qReply = QReply.reply;
        BooleanBuilder expression = new BooleanBuilder();
        expression = expression.and(qReply.id.eq(id));
        final Optional<Reply> reply = replyRepository.findOne(expression);
        if (!reply.isPresent()) {
            throw new AppException(BASE_ID_NOT_EXIST, "error.reply.id.no");
        }
        return reply.get();
    }

    /* 로우 여러건 조회 */
    public Page<Reply> findPagedReplies (BaseSearch.Search search, final Pageable pageable) {
        final QReply qReply = QReply.reply;
        BooleanBuilder expression = new BooleanBuilder();

        if (search.getFilters() != null) {
            for (BaseSearch.Filter filter : search.getFilters()) {
                logger.debug("filter.name = [{}], filter.value = [{}], filter.ops = [{}]", filter.getName(),
                        filter.getValue(), filter.getOps());
                StringPath stringPath = Expressions.stringPath(filter.getName());
                if (!StringUtils.isEmpty(filter.getName()) && !StringUtils.isEmpty(filter.getValue())) {
                    Expression<?> value = null;
                    if ("replyId".equals(filter.getName())) {
                        stringPath = Expressions.stringPath("replyId");
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
        final Page<Reply> replies = replyRepository.findAll(expression, pageable);
        return replies;
    }

    private void validateReplyFields (final ReplyDto.Request request, final Boolean isCreate) {
        final Long replyId = isCreate ? null : request.getId();
        if (!isCreate && request.getId() == null) {
            throw new AppException(BASE_ID_NOT_EXIST, "error.id.empty");
        }
    }
}
