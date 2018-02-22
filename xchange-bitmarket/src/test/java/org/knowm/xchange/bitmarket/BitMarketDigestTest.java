package org.knowm.xchange.bitmarket;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)

@PowerMockIgnore("javax.crypto.*")
public class BitMarketDigestTest {

  private BitMarketDigest bitMarketDigest;

  @Before
  public void setUp() throws Exception {
    bitMarketDigest = BitMarketDigest.createInstance("secretKey");
  }
/*
  @Test
  public void shouldEncodeRestInvocation() throws Exception {
    // given
    RestInvocation invocation = mock(RestInvocation.class);
    PowerMockito.when(invocation, "getRequestBody").thenReturn("rest body");

    String expected = "6372f349eea659b26f5e01bc76e1485de744a1894d5e036b98eca724a8104719ea8767518286863d1becd0a1313ad5e7e507749f7cdb98a4dee92fec055643c4";

    // when
    String encoded = bitMarketDigest.digestParams(invocation);

    // then
    assertThat(encoded).isEqualTo(expected);
  }

  @Test(expected = RuntimeException.class)
  public void shouldFailOnEncodeError() throws Exception {
    // given
    RestInvocation invocation = mock(RestInvocation.class);
    PowerMockito.when(invocation, "getRequestBody").thenThrow(UnsupportedEncodingException.class);

    // when
    bitMarketDigest.digestParams(invocation);

    // then
    fail("BitMarketDigest should throw RuntimeException when encoding caused an error");
  }*/
}
