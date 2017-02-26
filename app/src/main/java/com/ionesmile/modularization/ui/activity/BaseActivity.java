package com.ionesmile.modularization.ui.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by iOnesmile on 2017/2/26.
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity implements View.OnClickListener {

    protected T rootBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootBinding = DataBindingUtil.setContentView(this, getLayoutId());
        initBase();
        initUI();
        initData();
        initListener();
    }

    protected abstract int getLayoutId();

    protected abstract void initBase();

    protected abstract void initUI();

    protected abstract void initData();

    protected abstract void initListener();

    @Override
    public void onClick(View view) {

    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
