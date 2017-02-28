package com.ionesmile.mvpdemo.data.manager;


import com.ionesmile.mvpdemo.data.network.RetrofitHelper;
import com.ionesmile.mvpdemo.data.network.RetrofitService;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by iOnesmile on 2017/2/27 0027.
 */

public class DataManager {

    private RetrofitService mRetrofitService;

    public DataManager(){
        this.mRetrofitService = RetrofitHelper.getInstance().getServer();
    }

    public Observable<ResponseBody> getSearchResult(String keyword){
        return mRetrofitService.searchKey(keyword);
    }
}
