package com.project.gansul.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.List;

@Data
@Builder
public class BaseResponse {
    @JsonIgnore
    private HttpStatus httpStatus;

    @JsonInclude(Include.NON_NULL)
    private String message;

    @JsonInclude(Include.NON_NULL)
    private int responseCode;

    @JsonInclude(Include.NON_NULL)
    private String responseString;

    @JsonInclude(Include.NON_NULL)
    private List<FieldError> errors;

    public int getHttpStatusCode() { return httpStatus.value(); }
}
