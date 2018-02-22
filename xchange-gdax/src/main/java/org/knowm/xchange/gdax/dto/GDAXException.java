package org.knowm.xchange.gdax.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.knowm.xchange.exceptions.HttpStatusExceptionSupport;

public class GDAXException extends HttpStatusExceptionSupport {

  public GDAXException(@JsonProperty("message") String reason) {
    super(reason);
  }
}
