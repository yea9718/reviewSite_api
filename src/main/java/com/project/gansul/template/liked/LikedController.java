package com.project.gansul.template.liked;

import com.fasterxml.jackson.annotation.JsonView;
import com.project.gansul.entity.Liked;
import com.project.gansul.repository.LikedDto;
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
@RequestMapping("/api/liked")
@Component
public class LikedController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final HttpServletRequest request;
    private final LikedService likedService;
    private final ModelMapper modelMapper;

    /* 등록 */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LikedDto.Response createLiked (@RequestBody final LikedDto.Request create) {
        final Liked liked = likedService.createLiked(create);
        return this.buildResponse(liked);
    }

    /* 수정 */
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public LikedDto.Response updateLiked (@RequestBody @Valid final LikedDto.Request update){
        final Liked liked = likedService.updateLiked(update);
        return this.buildResponse(liked);
    }

    /* 삭제 */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLiked (@PathVariable final Long id) { likedService.deleteLiked(id); }

    /* 한건 조회 */
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public LikedDto.Response findLiked (@PathVariable final Long id) {
        final Liked liked = likedService.getLiked(id);
        logger.debug("Log Trace response =====> {}", liked);

        return this.buildResponse(liked);
    }

    /* 리스트 조회 */
    @GetMapping
    @JsonView(DataTablesOutput.View.class)
    @ResponseStatus(HttpStatus.OK)
    public DataTablesOutput<LikedDto.BriefResponse> findLiked() {
        final List<String> equals = likedService.getEunmsAndTables();
        final BaseSearch.Search searchCondition = PageUtil.buildSearch(request.getQueryString(), equals,
                BaseSearch.Sort.builder().name("id").direction(
                        Sort.Direction.ASC).seq(0).build());
        final PageRequest pageRequest = PageUtil.pageRequest(searchCondition);
        final Page<Liked> pagedLiked = likedService.findPagedLikeds(searchCondition, pageRequest);
        return buildDataTable(pagedLiked, searchCondition.getPage());
    }

    private DataTablesOutput<LikedDto.BriefResponse> buildDataTable (final Page<Liked> pagedLiked,
                                                                          final int pageNo) {
        final List<Liked> Likeds = pagedLiked.getContent();
        final long recordsTotal = pagedLiked.getTotalElements();
        final long recordsFilterd = pagedLiked.getNumberOfElements();
        final DataTablesOutput<LikedDto.BriefResponse> result = new DataTablesOutput<>();
        result.setData(Likeds.stream().map(this::buildBriefResponse).collect(Collectors.toList()));
        result.setDraw(pageNo);
        result.setRecordsFiltered(recordsFilterd);
        result.setRecordsTotal(recordsTotal);

        return result;
    }

    private LikedDto.Response buildResponse (final Liked liked) {
        final LikedDto.Response likedDto = modelMapper.map(liked, LikedDto.Response.class);

        return likedDto;
    }

    private LikedDto.BriefResponse buildBriefResponse (final Liked liked) {
        final LikedDto.BriefResponse briefResponse = modelMapper.map(liked, LikedDto.BriefResponse.class);

        return briefResponse;
    }
}
