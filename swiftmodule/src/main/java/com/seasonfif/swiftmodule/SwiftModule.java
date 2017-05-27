package com.seasonfif.swiftmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 创建时间：2017年05月26日17:24 <br>
 * 作者：zhangqiang <br>
 * 描述：
 */

public class SwiftModule {

  private final static Map<Method, ServiceMethod> serviceMethodCache = new LinkedHashMap<>();

  public static <T> T createRouterService(final Context context, Class<T> service){

    return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service },
        new InvocationHandler() {
      @Override public Object invoke(Object proxy, Method method, Object[] args)
          throws Throwable {
        if (method.getDeclaringClass() == Object.class){
          return method.invoke(this, args);
        }

        ServiceMethod intentMethod = loadServiceMethod(method, args);
        Intent intent = intentMethod.adapt(context, intentMethod, args);
        Class returnType = method.getReturnType();
        if (returnType == void.class){
          int requestCode = intentMethod.requestCode;
          if (requestCode > 0){
            ((Activity)context).startActivityForResult(intent, requestCode);
          }else{
            context.startActivity(intent);
          }
          return null;
        }
        return intent;
      }
    });

  }

  private static ServiceMethod loadServiceMethod(Method method, Object[] args){
    ServiceMethod result;
    synchronized (serviceMethodCache){
      result = serviceMethodCache.get(method);
      if (result == null){
        result = new ServiceMethod.Builder(method, args).build();
        serviceMethodCache.put(method, result);
      }
    }
    return result;
  }
}
