package com.project.gansul.template.reply;

import com.fasterxml.jackson.annotation.JsonView;
import com.project.gansul.entity.Reply;
import com.project.gansul.repository.ReplyDto;
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
@RequestMapping("/api/reply")
@Component
public class ReplyController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final HttpServletRequest request;
    private final ReplyService replyService;
    private final ModelMapper modelMapper;

    /* 등록 */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReplyDto.Response createReply (@RequestBody final ReplyDto.Request create) {
        final Reply reply = replyService.createReply(create);
        return this.buildResponse(reply);
    }

    /* 수정 */
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ReplyDto.Response updateReply (@RequestBody @Valid final ReplyDto.Request update){
        final Reply reply = replyService.updateReply(update);
        return this.buildResponse(reply);
    }

    /* 삭제 */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReply (@PathVariable final Long id) { replyService.deleteReply(id); }

    /* 한건 조회 */
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReplyDto.Response findReply (@PathVariable final Long id) {
        final Reply reply = replyService.getReply(id);
        logger.debug("Log Trace response =====> {}", reply);

        return this.buildResponse(reply);
    }

    /* 리스트 조회 */
    @GetMapping
    @JsonView(DataTablesOutput.View.class)
    @ResponseStatus(HttpStatus.OK)
    public DataTablesOutput<ReplyDto.BriefResponse> findReply() {
        final List<String> equals = replyService.getEunmsAndTables();
        final BaseSearch.Search searchCondition = PageUtil.buildSearch(request.getQueryString(), equals,
                BaseSearch.Sort.builder().name("id").direction(
                        Sort.Direction.ASC).seq(0).build());
        final PageRequest pageRequest = PageUtil.pageRequest(searchCondition);
        final Page<Reply> pagedReply = replyService.findPagedReplies(searchCondition, pageRequest);
        return buildDataTable(pagedReply, searchCondition.getPage());
    }

    private DataTablesOutput<ReplyDto.BriefResponse> buildDataTable (final Page<Reply> pagedReply,
                                                                     final int pageNo) {
        final List<Reply> replies = pagedReply.getContent();
        final long recordsTotal = pagedReply.getTotalElements();
        final long recordsFilterd = pagedReply.getNumberOfElements();
        final DataTablesOutput<ReplyDto.BriefResponse> result = new DataTablesOutput<>();
        result.setData(replies.stream().map(this::buildBriefResponse).collect(Collectors.toList()));
        result.setDraw(pageNo);
        result.setRecordsFiltered(recordsFilterd);
        result.setRecordsTotal(recordsTotal);

        return result;
    }

    private ReplyDto.Response buildResponse (final Reply reply) {
        final ReplyDto.Response replyDto = modelMapper.map(reply, ReplyDto.Response.class);

        return replyDto;
    }

    private ReplyDto.BriefResponse buildBriefResponse (final Reply reply) {
        final ReplyDto.BriefResponse briefResponse = modelMapper.map(reply, ReplyDto.BriefResponse.class);

        return briefResponse;
    }
}
