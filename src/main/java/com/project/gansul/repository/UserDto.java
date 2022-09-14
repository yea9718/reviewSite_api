package com.project.gansul.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.time.LocalDateTime;

public class UserDto {
    @Data
    @ApiModel("UserDto.Request")
    public static class Request {

        @ApiModelProperty(value = "ID")
        private final Long id;

        @ApiModelProperty(value = "회원명")
        private final String userName;

        @ApiModelProperty(value = "아이디")
        private final String userId;

        @ApiModelProperty(value = "패스워드")
        private final String password;

        @ApiModelProperty(value = "이메일")
        private final String email;

        @ApiModelProperty(value = "전화번호")
        private final String phone;

        @ApiModelProperty(value = "생년월일")
        private LocalDateTime birthday;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel("UserDto.Response")
    public static class Response {
        private Long id;
        private String userName;
        private String userId;
        private String password;
        private String email;
        private String phone;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime birthday;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime updatedAt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonView(DataTablesOutput.View.class)
    @ApiModel("UserDto.BriefResponse")
    public static class BriefResponse {
        private Long id;
        private String userName;
        private String userId;
        private String password;
        private String email;
        private String phone;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime birthday;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime updatedAt;
    }
}
