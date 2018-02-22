package org.knowm.xchange.gatecoin.dto;

import org.knowm.xchange.gatecoin.dto.marketdata.ResponseStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.knowm.xchange.exceptions.ExceptionalReturnContentException;

public class GatecoinResult {
  protected final ResponseStatus responseStatus;

  public GatecoinResult(@JsonProperty("responseStatus") ResponseStatus responseStatus) {
    if (responseStatus.getErrorCode() != null || (responseStatus.getMessage() != null && !responseStatus.getMessage().equals("OK"))) {
      throw new ExceptionalReturnContentException(responseStatus.toString());
    }
    this.responseStatus = responseStatus;
  }

  public ResponseStatus getResponseStatus() {
    return responseStatus;
  }
}
