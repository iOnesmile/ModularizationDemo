package com.ionesmile.modularization.data.network;



import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by win764-1 on 2016/12/12.
 */

public interface RetrofitService {

    @GET("api/getchannellist")
    Observable<ResponseBody> getChannelList(@Query("packagename") String packageName,
                                            @Query("language") String language);
}