package org.knowm.xchange.exceptions;


import java.util.List;
import java.util.Map;

public interface HttpResponseAware {

    void setResponseHeaders(Map<String, List<String>> headers);

    Map<String, List<String>> getResponseHeaders();
}