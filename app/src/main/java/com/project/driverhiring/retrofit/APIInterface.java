package com.project.driverhiring.retrofit;

import com.project.driverhiring.model.Root;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIInterface {


    @Multipart
    @POST("user_reg.php")
    Call<Root> USERREGROOTCALL(@Part("name") RequestBody name,
                               @Part("email") RequestBody email,
                               @Part("phone") RequestBody phone,
                               @Part("password") RequestBody password,
                               @Part("address") RequestBody address,
                               @Part MultipartBody.Part licenceImage,
                               @Part MultipartBody.Part proImage);

    @Multipart
    @POST("driver_reg.php")
    Call<Root> DRIVEREGROOTCALL(@Part("name") RequestBody name,
                                @Part("phone") RequestBody phone,
                                @Part("email") RequestBody email,
                                @Part("password") RequestBody password,
                                @Part("address") RequestBody address,
                                @Part("district") RequestBody district,
                                @Part("state") RequestBody state,
                                @Part("experience") RequestBody experience,
                                @Part("languages") RequestBody languages,
                                @Part("blood") RequestBody bloodGroup,
                                @Part("device_token") RequestBody deviceToken,
                                @Part("hgmv") RequestBody hgmv,
                                @Part("long_distance") RequestBody longDrive,
                                @Part("route_pref") RequestBody routePref,
                                @Part("account_no") RequestBody accountNo,
                                @Part("ifsc") RequestBody iFsc,
                                @Part MultipartBody.Part licenceImage,
                                @Part MultipartBody.Part proImage);

    @GET("driver_login.php")
    Call<Root> DRIVER_LOGIN(@Query("phone") String phone, @Query("password") String password, @Query("device_token") String deviceToken);

    @GET("user_login.php")
    Call<Root> USER_LOGIN(@Query("phone") String phone, @Query("password") String password, @Query("device_token") String deviceToken);

    @GET("view_driver_list.php")
    Call<Root> DRIVER_LIST_CALL(@Query("s_lat") String userLat, @Query("s_long") String userLong,
                                @Query("d_lat") String destinationLat, @Query("d_long") String destinationLong,
                                @Query("hgmv") String vehicleType, @Query("route_pref") String routePref);

//    @Multipart
//    @POST("bookRide.php")
//    Call<Root> BOOKRIDE_CALL(@Part("uid") RequestBody userId, @Part("did") RequestBody driverId, @Part("s_latitude") RequestBody userLatitude,
//                             @Part("s_longitude") RequestBody userLongitude, @Part("e_latitude") RequestBody destinationLatitude,
//                             @Part("e_longitude") RequestBody destinationLongitude,
//                             @Part("vehicle_no") RequestBody vehicleNumber, @Part("vehicle_mode_name") RequestBody vehicleModelName,
//                             @Part("vehicle_type") RequestBody vehicleType, @Part("comments") RequestBody comments);

    //TODO fix file upload

//    @Multipart
//    @POST("bookRide.php")
//    Call<Root> BOOKRIDE_CALL(@Part("uid") RequestBody userId, @Part("did") RequestBody driverId, @Part("s_latitude") RequestBody userLatitude,
//                             @Part("s_longitude") RequestBody userLongitude, @Part("e_latitude") RequestBody destinationLatitude,
//                             @Part("e_longitude") RequestBody destinationLongitude,
//                             @Part("vehicle_no") RequestBody vehicleNumber, @Part("vehicle_mode_name") RequestBody vehicleModelName,
//                             @Part("vehicle_type") RequestBody vehicleType, @Part("comments") RequestBody comments,
//                             @Part MultipartBody.Part rcBookImage, @Part MultipartBody.Part insuranceImage,
//                             @Part MultipartBody.Part pollutionImage);

    @FormUrlEncoded
    @POST("bookRide.php")
    Call<Root> BOOKRIDE_CALL(@Field("uid") String userId, @Field("did") String driverId, @Field("s_latitude") String userLatitude,
                             @Field("s_longitude") String userLongitude, @Field("e_latitude") String destinationLatitude,
                             @Field("e_longitude") String destinationLongitude,
                             @Field("vehicle_no") String vehicleNumber, @Field("vehicle_mode_name") String vehicleModelName,
                             @Field("vehicle_type") String vehicleType, @Field("comments") String comments);

    @FormUrlEncoded
    @POST("rideHistoryDriver.php")
    Call<Root> DRIVER_RIDE_HISTORY(@Field("did") String driverId);

    @GET("driver_avail.php")
    Call<Root> UPDATE_DRIVER_AVAILABILITY(@Query("online") String online, @Query("id") String driverId);

    @GET("view_driver_avail.php")
    Call<Root> VIEW_DRIVER_AVAILABILITY(@Query("id") String driverId);

    @FormUrlEncoded
    @POST("rideHistoryUser.php")
    Call<Root> USER_RIDE_HISTORY(@Field("uid") String userId);

    @FormUrlEncoded
    @POST("startStopRideUser.php")
    Call<Root> USER_RIDE_START_OR_STOP(@Field("uid") String userId, @Field("rid") String rideId, @Field("status") String status);

    @FormUrlEncoded
    @POST("payment.php")
    Call<Root> PAYMENT_API_USER(@Field("rid") String rideId, @Field("amount") String amount);

    @FormUrlEncoded
    @POST("rateReview.php")
    Call<Root> ADD_RATING_REVIEW_USER(@Field("uid") String userId,
                                      @Field("rid") String rideId,
                                      @Field("rate") String ratingCount, @Field("review") String review);


    @FormUrlEncoded
    @POST("startRideDriver.php")
    Call<Root> RIDE_START_OR_STOP_DRIVER(@Field("rid") String rideId,
                                         @Field("did") String driverId,
                                         @Field("status") String status);

    @FormUrlEncoded
    @POST("acc_rej.php")
    Call<Root> RIDE_ACCEPT_OR_REJECT(@Field("rid") String rideId,
                                     @Field("did") String driverId,
                                     @Field("acc_rej") String status);

    @FormUrlEncoded
    @POST("notRide.php")
    Call<Root> PENDING_REQUESTS_DRIVER(@Field("did") String driverId);

    @FormUrlEncoded
    @POST("viewProfileUser.php")
    Call<Root> VIEW_USER_PROFILE(@Field("uid") String userId);

    @FormUrlEncoded
    @POST("viewProfileDriver.php")
    Call<Root> VIEW_DRIVER_PROFILE(@Field("did") String userId);

    @Multipart
    @POST("profileEditUser.php")
    Call<Root> EDIT_USER_PROFILE(@Part("uid") RequestBody userId,
                                 @Part("name") RequestBody name,
                                 @Part("email") RequestBody email,
                                 @Part("address") RequestBody address, @Part MultipartBody.Part profileImage);

    @Multipart
    @POST("profileEditDriver.php")
    Call<Root> EDIT_DRIVER_PROFILE(@Part("did") RequestBody driverId,
                                   @Part("name") RequestBody name,
                                   @Part("address") RequestBody address,
                                   @Part("email")RequestBody email,
                                   @Part("languages") RequestBody languages,
                                   @Part("account_no") RequestBody accountNumber,
                                   @Part("ifsc") RequestBody ifsc,
                                   @Part("route_preference")RequestBody routePref,
                                   @Part MultipartBody.Part profileImage);

    @FormUrlEncoded
    @POST("user_complaint.php")
    Call<Root> USER_COMPLAINT(@Field("rid") String rideId, @Field("uid") String userId, @Field("user_complaint") String userComplaint);

    @FormUrlEncoded
    @POST("complaintDriver.php")
    Call<Root> DRIVER_COMPLAINT(@Field("rid") String rideId,
                                @Field("did") String driverId,
                                @Field("driver_complaint") String driverComplaint);


    @FormUrlEncoded
    @POST("driverLogout.php")
    Call<Root> DRIVER_LOGOUT(@Field("did") String driverId);

    @FormUrlEncoded
    @POST("userLogout.php")
    Call<Root> USER_LOGOUT(@Field("uid") String userId);

}
