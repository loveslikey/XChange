package org.knowm.xchange.gatecoin.dto;

import org.knowm.xchange.gatecoin.dto.marketdata.ResponseStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.knowm.xchange.exceptions.HttpStatusExceptionSupport;

public class GatecoinException extends HttpStatusExceptionSupport {
  protected final ResponseStatus responseStatus;

  public GatecoinException(@JsonProperty("responseStatus") ResponseStatus responseStatus) {
    super(responseStatus.getMessage());
    this.responseStatus = responseStatus;
  }

  public String getErrorCode() {
    return responseStatus.getErrorCode();
  }
}
