package com.project.gansul.template.user;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mysema.commons.lang.Assert;
import com.project.gansul.entity.QUser;
import com.project.gansul.entity.User;
import com.project.gansul.exception.AppException;
import com.project.gansul.repository.UserDto;
import com.project.gansul.repository.UserRepository;
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
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ImmutableList<String> enums = ImmutableList.<String>builder().build();
    private final ImmutableList<String> tables = ImmutableList.<String>builder().build();

    public List<String> getEunmsAndTables() {
        final List<String> equals = Lists.newArrayList(enums);
        equals.addAll(tables);
        return equals;
    }

    /* 등록 */
    public User createUser (final UserDto.Request create) {
        Assert.notNull(create, "error.non.user.dto");
        final User user = modelMapper.map(create, User.class);
        userRepository.save(user);

        return user;
    }

    /* 수정 */
    public User updateUser (final UserDto.Request update) {
        Assert.notNull(update, "error.no.user.dto");
        this.validateUserFields(update, Boolean.FALSE);

        User user = userRepository.getById(update.getId());
        if (update.getId() != null) {
            user.setId(update.getId());
        }
        if (update.getUserName() != null) {
            user.setUserName(update.getUserName());
        }
        if (update.getUserId() != null) {
            user.setUserId(update.getUserId());
        }
        if (update.getPassword() != null) {
            user.setPassword(update.getPassword());
        }
        if (update.getEmail() != null) {
            user.setEmail(update.getEmail());
        }
        if (update.getPhone() != null) {
            user.setPhone(update.getPhone());
        }
        if (update.getBirthday() != null) {
            user.setBirthday(update.getBirthday());
        }

        userRepository.save(user);
        return user;
    }

    /* 삭제 */
    public void deleteUser (final Long id) { userRepository.deleteById(id); }

    /* 로우 1건 조회 */
    public User getUser (final Long id) {
        final QUser qUser = QUser.user;
        BooleanBuilder expression = new BooleanBuilder();
        expression = expression.and(qUser.id.eq(id));
        final Optional<User> user = userRepository.findOne(expression);
        if (!user.isPresent()) {
            throw new AppException(BASE_ID_NOT_EXIST, "error.user.id.no");
        }
        return user.get();
    }

    /* 로우 여러건 조회 */
    public Page<User> findPagedUsers (BaseSearch.Search search, final Pageable pageable) {
        final QUser qUser = QUser.user;
        BooleanBuilder expression = new BooleanBuilder();

        if (search.getFilters() != null) {
            for (BaseSearch.Filter filter : search.getFilters()) {
                logger.debug("filter.name = [{}], filter.value = [{}], filter.ops = [{}]", filter.getName(),
                        filter.getValue(), filter.getOps());
                StringPath stringPath = Expressions.stringPath(filter.getName());
                if (!StringUtils.isEmpty(filter.getName()) && !StringUtils.isEmpty(filter.getValue())) {
                    Expression<?> value = null;
                    if ("userId".equals(filter.getName())) {
                        stringPath = Expressions.stringPath("userId");
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
        final Page<User> users = userRepository.findAll(expression, pageable);
        return users;
    }

    private void validateUserFields (final UserDto.Request request, final Boolean isCreate) {
        final Long userId = isCreate ? null : request.getId();
        if (!isCreate && request.getId() == null) {
            throw new AppException(BASE_ID_NOT_EXIST, "error.id.empty");
        }
    }
}
