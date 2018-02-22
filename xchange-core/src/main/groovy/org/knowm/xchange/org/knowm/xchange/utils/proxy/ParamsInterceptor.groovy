package org.knowm.xchange.org.knowm.xchange.utils.proxy

import feign.InvocationHandlerFactory
import feign.Target
import org.knowm.xchange.utils.Params
import org.knowm.xchange.utils.proxy.ParamSerialize
import org.knowm.xchange.utils.proxy.RestInvocation
import org.knowm.xchange.utils.proxy.RestMethodMetadata
import org.knowm.xchange.utils.proxy.XchangeInterceptor

import javax.ws.rs.Path
import java.lang.annotation.Annotation
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
/**
 * Created by Administrator on 2018/2/20.
 */
class ParamsInterceptor implements XchangeInterceptor {
    private final Map<Method, RestMethodMetadata> methodMetadataCache = new HashMap<>();
    private  final Map<Class<? extends Annotation>, Params> defaultParamsMap=new HashMap<>();
    @Override
    Object aroundInvoke(InvocationHandler invocationHandler,  Target target, Map<Method, InvocationHandlerFactory.MethodHandler> dispatch,Object proxy, Method method, Object[] args) throws Throwable {
        Boolean hasParamSerialize=args.any{
            return (it instanceof  ParamSerialize)
        }
        if(hasParamSerialize){
            RestMethodMetadata methodMetadata = getMetadata(method,target);
            RestInvocation invocation = RestInvocation.create(methodMetadata, args, defaultParamsMap);
            Object[] newArgs=new Object[args.length]
            args.eachWithIndex{ def entry, int i ->
                if(entry instanceof  ParamSerialize){
                    ParamSerialize paramSerialize= (ParamSerialize)entry
                    String newParam=paramSerialize.toSerialize(invocation)
                    newArgs[i]=newParam
                }else{
                    newArgs[i]=entry
                }
            }
        }
        return invocationHandler.invoke(proxy, method, args);
    }

    private RestMethodMetadata getMetadata(Method method,  Target target) {
        RestMethodMetadata metadata = methodMetadataCache.get(method);
        if (metadata == null) {
            String intfacePath = method.getDeclaringClass().getAnnotation(Path.class).value();
            metadata = RestMethodMetadata.create(method, target.url(), intfacePath);
            methodMetadataCache.put(method, metadata);
        }
        return metadata;
    }
}
