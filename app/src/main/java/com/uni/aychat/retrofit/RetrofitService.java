package com.uni.aychat.retrofit;

import com.uni.aychat.dto.ReqLogin;
import com.uni.aychat.dto.ResLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;



public interface RetrofitService {
    @POST("/ay/login")
    Call<ResLogin> reqLogin(@Body ReqLogin body);

    @POST("/ay/login2")
    Call<ResLogin> reqLogin2(@Body ReqLogin body);
}
