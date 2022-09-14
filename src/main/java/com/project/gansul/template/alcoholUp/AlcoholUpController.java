package com.project.gansul.template.alcoholUp;

import com.fasterxml.jackson.annotation.JsonView;
import com.project.gansul.entity.AlcoholUp;
import com.project.gansul.repository.AlcoholUpDto;
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
@RequestMapping("/api/alcoholUp")
@Component
public class AlcoholUpController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final HttpServletRequest request;
    private final AlcoholUpService alcoholUpService;
    private final ModelMapper modelMapper;

    /* 등록 */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlcoholUpDto.Response createAlcoholUp (@RequestBody final AlcoholUpDto.Request create) {
        final AlcoholUp alcoholUp = alcoholUpService.createAlcoholUp(create);
        return this.buildResponse(alcoholUp);
    }

    /* 수정 */
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public AlcoholUpDto.Response updateEvaluation (@RequestBody @Valid final AlcoholUpDto.Request update){
        final AlcoholUp alcoholUp = alcoholUpService.updateAlcoholUp(update);
        return this.buildResponse(alcoholUp);
    }

    /* 삭제 */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAlcoholUp (@PathVariable final Long id) { alcoholUpService.deleteAlcoholUp(id); }

    /* 한건 조회 */
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public AlcoholUpDto.Response findEvaluation (@PathVariable final Long id) {
        final AlcoholUp alcoholUp = alcoholUpService.getAlcoholUp(id);
        logger.debug("Log Trace response =====> {}", alcoholUp);

        return this.buildResponse(alcoholUp);
    }

    /* 리스트 조회 */
    @GetMapping
    @JsonView(DataTablesOutput.View.class)
    @ResponseStatus(HttpStatus.OK)
    public DataTablesOutput<AlcoholUpDto.BriefResponse> findEvaluation() {
        final List<String> equals = alcoholUpService.getEunmsAndTables();
        final BaseSearch.Search searchCondition = PageUtil.buildSearch(request.getQueryString(), equals,
                BaseSearch.Sort.builder().name("id").direction(
                        Sort.Direction.ASC).seq(0).build());
        final PageRequest pageRequest = PageUtil.pageRequest(searchCondition);
        final Page<AlcoholUp> pagedAlcoholUp = alcoholUpService.findPagedAlcoholUps(searchCondition, pageRequest);
        return buildDataTable(pagedAlcoholUp, searchCondition.getPage());
    }

    private DataTablesOutput<AlcoholUpDto.BriefResponse> buildDataTable (final Page<AlcoholUp> pagedAlcoholUp,
                                                                         final int pageNo) {
        final List<AlcoholUp> alcoholUps = pagedAlcoholUp.getContent();
        final long recordsTotal = pagedAlcoholUp.getTotalElements();
        final long recordsFilterd = pagedAlcoholUp.getNumberOfElements();
        final DataTablesOutput<AlcoholUpDto.BriefResponse> result = new DataTablesOutput<>();
        result.setData(alcoholUps.stream().map(this::buildBriefResponse).collect(Collectors.toList()));
        result.setDraw(pageNo);
        result.setRecordsFiltered(recordsFilterd);
        result.setRecordsTotal(recordsTotal);

        return result;
    }

    private AlcoholUpDto.Response buildResponse (final AlcoholUp alcoholUp) {
        final AlcoholUpDto.Response alcoholUpDto = modelMapper.map(alcoholUp, AlcoholUpDto.Response.class);

        return alcoholUpDto;
    }

    private AlcoholUpDto.BriefResponse buildBriefResponse (final AlcoholUp alcoholUp) {
        final AlcoholUpDto.BriefResponse briefResponse = modelMapper.map(alcoholUp, AlcoholUpDto.BriefResponse.class);

        return briefResponse;
    }
}
