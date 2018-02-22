package org.knowm.xchange.utils.proxy

import feign.InvocationHandlerFactory
import feign.ReflectiveFeign
import feign.Target

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
/**
 * Created by Administrator on 2018/2/20.
 */
public class XchangeInvocationHandlerFactory  implements InvocationHandlerFactory {

    private List<XchangeInterceptor> interceptorList=[];

    @Override
    public InvocationHandler create(Target target, Map<Method, InvocationHandlerFactory.MethodHandler> dispatch) {
        InvocationHandler  invocationHandler= new ReflectiveFeign.FeignInvocationHandler(target, dispatch);
        interceptorList.each {
            invocationHandler=new InterceptedInvocationHandler(it, invocationHandler,target,dispatch);
        }
        return invocationHandler;
    }
    public List<XchangeInterceptor>  addInterceptor(XchangeInterceptor interceptor){
        interceptorList.add(interceptor);
        return  interceptorList;
    }
}
