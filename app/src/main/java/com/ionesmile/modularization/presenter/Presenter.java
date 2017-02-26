package com.ionesmile.modularization.presenter;


import com.ionesmile.modularization.ui.interfaces.IBaseView;


/**
 * Created by win764-1 on 2016/12/12.
 */

public interface Presenter {

    void onCreate();

    void onStop();

    void attachView(IBaseView baseView);
}
