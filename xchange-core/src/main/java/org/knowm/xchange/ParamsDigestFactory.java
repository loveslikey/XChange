package org.knowm.xchange;

import org.knowm.xchange.service.ParamsDigest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/19.
 */
public  class ParamsDigestFactory {
    private  static Map<String,ParamsDigest> paramsDigestMap=new HashMap<>();

    public static Map<String,ParamsDigest> add(String key,ParamsDigest paramsDigest){
        paramsDigestMap.put(key,paramsDigest);
        return paramsDigestMap;
    }
    public static  ParamsDigest get(String key){
        return paramsDigestMap.get(key);
    }
}
