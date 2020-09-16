package com.exnovation.wishster.Network;

import com.exnovation.wishster.Models.DeleteWishlistModel;
import com.exnovation.wishster.Models.ForgotPasswordModel;
import com.exnovation.wishster.Models.LoginModel;
import com.exnovation.wishster.Models.ProductListModel;
import com.exnovation.wishster.Models.ChangePasswordModel;
import com.exnovation.wishster.Models.ProfileModel;
import com.exnovation.wishster.Models.ResetPasswordModel;
import com.exnovation.wishster.Models.ReviewQuestion;
import com.exnovation.wishster.Models.SaveQuestion;
import com.exnovation.wishster.Models.SaveToWishlistModel;
import com.exnovation.wishster.Models.ServerStatus;
import com.exnovation.wishster.Models.SignUpModel;
import com.exnovation.wishster.Models.UpdateProfileImgModel;
import com.exnovation.wishster.Models.VerifyOtpModel;
import com.exnovation.wishster.Models.WishListModel;
import com.exnovation.wishster.Models.wishListModel2;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })

    @POST("forget_pwd")
    Call<ForgotPasswordModel> getOtpEmail(@Body RequestBody email);

    @POST("reset_pwd")
    Call<ResetPasswordModel> resetPassword(@Body RequestBody bodyRequest);

    /*@FormUrlEncoded
    @POST("verify_otp")
    Call<VerifyOtpModel> getVerifyOtp(@Field("email") String email,
                                      @Field("otp") String otp);*/

    @FormUrlEncoded
    @POST("change_password")
    Call<ChangePasswordModel> setNewPassword(@Field("old_password") String old_password,
                                             @Field("new_password") String new_password);


    @POST("get_products")
    Call<ProductListModel> getProductList(@Body RequestBody bodyRequest);



    @GET("security_qtn")
    Call<ReviewQuestion> ReviewQuestion();
    @GET("profile")
    Call<ProfileModel> getProfile();
    @POST("save_security_qtn")
    Call<SaveQuestion> SaveQuestion(@Body RequestBody bodyRequest);
    @POST("login")
    Call<LoginModel> login(@Body RequestBody bodyRequest);
    @POST("social_login")
    Call<LoginModel> Social_login(@Body RequestBody bodyRequest);
    @POST("register")
    Call<ServerStatus> Register_email(@Body RequestBody bodyRequest);
    @POST("register_set_pwd")
    Call<SignUpModel> signup(@Body RequestBody bodyRequest);
    @POST("verify_otp")
    Call<VerifyOtpModel> getVerifyOtp(@Body RequestBody bodyRequest);
    @POST("update_profile")
    Call<SignUpModel> Update_profile(@Body RequestBody bodyRequest);

    @Multipart
    @POST("update_profile_img")
    Call<UpdateProfileImgModel> UpdatePrfImage(@Part MultipartBody.Part profile_img);

    @POST("save_wishlist")
    Call<SaveToWishlistModel> saveToWishlist(@Body RequestBody bodyRequest);

    @GET("get_wishlist")
    Call<wishListModel2> getwishList();

    @POST("delete_wishlist")
    Call<DeleteWishlistModel> deleteWishlist(@Body RequestBody requestBody);

}

