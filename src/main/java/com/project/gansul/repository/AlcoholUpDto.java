package com.project.gansul.repository;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public class AlcoholUpDto {
    @Data
    @ApiModel("AlcoholUpDto.Request")
    public static class Request {

        @ApiModelProperty(value = "ID")
        private final Long id;

        @ApiModelProperty(value = "이름")
        private final String name;

        @ApiModelProperty(value = "클릭수")
        private final Long clickCnt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel("AlcoholUpDto.Response")
    public static class Response {
        private Long id;
        private String name;
        private Long clickCnt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonView(DataTablesOutput.View.class)
    @ApiModel("AlcoholUpDto.BriefResponse")
    public static class BriefResponse {
        private Long id;
        private String name;
        private Long clickCnt;
    }
}
