package io.mine.ft.train.conf;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
/**
 * https://blog.csdn.net/u013905744/article/details/91364736
 * @author machao
 *
 */
@Configurable
public class InterceptorAnnotationConfig {
	
	 public static final String traceExecution = "execution(* com.hfi.aop..*.*(..))";
	
	// 方法1：使用aspectj execution（切点） + interceptor（增强Advice）构成织入（DefaultPointcutAdvisor）
	@Bean
	public DefaultPointcutAdvisor defaultPointcutAdvisor2() {
		TracingInterceptor interceptor = new TracingInterceptor();
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(traceExecution);

		// 配置增强类advisor
		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
		advisor.setPointcut(pointcut);
		advisor.setAdvice(interceptor);
		return advisor;
	}

	// 使用自定义注解（切点）+interceptor（增强Advice）构成织入（DefaultPointcutAdvisor）
	@Bean
	public DefaultPointcutAdvisor defaultPointcutAdvisor3() {
		LogInterceptor interceptor = new LogInterceptor();

		AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(CustomLog.class, true);
		//JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
		//pointcut.setPatterns("com.hfi.*");

		// 配置增强类advisor
		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
		advisor.setPointcut(pointcut);
		advisor.setAdvice(interceptor);
		return advisor;
	}

}
