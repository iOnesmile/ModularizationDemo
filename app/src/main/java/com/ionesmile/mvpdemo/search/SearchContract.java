package com.ionesmile.mvpdemo.search;

import com.ionesmile.mvpdemo.BasePresenter;
import com.ionesmile.mvpdemo.BaseView;

/**
 * Created by iOnesmile on 2017/2/27 0027.
 */
public interface SearchContract {

    interface View extends BaseView<SearchContract.Presenter> {

        void onSearchSuccess(String result);

        void onSearchFailure(String message);
    }

    interface Presenter extends BasePresenter {

        void stop();

        void startSearch(String searchKey);
    }
}
