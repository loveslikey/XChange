package org.knowm.xchange.utils.nonce;

import org.knowm.xchange.SynchronizedValueFactory;

public class CurrentNanosecondTimeIncrementalNonceFactory
    implements SynchronizedValueFactory<Long> {

  private long lastNonce = 0L;

  @Override
  public Long createValue() {

    long newNonce = System.currentTimeMillis() * 1000;

    while (newNonce <= lastNonce) {
      newNonce++;
    }

    lastNonce = newNonce;

    return newNonce;
  }
}
