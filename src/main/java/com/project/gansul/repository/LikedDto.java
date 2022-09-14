package com.project.gansul.repository;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public class LikedDto {
    @Data
    @ApiModel("LikedDto.Request")
    public static class Request {

        @ApiModelProperty(value = "ID")
        private final Long id;

        @ApiModelProperty(value = "게시글ID")
        private final Long postId;

        @ApiModelProperty(value = "클릭한 사람")
        private final String likeId;

        @ApiModelProperty(value = "클릭여부")
        private final String likeYn;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel("LikedDto.Response")
    public static class Response {
        private Long id;
        private Long postId;
        private String likeId;
        private String likeYn;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonView(DataTablesOutput.View.class)
    @ApiModel("LikedDto.BriefResponse")
    public static class BriefResponse {
        private Long id;
        private Long postId;
        private String likeId;
        private String likeYn;
    }
}
