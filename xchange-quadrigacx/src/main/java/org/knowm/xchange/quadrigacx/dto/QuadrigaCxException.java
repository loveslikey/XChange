package org.knowm.xchange.quadrigacx.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.exceptions.HttpStatusExceptionSupport;

import java.util.Collection;
import java.util.Map;


public class QuadrigaCxException extends HttpStatusExceptionSupport {

  private Map<String, Collection<String>> errors;

  public QuadrigaCxException(@JsonProperty("error") Object error) {
    super(getMessage(error));

    if (error instanceof Map) {
      try {
        errors = (Map<String, Collection<String>>) error;
      } catch (Exception ignore) {
      }
    }
  }

  private static String getMessage(Object errors) {
    if (errors instanceof Map) {
      try {
        Map<String, Iterable> map = (Map<String, Iterable>) errors;
        final StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
          for (Object msg : map.get(key)) {
            if (sb.length() > 0) {
              sb.append(" -- ");
            }
            sb.append(msg);
          }
        }
        return sb.toString();
      } catch (Exception ignore) {
      }
    }
    return String.valueOf(errors);
  }

  public Map<String, Collection<String>> getErrors() {
    return errors;
  }

  public Collection<String> getErrors(String key) {
    return errors.get(key);
  }
}
