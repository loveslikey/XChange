package org.knowm.xchange.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Administrator on 2018/2/18.
 */
public class HttpStatusExceptionSupport extends RuntimeException implements HttpStatusException {

    @JsonIgnore
    private int __httpStatusCode;

    public HttpStatusExceptionSupport() { }

    public HttpStatusExceptionSupport(String message) {
        super(message);
    }

    @JsonIgnore
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    @Override
    public String getMessage() {
        return String.format("%s (HTTP status code: %d)", super.getMessage(), __httpStatusCode);
    }

    public int getHttpStatusCode() {
        return __httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.__httpStatusCode = httpStatusCode;
    }
}