package com.uni.aychat.retrofit;

import com.uni.aychat.dto.ReqLogin;
import com.uni.aychat.dto.ReqLogout;
import com.uni.aychat.dto.ResLogin;
import com.uni.aychat.dto.ResLogout;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;



public interface RetrofitService {
    @POST("/ay/login")
    Call<ResLogin> reqLogin(@Body ReqLogin body);

    @POST("/ay/login2")
    Call<ResLogin> reqLogin2(@Body ReqLogin body);

    @POST("/ay/logout")
    Call<ResLogout> reqLogout(@Body ReqLogout body);
}
