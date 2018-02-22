package org.knowm.xchange.utils.nonce;

import org.knowm.xchange.SynchronizedValueFactory;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicLongCurrentTimeIncrementalNonceFactory implements SynchronizedValueFactory<Long> {

  private final AtomicLong incremental = new AtomicLong(System.currentTimeMillis());

  @Override
  public Long createValue() {

    return incremental.incrementAndGet();
  }
}
