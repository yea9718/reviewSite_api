package com.project.gansul.template.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.project.gansul.entity.User;
import com.project.gansul.repository.UserDto;
import com.project.gansul.util.BaseSearch;
import com.project.gansul.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/user")
@Component
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final HttpServletRequest request;
    private final UserService userService;
    private final ModelMapper modelMapper;

    /* 등록 */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto.Response createUser (@RequestBody final UserDto.Request create) {
        final User user = userService.createUser(create);
        return this.buildResponse(user);
    }

    /* 수정 */
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDto.Response updateUser (@RequestBody @Valid final UserDto.Request update) {
        final User user = userService.updateUser(update);
        return this.buildResponse(user);
    }

    /* 삭제 */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser (@PathVariable final Long id) { userService.deleteUser(id); }

    /* 한건 조회 */
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto.Response findUser (@PathVariable final Long id) {
        final User user = userService.getUser(id);
        logger.debug("Log Trace response =====> {}", user);

        return this.buildResponse(user);
    }

    /* 리스트 조회 */
    @GetMapping
    @JsonView(DataTablesOutput.View.class)
    @ResponseStatus(HttpStatus.OK)
    public DataTablesOutput<UserDto.BriefResponse> findUser() {
        final List<String> equals = userService.getEunmsAndTables();
        final BaseSearch.Search searchCondition = PageUtil.buildSearch(request.getQueryString(), equals,
                BaseSearch.Sort.builder().name("id").direction(
                        Sort.Direction.ASC).seq(0).build());
        final PageRequest pageRequest = PageUtil.pageRequest(searchCondition);
        final Page<User> pagedUser = userService.findPagedUsers(searchCondition, pageRequest);

        return buildDataTable(pagedUser, searchCondition.getPage());
    }

    private DataTablesOutput<UserDto.BriefResponse> buildDataTable (final Page<User> pagedUser,
                                                                    final int pageNo) {
        final List<User> users = pagedUser.getContent();
        final long recordsTotal = pagedUser.getTotalElements();
        final long recordsFilterd = pagedUser.getNumberOfElements();
        final DataTablesOutput<UserDto.BriefResponse> result = new DataTablesOutput<>();
        result.setData(users.stream().map(this::buildBriefResponse).collect(Collectors.toList()));
        result.setDraw(pageNo);
        result.setRecordsFiltered(recordsFilterd);
        result.setRecordsTotal(recordsTotal);

        return result;
    }

    private UserDto.Response buildResponse (final User user) {
        final UserDto.Response userDto = modelMapper.map(user, UserDto.Response.class);

        return userDto;
    }

    private UserDto.BriefResponse buildBriefResponse (final User user) {
        final UserDto.BriefResponse briefResponse = modelMapper.map(user, UserDto.BriefResponse.class);

        return briefResponse;
    }
}
