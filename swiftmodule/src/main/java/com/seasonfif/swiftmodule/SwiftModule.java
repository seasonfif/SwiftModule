package com.seasonfif.swiftmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.lang.reflect.Field;
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

  private final static Map<Method, IntentMethod> sIntentMethodCache = new LinkedHashMap<>();

  public static <T> T createRouterService(final Context context, Class<T> service){

    Field[] declaredFields = service.getDeclaredFields();
    Field[] fields = service.getFields();

    return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service },
        new InvocationHandler() {
      @Override public Object invoke(Object proxy, Method method, Object[] args)
          throws Throwable {
        if (method.getDeclaringClass() == Object.class){
          return method.invoke(this, args);
        }

        IntentMethod intentMethod = loadIntentMethod(method);
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

  private static IntentMethod loadIntentMethod(Method method){
    IntentMethod result;
    synchronized (sIntentMethodCache){
      result = sIntentMethodCache.get(method);
      if (result == null){
        result = new IntentMethod.Builder(method).build();
        sIntentMethodCache.put(method, result);
      }
    }
    return result;
  }
}
