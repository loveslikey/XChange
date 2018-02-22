package org.knowm.xchange.taurus.dto;

import org.knowm.xchange.exceptions.ExceptionalReturnContentException;

public class TaurusBaseResponse {

  protected TaurusBaseResponse(Object error) {
    if (error != null) {
      throw new ExceptionalReturnContentException("Error returned: " + error);
    }
  }
}
