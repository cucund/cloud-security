package com.cucund.work.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

import com.cucund.work.security.annotation.bean.ConParameterEnum;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ConParameter {
	
	ConParameterEnum value();
	
	String authLogin() default "";
	
	String description() default "";

}
