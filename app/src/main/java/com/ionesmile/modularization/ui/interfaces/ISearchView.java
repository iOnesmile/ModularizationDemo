package com.ionesmile.modularization.ui.interfaces;

/**
 * Created by iOnesmile on 2017/2/26.
 */
public interface ISearchView extends IBaseView {

    String getSearchKey();

    void onSearchSuccess(String result);

    void onSearchFailure(String message);
}
