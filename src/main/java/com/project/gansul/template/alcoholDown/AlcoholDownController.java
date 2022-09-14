package com.project.gansul.template.alcoholDown;

import com.fasterxml.jackson.annotation.JsonView;
import com.project.gansul.entity.AlcoholDown;
import com.project.gansul.repository.AlcoholDownDto;
import com.project.gansul.repository.AlcoholDownRepository;
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
@RequestMapping("/api/alcoholDown")
@Component
public class AlcoholDownController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final HttpServletRequest request;
    private final AlcoholDownService alcoholDownService;
    private final ModelMapper modelMapper;
    private final AlcoholDownRepository alcoholDownRepository;

    /* 등록 */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlcoholDownDto.Response createAlcoholDown (@RequestBody final AlcoholDownDto.Request create) {
        final AlcoholDown alcoholDown = alcoholDownService.createAlcoholDown(create);
        return this.buildResponse(alcoholDown);
    }

    /* 수정 */
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public AlcoholDownDto.Response updateAlcoholDown (@RequestBody @Valid final AlcoholDownDto.Request update) {
        final AlcoholDown alcoholDown = alcoholDownService.updateAlcoholDown(update);
        return this.buildResponse(alcoholDown);
    }

    /* 삭제 */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAlcoholDown (@PathVariable final Long id) { alcoholDownService.deleteAlcoholDown(id); }

    /* 1건 조회 */
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public AlcoholDownDto.Response findEvaluation (@PathVariable final Long id) {
        final AlcoholDown alcoholDown = alcoholDownService.getAlcoholDown(id);
        logger.debug("Log Trace response =====> {}", alcoholDown);

        return this.buildResponse(alcoholDown);
    }

    /* 리스트 조회 */
    @GetMapping
    @JsonView(DataTablesOutput.View.class)
    @ResponseStatus(HttpStatus.OK)
    public DataTablesOutput<AlcoholDownDto.BriefResponse> findAlcoholDown() {
        final List<String> equals = alcoholDownService.getEnumsAndTables();
        final BaseSearch.Search searchCondition = PageUtil.buildSearch(request.getQueryString(), equals,
                BaseSearch.Sort.builder().name("id").direction(
                        Sort.Direction.DESC).seq(0).build());
        final PageRequest pageRequest = PageUtil.pageRequest(searchCondition);
        final Page<AlcoholDown> pagedApplys = alcoholDownService.findPagedAlcoholDowns(searchCondition, pageRequest);
        return buildDataTable(pagedApplys, searchCondition.getPage());
    }

    private DataTablesOutput<AlcoholDownDto.BriefResponse> buildDataTable (final Page<AlcoholDown> pagedAlcoholDown, final int pageNo) {
        final List<AlcoholDown> alcoholDowns = pagedAlcoholDown.getContent();
        final long recordsTotal = pagedAlcoholDown.getTotalElements();
        final long recordsFilterd = pagedAlcoholDown.getNumberOfElements();
        final DataTablesOutput<AlcoholDownDto.BriefResponse> result = new DataTablesOutput<>();

        result.setData(alcoholDowns.stream().map(this::buildBriefResponse).collect(Collectors.toList()));
        result.setDraw(pageNo);
        result.setRecordsFiltered(recordsFilterd);
        result.setRecordsTotal(recordsTotal);

        return result;
    }

    private AlcoholDownDto.Response buildResponse (final AlcoholDown alcoholDown) {
        final AlcoholDownDto.Response baseDto = modelMapper.map(alcoholDown, AlcoholDownDto.Response.class);
        return baseDto;
    }

    private AlcoholDownDto.BriefResponse buildBriefResponse (final AlcoholDown alcoholDown) {
        final AlcoholDownDto.BriefResponse briefResponse = modelMapper.map(alcoholDown, AlcoholDownDto.BriefResponse.class);
        return briefResponse;
    }

    private AlcoholDownDto.BriefResponse buildApplyBriefResponse(AlcoholDownDto.BriefResponse response) {
        return response;
    }
}
