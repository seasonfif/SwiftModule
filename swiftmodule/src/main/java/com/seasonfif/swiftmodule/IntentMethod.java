package com.seasonfif.swiftmodule;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.SparseArray;

import com.seasonfif.swiftmodule.annotation.FieldMap;
import com.seasonfif.swiftmodule.annotation.Flags;
import com.seasonfif.swiftmodule.annotation.Key;
import com.seasonfif.swiftmodule.annotation.RequestCode;
import com.seasonfif.swiftmodule.annotation.Scheme;
import com.seasonfif.swiftmodule.annotation.TargetClass;
import com.seasonfif.swiftmodule.annotation.TargetName;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.net.URI;
import java.util.ArrayList;

/**
 * 创建时间：2017年05月26日17:52 <br>
 * 作者：zhangqiang <br>
 * 描述：
 */
public class IntentMethod {

  String className;
  String scheme;
  int requestCode;
  int flags;
  Annotation[][] parameterAnnotationsArray;
  Type[] parameterTypes;

  public IntentMethod(Builder builder) {
    this.className = builder.className;
    this.scheme = builder.scheme;
    this.requestCode = builder.requestCode;
    this.flags = builder.flags;
    this.parameterAnnotationsArray = builder.parameterAnnotationsArray;
    this.parameterTypes = builder.parameterTypes;
  }

  /**
   * Intent缓存更新参数值
   *
   * @param ctx
   * @param serviceMethod
   * @param args
   * @return
   */
  public Intent adapt(Context ctx, IntentMethod serviceMethod, Object[] args) {
    return serviceMethod.toIntent(ctx, args);
  }

  private Intent toIntent(Context ctx, Object[] args) {
    Intent it = new Intent();
    if (!TextUtils.isEmpty(className)){
      it.setClassName(ctx, className);
    } else {
      it.setAction(Intent.ACTION_VIEW);
      it.setData(Uri.parse(scheme));
    }
    it.addFlags(flags);
    if (args != null && args.length > 0){
      it.putExtras(prepareBundle(args));
    }
    return it;
  }

  private Bundle prepareBundle(Object[] args) {
    Bundle bundle = new Bundle();
    int paramCount = parameterTypes.length;
    if (parameterTypes != null && paramCount > 0){
      for (int i = 0; i < paramCount; i++) {
        String key;
        Annotation[] paramAnnotations = parameterAnnotationsArray[i];
        for (Annotation anno: paramAnnotations) {
          if (anno instanceof Key){
            key = ((Key) anno).value();
            handleParams(bundle, key, parameterTypes[i], args[i]);
          }else if(anno instanceof FieldMap){

          }else{
            throw new IllegalStateException("不支持的参数注解: " + anno.toString() );
          }
        }
      }
    }
    return bundle;
  }

  static final class Builder{

    final Method method;
    final Annotation[] methodAnnotations;
    final Annotation[][] parameterAnnotationsArray;
    final Type[] parameterTypes;

    String scheme;
    String className;
    int requestCode;
    int flags;


    public Builder(Method method){
      this.method = method;
      this.methodAnnotations = method.getAnnotations();
      this.parameterTypes = method.getGenericParameterTypes();
      this.parameterAnnotationsArray = method.getParameterAnnotations();
    }

    public IntentMethod build(){
      for (Annotation annotation : methodAnnotations) {
        parseMethodAnnotation(annotation);
      }
      if (TextUtils.isEmpty(scheme) && TextUtils.isEmpty(className)){
        throw new IllegalStateException("未指定跳转对象，通过@Scheme或@TargetClass或@TargetName指定");
      }

      return new IntentMethod(this);
    }

    private void parseMethodAnnotation(Annotation annotation) {
      if (annotation instanceof Scheme){

        scheme = ((Scheme) annotation).value();
      }else if (annotation instanceof TargetName){

        className = ((TargetName) annotation).value();
      }else if(annotation instanceof TargetClass){

        className = ((TargetClass) annotation).value().getName();
      }else if(annotation instanceof RequestCode){

        requestCode = ((RequestCode) annotation).value();
      }else if(annotation instanceof Flags){

        flags |= ((Flags) annotation).value();
      }else{

        throw new IllegalStateException("不支持的方法注解: " + annotation.toString() );
      }
    }
  }

  private void handleParams(Bundle bundleExtra, String key, Type type, Object arg) {
    Class<?> rawParameterType = getRawType(type);
    if (rawParameterType == String.class)
    {
      bundleExtra.putString(key, arg.toString());
    }
    else if (rawParameterType == String[].class)
    {
      bundleExtra.putStringArray(key, (String[])arg);
    }
    else if (rawParameterType == int.class || rawParameterType == Integer.class)
    {
      bundleExtra.putInt(key, Integer.parseInt(arg.toString()));
    }
    else if (rawParameterType == int[].class || rawParameterType == Integer[].class)
    {
      bundleExtra.putIntArray(key, (int[])arg);
    }
    else if (rawParameterType == short.class || rawParameterType == Short.class)
    {
      bundleExtra.putShort(key, Short.parseShort(arg.toString()));
    }
    else if (rawParameterType == short[].class || rawParameterType == Short[].class)
    {
      bundleExtra.putShortArray(key, (short[])arg);
    }
    else if (rawParameterType == long.class || rawParameterType == Long.class)
    {
      bundleExtra.putLong(key, Long.parseLong(arg.toString()));
    }
    else if (rawParameterType == long[].class || rawParameterType == Long[].class)
    {
      bundleExtra.putLongArray(key, (long[])arg);
    }
    else if (rawParameterType == char.class)
    {
      bundleExtra.putChar(key, arg.toString().toCharArray()[0]);
    }
    else if (rawParameterType == char[].class)
    {
      bundleExtra.putCharArray(key, arg.toString().toCharArray());
    }
    else if (rawParameterType == double.class || rawParameterType == Double.class)
    {
      bundleExtra.putDouble(key, Double.parseDouble(arg.toString()));
    }
    else if (rawParameterType == double[].class || rawParameterType == Double[].class)
    {
      bundleExtra.putDoubleArray(key, (double[])arg);
    }
    else if (rawParameterType == float.class || rawParameterType == Float.class)
    {
      bundleExtra.putFloat(key, Float.parseFloat(arg.toString()));
    }
    else if (rawParameterType == float[].class || rawParameterType == Float[].class)
    {
      bundleExtra.putFloatArray(key, (float[])arg);
    }
    else if (rawParameterType == byte.class || rawParameterType == Byte.class)
    {
      bundleExtra.putByte(key, Byte.parseByte(arg.toString()));
    }
    else if (rawParameterType == byte[].class || rawParameterType == Byte[].class)
    {
      bundleExtra.putByteArray(key, (byte[])arg);
    }
    else if (rawParameterType == boolean.class || rawParameterType == Boolean.class)
    {
      bundleExtra.putBoolean(key, Boolean.parseBoolean(arg.toString()));
    }
    else if (rawParameterType == boolean[].class || rawParameterType == Boolean[].class)
    {
      bundleExtra.putBooleanArray(key, (boolean[]) arg);
    }
    else if (rawParameterType == Bundle.class)
    {
      if (TextUtils.isEmpty(key))
      {
        bundleExtra.putAll((Bundle) arg);
      }
      else
      {
        bundleExtra.putBundle(key, (Bundle) arg);
      }
    }
    //        else if (rawParameterType == PersistableBundle.class)
    //        {
    //            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) // 5.0 & 5.0+
    //            {
    //                bundleExtra.putAll((PersistableBundle)arg);
    //            }
    //            else
    //            {
    //                throw new RuntimeException("Call requires API level 21 (current min is 14): android.os.BaseBundle#putAll");
    //            }
    //        }
    //        else if (rawParameterType == Size.class)
    //        {
    //            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) // 5.0 & 5.0+
    //            {
    //                bundleExtra.putSize(key, (Size)arg);
    //            }
    //            else
    //            {
    //                throw new RuntimeException("Call requires API level 21 (current min is 14): android.os.Bundle#putSize");
    //            }
    //        }
    //        else if (rawParameterType == SizeF.class)
    //        {
    //            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) // 5.0 & 5.0+
    //            {
    //                bundleExtra.putSizeF(key, (SizeF)arg);
    //            }
    //            else
    //            {
    //                throw new RuntimeException("Call requires API level 21 (current min is 14): android.os.Bundle#putSizeF");
    //            }
    //        }
    else if (rawParameterType == SparseArray.class)
    {
      if (type instanceof ParameterizedType)
      {
        ParameterizedType parameterizedType = (ParameterizedType)type;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Type actualTypeArgument = actualTypeArguments[0];

        if (actualTypeArgument instanceof Class)
        {
          Class<?>[] interfaces = ((Class)actualTypeArgument).getInterfaces();
          for(Class<?> interfaceClass : interfaces)
          {
            if (interfaceClass == Parcelable.class)
            {
              bundleExtra.putSparseParcelableArray(key, (SparseArray<Parcelable>) arg);
              return;
            }
          }
          throw new RuntimeException("SparseArray的泛型必须实现Parcelable接口");
        }
      }
      else
      {
        throw new RuntimeException("SparseArray的泛型必须实现Parcelable接口");
      }
    }
    else if (rawParameterType == ArrayList.class)
    {
      if (type instanceof ParameterizedType)
      {
        ParameterizedType parameterizedType = (ParameterizedType)type;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments(); // 泛型类型数组
        if (actualTypeArguments == null || actualTypeArguments.length != 1)
        {
          throw new RuntimeException("ArrayList的泛型必须实现Parcelable接口");
        }

        Type actualTypeArgument = actualTypeArguments[0]; // 获取第一个泛型类型
        if (actualTypeArgument == String.class)
        {
          bundleExtra.putStringArrayList(key, (ArrayList<String>) arg);
        }
        else if (actualTypeArgument == Integer.class)
        {
          bundleExtra.putIntegerArrayList(key, (ArrayList<Integer>) arg);
        }
        else if (actualTypeArgument == CharSequence.class)
        {
          bundleExtra.putCharSequenceArrayList(key, (ArrayList<CharSequence>) arg);
        }
        else if (actualTypeArgument instanceof Class)
        {
          Class<?>[] interfaces = ((Class)actualTypeArgument).getInterfaces();
          for(Class<?> interfaceClass : interfaces)
          {
            if (interfaceClass == Parcelable.class)
            {
              bundleExtra.putParcelableArrayList(key, (ArrayList<Parcelable>) arg);
              return;
            }
          }
          throw new RuntimeException("ArrayList的泛型必须实现Parcelable接口");
        }
      }
      else
      {
        throw new RuntimeException("ArrayList的泛型必须实现Parcelable接口");
      }
    }
    else
    {
      if (rawParameterType.isArray()) // Parcelable[]
      {
        Class<?>[] interfaces = rawParameterType.getComponentType().getInterfaces();
        for(Class<?> interfaceClass : interfaces)
        {
          if (interfaceClass == Parcelable.class)
          {
            bundleExtra.putParcelableArray(key, (Parcelable[])arg);
            return;
          }
        }
        throw new RuntimeException("Object[]数组中的对象必须全部实现了Parcelable接口");
      }
      else // 其他接口
      {
        Class<?>[] interfaces = rawParameterType.getInterfaces();
        for(Class<?> interfaceClass : interfaces)
        {
          if (interfaceClass == Serializable.class)
          {
            bundleExtra.putSerializable(key, (Serializable)arg);
          }
          else if (interfaceClass == Parcelable.class)
          {
            bundleExtra.putParcelable(key, (Parcelable)arg);
          }
          //                else if (interfaceClass == IBinder.class)
          //                {
          //                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) // 4.3+
          //                    {
          //                        bundleExtra.putBinder(key, (IBinder)arg);
          //                    }
          //                    else
          //                    {
          //                        throw new RuntimeException("Call requires API level 18 (current min is 14): android.os.Bundle#putBinder");
          //                    }
          //                }
          else
          {
            throw new RuntimeException("Bundle不支持的类型, 参数: " + key);
          }
        }
      }

    }
  }

  private Class<?> getRawType(Type type) {
    if (type == null) throw new NullPointerException("type == null");

    if (type instanceof Class<?>) {
      // Type is a normal class.
      return (Class<?>) type;
    }
    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;

      // I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
      // suspects some pathological case related to nested classes exists.
      Type rawType = parameterizedType.getRawType();
      if (!(rawType instanceof Class)) throw new IllegalArgumentException();
      return (Class<?>) rawType;
    }
    if (type instanceof GenericArrayType) {
      Type componentType = ((GenericArrayType) type).getGenericComponentType();
      return Array.newInstance(getRawType(componentType), 0).getClass();
    }
    if (type instanceof TypeVariable) {
      // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
      // type that's more general than necessary is okay.
      return Object.class;
    }
    if (type instanceof WildcardType) {
      return getRawType(((WildcardType) type).getUpperBounds()[0]);
    }

    throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
            + "GenericArrayType, but <" + type + "> is of type " + type.getClass().getName());
  }
}
