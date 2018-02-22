package org.knowm.xchange.zaif.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.exceptions.HttpStatusExceptionSupport;


public class ZaifException extends HttpStatusExceptionSupport {
  public ZaifException(@JsonProperty("error") String reason) {
    super(reason);
  }
}
