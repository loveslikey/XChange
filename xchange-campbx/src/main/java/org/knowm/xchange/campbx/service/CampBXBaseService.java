package org.knowm.xchange.campbx.service;

import feign.Feign;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.RestProxyFactory;
import org.knowm.xchange.campbx.CampBX;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;
import org.knowm.xchange.utils.CertHelper;

/**
 * @author timmolter
 */
public class CampBXBaseService extends BaseExchangeService implements BaseService {

  protected final CampBX campBX;

  /**
   * Constructor
   *
   * @param exchange
   */
  public CampBXBaseService(Exchange exchange) {

    super(exchange);

    okhttp3.OkHttpClient.Builder okHttpbuilder=getClientBuilder();
    okHttpbuilder.sslSocketFactory(CertHelper.createRestrictedSSLSocketFactory("TLSv1", "TLSv1.1"));
    Feign.Builder builder = getClientConfig(okHttpbuilder);
    // campbx server raises "internal error" if connected via these protocol versions
    //config.setSslSocketFactory();

    this.campBX = RestProxyFactory.createProxy(CampBX.class, exchange.getExchangeSpecification().getSslUri(), builder);
  }
}
