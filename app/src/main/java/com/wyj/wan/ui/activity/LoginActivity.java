package com.wyj.wan.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.wyj.wan.R;
import com.wyj.wan.base.BaseActivity;
import com.wyj.wan.di.component.ActivityComponent;
import com.wyj.wan.model.UserEntity;
import com.wyj.wan.presenter.LoginActivityPresenter;
import com.wyj.wan.utils.ClickBinder;
import com.wyj.wan.utils.SPUtils;

import butterknife.BindView;

import static com.wyj.wan.app.Constants.MY_SHARED_PREFERENCE;

public class LoginActivity extends BaseActivity<LoginActivityPresenter> {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.login_progress)
    ProgressBar mProgressView;
    @BindView(R.id.et_account)
    AutoCompleteTextView mEtAccount;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.til_password)
    TextInputLayout mTilPassword;
    @BindView(R.id.til_account)
    TextInputLayout mTilAccount;
    @BindView(R.id.btn_sign_in)
    Button mBtnSignIn;
    @BindView(R.id.login_form)
    ScrollView mLoginFormView;
    @BindView(R.id.checkbox)
    CheckBox mCheckBox;
    private boolean mIsRememberPW = false;
    private String account;
    private String password;

    @Override
    protected void setStatusBar() {
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        String userAccount = SPUtils.getInstance(MY_SHARED_PREFERENCE).getString("userAccount");
        String userPassword = SPUtils.getInstance(MY_SHARED_PREFERENCE).getString("userPassword");
        if (!TextUtils.isEmpty(userAccount)) {
            mEtAccount.setText(userAccount);
            mEtPassword.setText(userPassword);
        }
        mCheckBox.setChecked(true);
    }

    @Override
    protected void initListener() {
        mToolbar.setNavigationOnClickListener(v -> finish());

        ClickBinder.bind(mBtnSignIn, v -> attemptLogin());

        mEtPassword.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });
        mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mIsRememberPW = isChecked;
        });
    }

    @Override
    protected void initCompleted() {

    }

    private void attemptLogin() {
        // Reset errors.
        mTilAccount.setError(null);
        mTilPassword.setError(null);

        // Store values at the time of the login attempt.
        account = mEtAccount.getText().toString();
        password = mEtPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mTilPassword.setError(getString(R.string.error_invalid_password));
            focusView = mEtPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(account)) {
            mTilAccount.setError(getString(R.string.error_field_required));
            focusView = mEtAccount;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mPresenter.login(account, password);
        }
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void loginSuccess(UserEntity data) {
        if (mIsRememberPW) {
            SPUtils.getInstance(MY_SHARED_PREFERENCE).put("userAccount", account);
            SPUtils.getInstance(MY_SHARED_PREFERENCE).put("userPassword", password);
        }
        SPUtils.getInstance(MY_SHARED_PREFERENCE).put("login", true);
        finish();
    }
}

