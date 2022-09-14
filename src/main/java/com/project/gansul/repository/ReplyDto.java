package com.project.gansul.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.awt.*;
import java.time.LocalDateTime;

public class ReplyDto {
    @Data
    @ApiModel("ReplyDto.Request")
    public static class Request {

        @ApiModelProperty(value = "ID")
        private final Long id;

        @ApiModelProperty(value = "게시글ID")
        private final Long postId;

        @ApiModelProperty(value = "작성자")
        private final String createId;

        @ApiModelProperty(value = "내용")
        private final TextArea cn;

        @ApiModelProperty(value = "수정자")
        private final String updatedId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel("ReplyDto.Response")
    public static class Response {
        private Long id;
        private Long postId;
        private String createId;
        private TextArea cn;
        private String updatedId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime updatedAt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonView(DataTablesOutput.View.class)
    @ApiModel("ReplyDto.BriefResponse")
    public static class BriefResponse {
        private Long id;
        private Long postId;
        private String createId;
        private TextArea cn;
        private String updatedId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime updatedAt;
    }
}
