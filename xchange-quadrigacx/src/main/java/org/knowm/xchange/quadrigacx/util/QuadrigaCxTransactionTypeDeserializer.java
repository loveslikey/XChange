package org.knowm.xchange.quadrigacx.util;

import org.knowm.xchange.quadrigacx.dto.trade.QuadrigaCxUserTransaction;
import org.knowm.xchange.utils.jackson.serializers.EnumIntDeserializer;


public class QuadrigaCxTransactionTypeDeserializer extends EnumIntDeserializer<QuadrigaCxUserTransaction.TransactionType> {

  /**
   * Constructor
   */
  public QuadrigaCxTransactionTypeDeserializer() {

    super(QuadrigaCxUserTransaction.TransactionType.class);
  }
}
