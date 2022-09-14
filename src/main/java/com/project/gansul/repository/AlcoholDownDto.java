package com.project.gansul.repository;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public class AlcoholDownDto {
    @Data
    @ApiModel("AlcoholDownDto.Request")
    public static class Request {
        @ApiModelProperty(value = "ID")
        private final Long id;

        @ApiModelProperty(value = "대분류ID")
        private final Long upId;

        @ApiModelProperty(value = "이름")
        private final String name;

        @ApiModelProperty(value = "가격")
        private final Long price;

        @ApiModelProperty(value = "특징")
        private final String sfe;

        @ApiModelProperty(value = "도수")
        private final String alcohol;

        @ApiModelProperty(value = "이미지")
        private final String image;

        @ApiModelProperty(value = "클릭수")
        private final Long clickCnt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel("AlcoholDownDto.Response")
    public static class Response {
        private Long id;
        private Long upId;
        private String name;
        private Long price;
        private String sfe;
        private String alcohol;
        private String image;
        private Long clickCnt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonView(DataTablesOutput.View.class)
    @ApiModel("AlcoholDownDto.BriefResponse")
    public static class BriefResponse {
        private Long id;
        private Long upId;
        private String name;
        private Long price;
        private String sfe;
        private String alcohol;
        private String image;
        private Long clickCnt;
    }
}
