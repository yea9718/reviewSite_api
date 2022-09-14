package com.project.gansul.template.posting;

import com.fasterxml.jackson.annotation.JsonView;
import com.project.gansul.entity.Posting;
import com.project.gansul.repository.PostingDto;
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
@RequestMapping("/api/posting")
@Component
public class PostingController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final HttpServletRequest request;
    private final PostingService postingService;
    private final ModelMapper modelMapper;

    /* 등록 */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostingDto.Response createPosting (@RequestBody final PostingDto.Request create) {
        final Posting posting = postingService.createPosting(create);
        return this.buildResponse(posting);
    }

    /* 수정 */
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public PostingDto.Response updatePosting (@RequestBody @Valid final PostingDto.Request update){
        final Posting posting = postingService.updatePosting(update);
        return this.buildResponse(posting);
    }

    /* 삭제 */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePosting (@PathVariable final Long id) { postingService.deletePosting(id); }

    /* 한건 조회 */
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostingDto.Response findPosting (@PathVariable final Long id) {
        final Posting posting = postingService.getPosting(id);
        logger.debug("Log Trace response =====> {}", posting);

        return this.buildResponse(posting);
    }

    /* 리스트 조회 */
    @GetMapping
    @JsonView(DataTablesOutput.View.class)
    @ResponseStatus(HttpStatus.OK)
    public DataTablesOutput<PostingDto.BriefResponse> findPosting() {
        final List<String> equals = postingService.getEunmsAndTables();
        final BaseSearch.Search searchCondition = PageUtil.buildSearch(request.getQueryString(), equals,
                BaseSearch.Sort.builder().name("id").direction(
                        Sort.Direction.ASC).seq(0).build());
        final PageRequest pageRequest = PageUtil.pageRequest(searchCondition);
        final Page<Posting> pagedPosting = postingService.findPagedPostings(searchCondition, pageRequest);
        return buildDataTable(pagedPosting, searchCondition.getPage());
    }

    private DataTablesOutput<PostingDto.BriefResponse> buildDataTable (final Page<Posting> pagedPosting,
                                                                       final int pageNo) {
        final List<Posting> postings = pagedPosting.getContent();
        final long recordsTotal = pagedPosting.getTotalElements();
        final long recordsFilterd = pagedPosting.getNumberOfElements();
        final DataTablesOutput<PostingDto.BriefResponse> result = new DataTablesOutput<>();
        result.setData(postings.stream().map(this::buildBriefResponse).collect(Collectors.toList()));
        result.setDraw(pageNo);
        result.setRecordsFiltered(recordsFilterd);
        result.setRecordsTotal(recordsTotal);

        return result;
    }

    private PostingDto.Response buildResponse (final Posting posting) {
        final PostingDto.Response postingDto = modelMapper.map(posting, PostingDto.Response.class);

        return postingDto;
    }

    private PostingDto.BriefResponse buildBriefResponse (final Posting posting) {
        final PostingDto.BriefResponse briefResponse = modelMapper.map(posting, PostingDto.BriefResponse.class);

        return briefResponse;
    }
}
