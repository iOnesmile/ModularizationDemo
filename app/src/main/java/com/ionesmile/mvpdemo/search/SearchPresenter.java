package com.ionesmile.mvpdemo.search;

import com.ionesmile.mvpdemo.data.manager.DataManager;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by iOnesmile on 2017/2/27 0027.
 */
public class SearchPresenter implements SearchContract.Presenter {

    private SearchContract.View searchView;
    private CompositeSubscription mCompositeSubscription;
    private DataManager dataManager;
    private String mResult;

    public SearchPresenter(SearchContract.View searchView, DataManager dataManager) {
        this.searchView = searchView;
        this.dataManager = dataManager;

        this.searchView.setPresenter(this);
    }

    @Override
    public void start() {
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void stop() {
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void startSearch(String searchKey) {
        mCompositeSubscription.add(dataManager.getSearchResult(searchKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        if (mResult != null) {
                            searchView.onSearchSuccess(mResult);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        searchView.onSearchFailure("请求失败！！！");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (responseBody != null) {
                            try {
                                mResult = responseBody.source().readUtf8();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
        );
    }
}
