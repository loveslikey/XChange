package org.knowm.xchange.quoine.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

import org.knowm.xchange.service.ParamsDigest;
import feign.RequestTemplate;
import org.knowm.xchange.SynchronizedValueFactory;

public class QuoineSignatureDigest extends ParamsDigest {

  private final JWTCreator.Builder builder;
  private final String tokenID;
  private final byte[] userSecret;
  private final SynchronizedValueFactory<Long> nonceFactory;

  public QuoineSignatureDigest(String tokenID, String userSecret, SynchronizedValueFactory<Long> nonceFactory) {
    this.tokenID = tokenID;
    this.userSecret = userSecret.getBytes();
    this.nonceFactory = nonceFactory;

    this.builder = JWT.create();
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {

    String path = "/" + requestTemplate.method();

    if (requestTemplate.queries() != null && requestTemplate.queries().size() > 0)
      path +=requestTemplate.queryLine();

    return builder.withClaim("path", path)
        .withClaim("nonce", String.valueOf(nonceFactory.createValue()))
        .withClaim("token_id", tokenID)
        .sign(Algorithm.HMAC256(userSecret));
  }
}
