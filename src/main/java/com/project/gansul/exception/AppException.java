package com.project.gansul.exception;

import com.project.gansul.util.BaseResponseCode;

public class AppException extends RuntimeException {
    private static final long serialVersionUID = 6909871303979900328L;
    private BaseResponseCode responseCode;

    public AppException(BaseResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public AppException(BaseResponseCode responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
    }

    public AppException(BaseResponseCode responseCode, String message, Throwable cause) {
        super(message, cause);
        this.responseCode = responseCode;
    }

    public AppException(BaseResponseCode responseCode, Throwable cause) {
        super(null, cause);
        this.responseCode = responseCode;
    }

    public BaseResponseCode getResponseCode() {
        return responseCode;
    }
}
