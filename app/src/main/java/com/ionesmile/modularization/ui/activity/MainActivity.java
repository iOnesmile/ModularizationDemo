package com.ionesmile.modularization.ui.activity;

import android.view.View;

import com.ionesmile.modularization.R;
import com.ionesmile.modularization.databinding.MainActivityBinding;
import com.ionesmile.modularization.presenter.SearchPresenter;
import com.ionesmile.modularization.ui.interfaces.ISearchView;

public class MainActivity extends BaseActivity<MainActivityBinding> implements ISearchView {

    private SearchPresenter searchPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initBase() {
        searchPresenter = new SearchPresenter();
        searchPresenter.onCreate();
        searchPresenter.attachView(this);
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {
        rootBinding.setKeyword("com.chipsguide.demo.cloudmusic");
    }

    @Override
    protected void initListener() {
        rootBinding.setClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchPresenter.onStop();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_search:
                searchPresenter.startSearch();
                break;
        }
    }

    @Override
    public String getSearchKey() {
        return rootBinding.getKeyword();
    }

    @Override
    public void onSearchSuccess(String result) {
        rootBinding.setResult(result);
    }

    @Override
    public void onSearchFailure(String message) {
        rootBinding.setResult(message);
    }
}
