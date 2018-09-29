package com.liu.zhibao.angrypandacustomannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhibao.Liu on 2018/9/29.
 *
 * @version :
 * @date : 2018/9/29
 * @des :
 * @see{@link}
 */

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBase {
    Class listenerType();
    String listenerSetter();
    String methodName();
}
