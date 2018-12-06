package com.wyj.wan.utils;

import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.wyj.wan.app.App;

/**
 * Created by wangyujie
 * Date 2017/8/2
 * Time 21:41
 * TODO Toast工具类
 */


public class ToastUtil {
    private static Toast toast;

    public static Toast show(String text) {
        if (toast == null) {
            toast = Toast.makeText(App.getContext(), text, Toast.LENGTH_SHORT);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setText(text);
        toast.show();
        return toast;
    }

    public static Toast show(int text) {
        String string = null;
        try {
            string = App.getContext().getResources().getString(text);
        } catch (Exception e) {

        }
        if (TextUtils.isEmpty(string)) {
            return show(String.valueOf(Double.valueOf(text)));
        } else {
            return show(string);
        }
    }
}
