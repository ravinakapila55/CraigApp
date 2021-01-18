package com.helper.Helper2Go.ApiUtils;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface TinderAppInterface
{

    //http://178.128.116.149:4201/api/chat/message
    @POST("login")
    Observable<Response<ResponseBody>> loginApi(@Body JsonObject jsonObject);

    @POST("admin-message")
    Observable<Response<ResponseBody>> sendFeedback(@Body JsonObject jsonObject);

    @POST("register")
    Observable<Response<ResponseBody>> registerApi(@Body JsonObject jsonObject);

    @POST("forgot-password")
    Observable<Response<ResponseBody>> forgotPasswordApi(@Body JsonObject jsonObject);

    @GET("notifications")
    Observable<Response<ResponseBody>> getNotifications(@Header("Authorization") String user_access_token);

    @GET("delete-notification")
    Observable<Response<ResponseBody>> deleteNotification(@Query("id") String id, @Header("Authorization") String authHeader);

    @POST("profile")
    Observable<Response<ResponseBody>> getProfile(@Header("Authorization") String user_access_token);

    @POST("get-jobs")
    Observable<Response<ResponseBody>> getJobs(@Body JsonObject jsonObject, @Header("Authorization") String user_access_token);

    @GET("search-jobs")
    Observable<Response<ResponseBody>> getAllJobs(@Header("Authorization") String user_access_token);

    @GET("search-jobs")
    Observable<Response<ResponseBody>> getSearchJobs(@Query("search_query") String search_key, @Header("Authorization") String user_access_token);

    @GET("my-applied-jobs")
    Observable<Response<ResponseBody>> getAppliedJobs(@Header("Authorization") String user_access_token);

    @GET("my-jobs")
    Observable<Response<ResponseBody>> getMyJobs(@Header("Authorization") String user_access_token);
    //http://178.128.116.149:4201/api/chat/message?conversationId=f89eafaf-2884-4fcb-b6f6-7473d6461420&page=1

    @GET("completed-jobs")
    Observable<Response<ResponseBody>> getCompletedJobs(@Header("Authorization") String user_access_token);

    @POST("user-all-jobs")
    Observable<Response<ResponseBody>> getUserAllJobs(@Header("Authorization") String user_access_token);

    @GET("get-skills")
    Observable<Response<ResponseBody>> getSkills(@Header("Authorization") String user_access_token);

    @Multipart
    @POST("update-profile")
    Observable<Response<ResponseBody>> updateProfileWithImage(@Part("name") RequestBody nameRequest,
            @Part("gender") RequestBody genderRequest,
            @Part("address") RequestBody addressRequest,
            @Part("info") RequestBody introRequest,
            @Part MultipartBody.Part image,
            @Header("Authorization") String user_access_token);

    @Multipart
    @POST("update-profile")
    Observable<Response<ResponseBody>> updateProfileWithoutImage(
            @Part("name") RequestBody nameRequest,
            @Part("gender") RequestBody genderRequest,
            @Part("address") RequestBody addressRequest,
            @Part("info") RequestBody introRequest,
            @Header("Authorization") String user_access_token);


    @POST("apply-job")
    Observable<Response<ResponseBody>> applyForJob(@Body JsonObject jsonObject, @Header("Authorization") String user_access_token);

    @POST("chat/message")
    Observable<Response<ResponseBody>> saveChatMessage(@Body JsonObject jsonObject, @Header("Authorization") String user_access_token);

    @POST("send-message-notification")
    Observable<Response<ResponseBody>> sendChatNotification(@Body JsonObject jsonObject, @Header("Authorization") String user_access_token);

    @POST("admin-message")
    Observable<Response<ResponseBody>> sendFeedback(@Body JsonObject jsonObject, @Header("Authorization") String user_access_token);

    @POST("logout")
    Observable<Response<ResponseBody>> logoutApi(@Header("Authorization") String user_access_token);

    @POST("change-pass")
    Observable<Response<ResponseBody>> changePasswordApi(@Body JsonObject jsonObject, @Header("Authorization") String user_access_token);

    @GET("chat/message")
    Observable<Response<ResponseBody>> getConversation(@Query("conversationId") String conversationId, @Query("page") String page);

    @GET("chat/conversation/id")
    Observable<Response<ResponseBody>> getConversationId(@Query("sender") String sender, @Query("receiver")
            String receiver, @Query("sender_job_id") String sender_job_id, @Query("receiver_job_id") String receiver_job_id);

    @GET("get-skills")
    Observable<Response<ResponseBody>> getSkillsParams(@Header("Authorization") String user_access_token);

    @POST("delete-job")
    Observable<Response<ResponseBody>> deleteJob(@Body JsonObject jsonObject,@Header("Authorization") String user_access_token);

    @Multipart
    @POST("create-job")
    Observable<Response<ResponseBody>> createJobApiWithImage(
            @Part("name") RequestBody name,
            @Part("short_desc") RequestBody short_desc,
            @Part("long_desc") RequestBody long_desc,
            @Part("applicants_required") RequestBody applicants_required,
            @Part MultipartBody.Part image,
            @Part("duration") RequestBody duration,
            @Part("location") RequestBody location,
            @Part("category") RequestBody category,
            @Query("skills_required[]") List<String> skills_required,
            @Query("tools_needed[]") List<String> tools_needed,
            @Part("cost") RequestBody price,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("start_date") RequestBody start_date,
            @Part("end_date") RequestBody end_date,
            @Part("max_price ") RequestBody maxPrice,
            @Header("Authorization") String user_access_token);

            @Multipart
            @POST("create-job")
            Observable<Response<ResponseBody>> createJobApiWithoutImage(
            @Part("name") RequestBody name,
            @Part("short_desc") RequestBody short_desc,
            @Part("long_desc") RequestBody long_desc,
            @Part("applicants_required") RequestBody applicants_required,
            @Part("duration") RequestBody duration,
            @Part("location") RequestBody location,
            @Part("category") RequestBody category,
            @Query("skills_required[]") List<String> skills_required,
            @Query("tools_needed[]") List<String> tools_needed,
            @Part("cost") RequestBody price,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("start_date") RequestBody start_date,
            @Part("end_date") RequestBody end_date,
            @Part("max_price") RequestBody maxPrice,
            @Header("Authorization") String user_access_token);

            @Multipart
            @POST("edit-job")
            Observable<Response<ResponseBody>> updateJobApi(
            @Part("name") RequestBody name,
            @Part("short_desc") RequestBody short_desc,
            @Part("long_desc") RequestBody long_desc,
            @Part("applicants_required") RequestBody applicants_required,
            @Part MultipartBody.Part image,
            @Part("duration") RequestBody duration,
            @Part("location") RequestBody location,
            @Part("category") RequestBody category,
            @Query("skills_required[]") List<String> skills_required,
            @Query("tools_needed[]") List<String> tools_needed,
            @Part("cost") RequestBody price,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("8") RequestBody job_id,
            @Part("start_date") RequestBody start_date,
            @Part("end_date") RequestBody end_date,
            @Part("max_price") RequestBody maxPrice,
            @Header("Authorization") String user_access_token);

    @Multipart
    @POST("edit-job")
    Observable<Response<ResponseBody>> updateJobWithoutImageApi(
            @Part("name") RequestBody name,
            @Part("short_desc") RequestBody short_desc,
            @Part("long_desc") RequestBody long_desc,
            @Part("applicants_required") RequestBody applicants_required,
            @Part("duration") RequestBody duration,
            @Part("location") RequestBody location,
            @Part("category") RequestBody category,
            @Query("skills_required[]") List<String> skills_required,
            @Query("tools_needed[]") List<String> tools_needed,
            @Part("cost") RequestBody price,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("job_id") RequestBody job_id,
            @Part("start_date") RequestBody start_date,
            @Part("end_date") RequestBody end_date,
            @Part("max_price") RequestBody maxPrice,
            @Header("Authorization") String user_access_token);

    @POST("accept-job")
    Observable<Response<ResponseBody>> acceptJobOffer(@Body JsonObject jsonObject,@Header("Authorization") String user_access_token);

    @POST("reject-job")
    Observable<Response<ResponseBody>> rejectJobOffer(@Body JsonObject jsonObject,@Header("Authorization") String user_access_token);

    @POST("reject-job")
    Observable<Response<ResponseBody>> completeJob(@Body JsonObject jsonObject,@Header("Authorization") String user_access_token);

}
