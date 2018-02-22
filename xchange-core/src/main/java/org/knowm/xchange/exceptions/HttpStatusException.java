package org.knowm.xchange.exceptions;

/**
 * Created by Administrator on 2018/2/18.
 */
public interface HttpStatusException {
    int getHttpStatusCode();

    void setHttpStatusCode(int httpStatus);
}