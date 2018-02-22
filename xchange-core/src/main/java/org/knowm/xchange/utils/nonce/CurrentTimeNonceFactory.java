package org.knowm.xchange.utils.nonce;

import org.knowm.xchange.SynchronizedValueFactory;

public class CurrentTimeNonceFactory implements SynchronizedValueFactory<Long> {

  @Override
  public Long createValue() {

    return System.currentTimeMillis();
  }
}
