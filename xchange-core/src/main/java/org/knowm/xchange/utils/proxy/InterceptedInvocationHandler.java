/**
 * Copyright (C) 2013 Matija Mazi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.knowm.xchange.utils.proxy;

import feign.InvocationHandlerFactory;
import feign.Target;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Matija Mazi
 */
class InterceptedInvocationHandler implements InvocationHandler {

    private final XchangeInterceptor xchangeInterceptor;
    private final InvocationHandler invocationHandler;
    private final Target target;
    private final Map<Method, InvocationHandlerFactory.MethodHandler> dispatch;

    public InterceptedInvocationHandler(XchangeInterceptor xchangeInterceptor, InvocationHandler invocationHandler,Target target,Map<Method, InvocationHandlerFactory.MethodHandler> dispatch) {
        this.xchangeInterceptor = xchangeInterceptor;
        this.invocationHandler = invocationHandler;
        this.target=target;
        this.dispatch=dispatch;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return xchangeInterceptor.aroundInvoke(invocationHandler,target,dispatch, proxy, method, args);
    }
}
