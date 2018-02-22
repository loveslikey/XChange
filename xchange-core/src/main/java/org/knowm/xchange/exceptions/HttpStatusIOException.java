package org.knowm.xchange.exceptions;


import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HttpStatusIOException extends IOException implements HttpStatusException,HttpResponseAware {


    private Map<String, List<String>> headers;

    public HttpStatusIOException(String message) {
        super(message);
    }

    public int getHttpStatusCode() {
        return 0;
    }

    public void setHttpStatusCode(int httpStatus) {
        throw new UnsupportedOperationException("Status code should be provided in constructor.");
    }

    public String getHttpBody() {
        return null;
    }

    @Override
    public void setResponseHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    @Override
    public Map<String, List<String>> getResponseHeaders() {
        return headers;
    }
}