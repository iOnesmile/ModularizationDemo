package com.ionesmile.modularization.presenter;

import android.text.TextUtils;

import com.ionesmile.modularization.data.manager.DataManager;
import com.ionesmile.modularization.ui.interfaces.IBaseView;
import com.ionesmile.modularization.ui.interfaces.ISearchView;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by iOnesmile on 2017/2/26.
 */
public class SearchPresenter implements Presenter {

    private ISearchView searchView;
    private CompositeSubscription mCompositeSubscription;
    private DataManager dataManager;
    private String mResult;

    @Override
    public void onCreate() {
        mCompositeSubscription = new CompositeSubscription();
        dataManager = new DataManager();
    }

    @Override
    public void onStop() {
        if (mCompositeSubscription.hasSubscriptions()){
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void attachView(IBaseView baseView) {
        this.searchView = (ISearchView) baseView;
    }

    public void startSearch(){
        final String searchKey = searchView.getSearchKey();
        if (TextUtils.isEmpty(searchKey)) {
            searchView.showToast("搜索关键字不能为空！");
            return;
        }
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
