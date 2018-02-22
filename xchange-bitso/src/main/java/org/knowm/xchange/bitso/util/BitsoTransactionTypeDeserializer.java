package org.knowm.xchange.bitso.util;

import org.knowm.xchange.bitso.dto.trade.BitsoUserTransaction;
import org.knowm.xchange.utils.jackson.serializers.EnumIntDeserializer;


/**
 * @author Piotr Ładyżyński
 */
public class BitsoTransactionTypeDeserializer extends EnumIntDeserializer<BitsoUserTransaction.TransactionType> {

  /**
   * Constructor
   */
  public BitsoTransactionTypeDeserializer() {

    super(BitsoUserTransaction.TransactionType.class);
  }
}
