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

public class PostingDto {
    @Data
    @ApiModel("PostingDto.Request")
    public static class Request {

        @ApiModelProperty(value = "ID")
        private final Long id;

        @ApiModelProperty(value = "작성자")
        private final String createId;

        @ApiModelProperty(value = "제목")
        private final String subject;

        @ApiModelProperty(value = "내용")
        private final TextArea cn;

        @ApiModelProperty(value = "수정자")
        private final String updatedId;

        @ApiModelProperty(value = "술종류(대)")
        private final String kindUp;

        @ApiModelProperty(value = "술종류(소)")
        private final String kindDown;

        @ApiModelProperty(value = "별점")
        private final String horoscope;

        @ApiModelProperty(value = "조회수")
        private final Long viewCnt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel("PostingDto.Response")
    public static class Response {
        private Long id;
        private String createId;
        private String subject;
        private TextArea cn;
        private String updatedId;
        private String kindUp;
        private String kindDown;
        private String horoscope;
        private Long viewCnt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime updatedAt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonView(DataTablesOutput.View.class)
    @ApiModel("PostingDto.BriefResponse")
    public static class BriefResponse {
        private Long id;
        private String createId;
        private String subject;
        private TextArea cn;
        private String updatedId;
        private String kindUp;
        private String kindDown;
        private String horoscope;
        private Long viewCnt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime updatedAt;
    }
}
