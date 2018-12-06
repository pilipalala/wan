package com.wyj.wan.di.qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * @author wangyujie
 * @date 2018/11/27.11:31
 * @describe 对 Context 的注解
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface AppForContext {
}
