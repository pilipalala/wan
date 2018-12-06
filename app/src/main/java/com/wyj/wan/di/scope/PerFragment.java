package com.wyj.wan.di.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * @author wangyujie
 * @date 2018/11/30.18:22
 * @describe 添加描述
 */
@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerFragment {
}
