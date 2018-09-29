package com.liu.zhibao.angrypandacustomannotation;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by zhibao.Liu on 2018/9/29.
 *
 * @version :
 * @date : 2018/9/29
 * @des : 动态代理onClick方法,即将所有的onClick由动态代理统一处理
 * @see{@link}
 */

public class DynamicHandler implements InvocationHandler {
    private final HashMap<String, Method> methodMap = new HashMap<String, Method>(
            1);
    // 因为传进来的为activity，使用弱引用主要是为了防止内存泄漏
    private WeakReference<Object> handlerRef;

    public DynamicHandler(Object object) {
        this.handlerRef = new WeakReference<Object>(object);
    }

    public void addMethod(String name, Method method) {
        methodMap.put(name, method);
    }

    // 当回到OnClickListener的OnClick方法的时候，它会调用这里的invoke方法
    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        // 得到activity实例
        Object handler = handlerRef.get();
        if (handler != null) {
            // method对应的就是回调方法OnClick，得到方法名
            String methodName = method.getName();
            // 得到activtiy里面的clickBtnInvoked方法
            method = methodMap.get(methodName);
            if (method != null) {
                // 回调clickBtnInvoked方法
                return method.invoke(handler, objects);
            }
        }
        return null;
    }
}

