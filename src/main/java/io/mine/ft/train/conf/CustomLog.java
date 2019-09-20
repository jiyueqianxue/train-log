package io.mine.ft.train.conf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.METHOD, ElementType.CONSTRUCTOR })
public @interface CustomLog {
	// true:允许读取自定义日志参数
	// false:不允许
	boolean value() default true;
}
