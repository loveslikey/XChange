package org.knowm.xchange

import feign.RequestInterceptor
import feign.RequestTemplate
import org.knowm.xchange.service.ParamsDigest

/**
 * Created by Administrator on 2018/2/19.
 */
public class DigestRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        //Map<String, Collection<String>> headMap= template.headers();
       /* headMap.
                template.queries();*/
        Map.Entry<String, Collection<String>> paramsDigestEntry= template.headers().find{
            it.value.size()==1&&it.value[0].startsWith("ParamsDigest:")
        }
        if(paramsDigestEntry){
            ParamsDigest paramsDigest=ParamsDigestFactory.get(paramsDigestEntry.value[0])
            if(paramsDigest){
                String digestBase64Binary = paramsDigest.digestParams(template)
                template.header(paramsDigestEntry.key,digestBase64Binary);
            }
        }

         paramsDigestEntry= template.queries().find{
            it.value.size()==1&&it.value[0].startsWith("ParamsDigest:")
        }
        if(paramsDigestEntry){
            ParamsDigest paramsDigest=ParamsDigestFactory.get(paramsDigestEntry.value[0])
            if(paramsDigest){
                String digestBase64Binary = paramsDigest.digestParams(template)
                template.query(paramsDigestEntry.key,digestBase64Binary);
            }
        }
    }
}