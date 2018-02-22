package org.knowm.xchange.taurus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.knowm.xchange.exceptions.HttpStatusExceptionSupport;

public class TaurusException extends HttpStatusExceptionSupport {
  public TaurusException(@JsonProperty("error") Object error) {
    super(error.toString());
  }
}
