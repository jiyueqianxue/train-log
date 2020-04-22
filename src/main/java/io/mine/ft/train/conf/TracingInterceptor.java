package io.mine.ft.train.conf;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class TracingInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation i) throws Throwable {
		System.out
				.println("method " + i.getMethod() + " is called on " + i.getThis() + " with args " + i.getArguments());
		Object ret = i.proceed();
		System.out.println("method " + i.getMethod() + " returns " + ret);
		return ret;
	}
}
