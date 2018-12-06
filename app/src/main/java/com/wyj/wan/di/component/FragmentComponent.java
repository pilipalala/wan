package com.wyj.wan.di.component;

import com.wyj.wan.di.scope.PerFragment;
import com.wyj.wan.ui.fragment.CollectFragment;
import com.wyj.wan.ui.fragment.HomeFragment;

import dagger.Component;

/**
 * @author wangyujie
 * @date 2018/11/30.18:21
 * @describe 添加描述
 */
@PerFragment
@Component(dependencies = AppComponent.class)
public interface FragmentComponent {

    void inject(HomeFragment homeFragment);

    void inject(CollectFragment collectFragment);
}
