package org.knowm.xchange.poloniex.dto.account;

public class PoloniexBalanceTest {
/*
  @Test(expected = PoloniexException.class)
  public void balanceRejectTest() throws Exception {

    InvocationResult invocationResult = new InvocationResult("{\"error\":\"Invalid API key\\/secret pair.\"}", 200);

    Method apiMethod = PoloniexAuthenticated.class.getDeclaredMethod("returnCompleteBalances", String.class, ParamsDigest.class,
        SynchronizedValueFactory.class, String.class);
    RestMethodMetadata balances = RestMethodMetadata.create(apiMethod, "", "");

    try {
      new JacksonResponseReader(new DefaultJacksonObjectMapperFactory().createObjectMapper(), false).read(invocationResult, balances);
    } catch (PoloniexException e) {
      Assert.assertTrue(e.getMessage().startsWith("Invalid API key/secret pair."));
      throw e;
    }
  }*/
}
