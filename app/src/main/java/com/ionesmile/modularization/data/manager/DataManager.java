package com.ionesmile.modularization.data.manager;

import com.ionesmile.modularization.data.network.RetrofitHelper;
import com.ionesmile.modularization.data.network.RetrofitService;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by win764-1 on 2016/12/12.
 */

public class DataManager {

    private RetrofitService mRetrofitService;

    public DataManager(){
        this.mRetrofitService = RetrofitHelper.getInstance().getServer();
    }

    public Observable<ResponseBody> getSearchResult(String keyword){
        return mRetrofitService.getChannelList(keyword, "zh");
    }
}
