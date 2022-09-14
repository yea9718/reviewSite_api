package com.project.gansul.util;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.List;

@AllArgsConstructor
@Component
public class ResponseUtil {
    public static ResponseEntity<BaseResponse> getResponse(
            final HttpStatus httpStatus,
            final String message,
            final List<FieldError> errors,
            final BaseResponseCode responseCode) {
        final BaseResponse baseResponse =
                BaseResponse.builder()
                        .httpStatus(httpStatus)
                        .errors(errors)
                        .message(message)
                        .responseCode(responseCode.CODE)
                        .responseString(responseCode.PHRASE)
                        .build();
        return new ResponseEntity<>(baseResponse, httpStatus);
    }

    public static ResponseEntity<BaseResponse> getResponse(
            final HttpStatus httpStatus, final String message, final List<FieldError> errors) {
        final BaseResponse baseResponse =
                BaseResponse.builder().httpStatus(httpStatus).errors(errors).message(message).build();
        return new ResponseEntity<>(baseResponse, httpStatus);
    }

    public static ResponseEntity<BaseResponse> getResponse(
            final HttpStatus httpStatus, final String message, final BaseResponseCode responseCode) {
        final BaseResponse baseResponse =
                BaseResponse.builder()
                        .httpStatus(httpStatus)
                        .responseCode(responseCode.CODE)
                        .responseString(responseCode.PHRASE)
                        .message(message)
                        .build();
        return new ResponseEntity<>(baseResponse, httpStatus);
    }
}
