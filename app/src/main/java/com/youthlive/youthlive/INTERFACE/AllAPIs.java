package com.youthlive.youthlive.INTERFACE;


import com.youthlive.youthlive.ExchangeDiamondPOJO.ExchangeBean;
import com.youthlive.youthlive.GetRankingPOJO.RankingBean;
import com.youthlive.youthlive.acceptRejectPOJO.acceptRejectBean;
import com.youthlive.youthlive.addCareerPOJO.addCareerBean;
import com.youthlive.youthlive.addEducationPOJO.addEducationBean;
import com.youthlive.youthlive.addVideoPOJO.addVideoBean;
import com.youthlive.youthlive.addWalletPOJO.addWalletBean;
import com.youthlive.youthlive.allMessagePOJO.allMessageBean;
import com.youthlive.youthlive.checkStatusPOJO.checkStatusBean;
import com.youthlive.youthlive.commentPOJO.commentBean;
import com.youthlive.youthlive.deleteCareerPOJO.deleteCareerBean;
import com.youthlive.youthlive.deleteVLOGPOJO.deleteVLOGBean;
import com.youthlive.youthlive.editCareerPOJO.editCareerBean;
import com.youthlive.youthlive.editEducationPOJO.editEducationBean;
import com.youthlive.youthlive.engineLiveUsersPOJO.engineLiveUsersBean;
import com.youthlive.youthlive.feedBackPOJO.feedBackBean;
import com.youthlive.youthlive.followListPOJO.followListBean;
import com.youthlive.youthlive.followPOJO.followBean;
import com.youthlive.youthlive.getConnectionPOJO.getConnectionBean;
import com.youthlive.youthlive.getIpdatedPOJO.getUpdatedBean;
import com.youthlive.youthlive.getLivePOJO.getLiveBean;
import com.youthlive.youthlive.getSingleIrisBean;
import com.youthlive.youthlive.giftPOJO.giftBean;
import com.youthlive.youthlive.goLivePOJO.goLiveBean;
import com.youthlive.youthlive.liveBean;
import com.youthlive.youthlive.liveCommentPOJO.liveCommentBean;
import com.youthlive.youthlive.liveLikePOJO.liveLikeBean;
import com.youthlive.youthlive.login2POJO.login2Bean;
import com.youthlive.youthlive.loginResponsePOJO.loginResponseBean;
import com.youthlive.youthlive.otpPOJO.otpBean;
import com.youthlive.youthlive.requestConnectionPOJO.requestConnectionBean;
import com.youthlive.youthlive.sendGiftPOJO.sendGiftBean;
import com.youthlive.youthlive.sendMessagePOJO.sendMessageBean;
import com.youthlive.youthlive.sharePOJO.shareBean;
import com.youthlive.youthlive.singleMessagePOJO.singleMessageBean;
import com.youthlive.youthlive.singleStreamBean;
import com.youthlive.youthlive.singleVideoPOJO.singleVideoBean;
import com.youthlive.youthlive.socialPOJO.socialBean;
import com.youthlive.youthlive.startStreamPOJO.startStreamBean;
import com.youthlive.youthlive.streamPOJO.streamBean;
import com.youthlive.youthlive.streamResponsePOJO.streamResponseBean;
import com.youthlive.youthlive.timelinePOJO.timelineBean;
import com.youthlive.youthlive.timelineProfilePOJO.timelineProfileBean;
import com.youthlive.youthlive.updatePOJO.updateBean;
import com.youthlive.youthlive.updateProfilePOJO.updateProfileBean;
import com.youthlive.youthlive.vlogListPOJO.vlogListBean;
import com.youthlive.youthlive.walletPOJO.walletBean;
import com.youthlive.youthlive.wowzaAPIPOJO.wowzaAPIBean;
import com.youthlive.youthlive.wowzaLiveStreamsPOJO.getWowzaStreamBean;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AllAPIs {
    @Multipart
    @POST("youthlive/api/sign_up.php")
    Call<loginResponseBean> signUp(
            @Part("phone") String phone,
            @Part("countryCode") String code
    );
    @Multipart
    @POST("youthlive/api/socialsign_up.php")
    Call<socialBean> socialSignIn(
            @Part("pid") String pid,
            @Part("email") String email
    );

    @Multipart
    @POST("youthlive/api/resend_code.php")
    Call<loginResponseBean> resend(
            @Part("phone") String phone,
            @Part("countryCode") String code
    );

    @Multipart
    @POST("youthlive/api/follow_unfollow.php")
    Call<followBean> follow(
            @Part("userId") String userId,
            @Part("friendId") String friendId
    );

    @Multipart
    @POST("youthlive/api/user_add_beans.php")
    Call<addWalletBean> addBeans(
            @Part("userId") String userId,
            @Part("amount") String amount
    );

    @Multipart
    @POST("youthlive/api/user_feedback.php")
    Call<feedBackBean> feedback(
            @Part("userId") String userId,
            @Part("message") String message
    );

    @Multipart
    @POST("youthlive/api/user_report.php")
    Call<feedBackBean> report(
            @Part("userId") String userId,
            @Part("message") String message,
            @Part("viewId") String viewId
    );

    @Multipart
    @POST("youthlive/api/follow_list.php")
    Call<followListBean> followList(
            @Part("userId") String userId
    );

    @Multipart
    @POST("youthlive/api/all_message.php")
    Call<allMessageBean> allMessageList(
            @Part("userId") String userId
    );

    @Multipart
    @POST("youthlive/api/get_wallet.php")
    Call<walletBean> getWalletData(
            @Part("userId") String userId
    );

    @Multipart
    @POST("youthlive/api/update_user_info.php")
    Call<loginResponseBean> addUserData(
            @Part MultipartBody.Part file,
            @Part("userName") String userName,
            @Part("gender") String gender,
            @Part("birthday") String birthday,
            @Part("bio") String bio,
            @Part("userId") String userId
    );

    @Multipart
    @POST("youthlive/api/update_user_info.php")
    Call<updateBean> updateUserData(
            @Part("userName") String userName,
            @Part("gender") String gender,
            @Part("birthday") String birthday,
            @Part("bio") String bio,
            @Part("userId") String userId
    );

    @Multipart
    @POST("youthlive/api/add_career.php")
    Call<addCareerBean> addCareer(
            @Part("position") String position,
            @Part("company") String company,
            @Part("from") String from,
            @Part("to") String to,
            @Part("userId") String userId
    );

    @Multipart
    @POST("youthlive/api/edit_career.php")
    Call<editCareerBean> editCareer(
            @Part("userId") String userId,
            @Part("position") String position,
            @Part("company") String company,
            @Part("from") String from,
            @Part("to") String to,
            @Part("careerId") String careerId
    );

    @Multipart
    @POST("youthlive/api/varify_code.php")
    Call<otpBean> verify(
            @Part("userId") String userId,
            @Part("code") String code
    );

    @Multipart
    @POST("youthlive/api/delete_career.php")
    Call<deleteCareerBean> deleteCareer(
            @Part("userId") String userId,
            @Part("careerId") String code
    );

    @Multipart
    @POST("youthlive/api/video_share.php")
    Call<shareBean> share(
            @Part("userId") String userId,
            @Part("videoId") String videoId
    );

    @Multipart
    @POST("youthlive/api/add_user_image.php")
    Call<updateProfileBean> updateProfile(
            @Part("userId") String userId,
            @Part MultipartBody.Part file
    );

    @Multipart
    @POST("youthlive/api/create_password.php")
    Call<otpBean> createPassword(
            @Part("userId") String userId,
            @Part("password") String password
    );

    @Multipart
    @POST("youthlive/api/update_phone.php")
    Call<loginResponseBean> updatePhone(
            @Part("userId") String userId,
            @Part("countryCode") String code,
            @Part("phone") String phone
    );

    @Multipart
    @POST("youthlive/api/single_user_message.php")
    Call<singleMessageBean> singleChatList(
            @Part("userId") String userId,
            @Part("friendId") String friendId,
            @Part("chatId") String chatId
    );

    @Multipart
    @POST("youthlive/api/send_message.php")
    Call<sendMessageBean> sendMessage(
            @Part("userId") String userId,
            @Part("friendId") String friendId,
            @Part("message") String message
    );

    @Multipart
    @POST("youthlive/api/go_live.php")
    Call<goLiveBean> goLive(
            @Part("userId") String userId,
            @Part("thirdPartyKey") String key,
            @Part("liveTag") String tag
    );

    @Multipart
    @POST("youthlive/api/add_education.php")
    Call<addEducationBean> addEducation(
            @Part("userId") String userId,
            @Part("educationTitle") String title,
            @Part("educationTime") String year
    );

    @Multipart
    @POST("youthlive/api/edit_education.php")
    Call<editEducationBean> editEducation(
            @Part("userId") String userId,
            @Part("educationId") String educationId,
            @Part("educationTitle") String title,
            @Part("educationTime") String year
    );

    @Multipart
    @POST("youthlive/api/delete_education.php")
    Call<editEducationBean> deleteEducation(
            @Part("userId") String userId,
            @Part("educationId") String educationId
    );

    @Multipart
    @POST("youthlive/api/get_profile.php")
    Call<loginResponseBean> getProfile(
            @Part("userId") String userId
    );


    @Multipart
    @POST("youthlive/api/get_profile_new.php")
    Call<timelineProfileBean> getProfile2(
            @Part("userId") String userId,
            @Part("friendId") String friendId
    );


    @Multipart
    @POST("youthlive/api/add_cover_image.php")
    Call<loginResponseBean> addCover(
            @Part("userId") String userId,
            @Part("title") String title,
            @Part MultipartBody.Part file
    );

    @Multipart
    @POST("youthlive/api/mobile_signin.php")
    Call<login2Bean> signIn(
            @Part("phone") String phone,
            @Part("password") String password
    );

    @Multipart
    @POST("youthlive/api/all_live_user.php")
    Call<List<liveBean>> getLives(
            @Part("userId") String userId
    );

    @Multipart
    @POST("youthlive/api/get_timelinelist.php")
    Call<timelineBean> getTimeline(
            @Part("userId") String userId
    );


    @Headers({
            "Accept: application/vnd.bambuser.v1+json",
            "Content-Type: application/json",
            "Authorization: Bearer 3m5chxsssdd7f6d3s7l2pkklx"
    })
    @GET("broadcasts")
    Call<getLiveBean> getLiveList();



    @Headers({
            "Accept: application/json",
    })
    @GET("v2/servers/_defaultServer_/vhosts/_defaultVHost_/applications/youthlive/instances/_definst_")
    Call<String> getEngineLiveList();



    @Headers({
            "Accept: application/vnd.bambuser.v1+json",
            "Content-Type: application/json",
            "Authorization: Bearer 3m5chxsssdd7f6d3s7l2pkklx"
    })
    @GET("broadcasts/{id}")
    Call<singleStreamBean> getSingleLive(@Path("id") String id);


    @Multipart
    @POST("youthlive/api/all_video.php")
    Call<vlogListBean> getVlogList(
            @Part("userId") String userId
    );

    @Multipart
    @POST("youthlive/api/all_gift.php")
    Call<giftBean> getGiftData(
            @Part("userId") String userId
    );


    @Multipart
    @POST("youthlive/api/get_video.php")
    Call<vlogListBean> getVlog(
            @Part("userId") String userId
    );

    @Multipart
    @POST("youthlive/api/get_single_video.php")
    Call<singleVideoBean> getsingleVideo(
            @Part("userId") String userId,
            @Part("videoId") String videoId
    );

    @Multipart
    @POST("youthlive/api/delete_video.php")
    Call<deleteVLOGBean> removeVideo(
            @Part("userId") String userId,
            @Part("videoId") String videoId
    );

    @Multipart
    @POST("youthlive/api/getUpdatedData.php")
    Call<getUpdatedBean> getUpdatedData(
            @Part("userId") String userId,
            @Part("liveId") String liveId
    );

    @Multipart
    @POST("youthlive/api/go_live_like.php")
    Call<liveLikeBean> likeLive(
            @Part("userId") String userId,
            @Part("liveId") String liveId
    );


    @Multipart
    @POST("youthlive/api/video_likes.php")
    Call<singleVideoBean> likeVideo(
            @Part("userId") String userId,
            @Part("videoId") String videoId
    );
    @Multipart
    @POST("youthlive/api/video_comment.php")
    Call<commentBean> comment(
            @Part("userId") String userId,
            @Part("videoId") String videoId,
            @Part("comment") String comment
    );

    @Multipart
    @POST("youthlive/api/add_live_comment.php")
    Call<liveCommentBean> commentLive(
            @Part("userId") String userId,
            @Part("videoId") String videoId,
            @Part("comment") String comment
    );

    @Multipart
    @POST("youthlive/api/send_live_gift.php")
    Call<sendGiftBean> sendGift(
            @Part("userId") String userId,
            @Part("videoId") String videoId,
            @Part("liveUserId") String liveId,
            @Part("giftId") String giftId,
            @Part("qty") String qty,
            @Part("diamond") String comment
    );

    @Multipart
    @POST("youthlive/api/add_video.php")
    Call<addVideoBean> addVideo(
            @Part("userId") String userId,
            @Part("caption") String caption,
            @Part("tag") String tag,
            @Part MultipartBody.Part file
    );



    @Multipart
    @POST("youthlive/api/get_ranking.php")
    Call<RankingBean> ranking(
            @Part("userId") String userId,
            @Part("type") String caption
    );

    @Multipart
    @POST("youthlive/api/get_connection.php")
    Call<getConnectionBean> getConnection(
            @Part("userId") String userId,
            @Part("liveId") String liveId
    );


    @Multipart
    @POST("youthlive/api/checkRequest.php")
    Call<checkStatusBean> checkStatus(
            @Part("userId") String userId,
            @Part("liveId") String liveId
    );


    @Multipart
    @POST("youthlive/api/accept_reject_connection.php")
    Call<acceptRejectBean> acceptReject(
            @Part("requestId") String requestId,
            @Part("url") String url,
            @Part("status") String status
    );


    @Multipart
    @POST("youthlive/api/request_connection.php")
    Call<requestConnectionBean> requestConnection(
            @Part("liveId") String requestId,
            @Part("liveUserId") String url,
            @Part("receiverId") String status
    );


    @Multipart
    @POST("youthlive/api/all_wowza_user.php")
    Call<List<wowzaAPIBean>> getAllStreams2(
            @Part("userId") String userId
    );

    @Multipart
    @POST("youthlive/api/exchange_beans_diamond.php")
    Call<ExchangeBean> exchange(
            @Part("userId") String userId,
            @Part("beans") String code,
            @Part("diamond") String phone
    );


    //@Multipart
    @Headers({"Content-Type: application/json", "wsc-api-key: dicLNKTWDjmx14cgVpw1sLVuQxBQVbOVE0tpkf3y6VijiUln4sn3QJ2W5zKr3524", "wsc-access-key: tsaCoQS07GnFTTcel0L3gY59ELa7ouYykKFXLQLBApzjPV7a3IWfKXkYt0e3323e"})
    @POST("api/v1/live_streams")
    Call<streamResponseBean> createStream(
            @Body streamBean body
            );

    //@Multipart
    @Headers({"Content-Type: application/json", "wsc-api-key: dicLNKTWDjmx14cgVpw1sLVuQxBQVbOVE0tpkf3y6VijiUln4sn3QJ2W5zKr3524", "wsc-access-key: tsaCoQS07GnFTTcel0L3gY59ELa7ouYykKFXLQLBApzjPV7a3IWfKXkYt0e3323e"})
    @PUT("api/v1/live_streams/{id}/start")
    Call<startStreamBean> startStream(
            @Path("id") String id
    );

    @Headers({"Content-Type: application/json", "wsc-api-key: dicLNKTWDjmx14cgVpw1sLVuQxBQVbOVE0tpkf3y6VijiUln4sn3QJ2W5zKr3524", "wsc-access-key: tsaCoQS07GnFTTcel0L3gY59ELa7ouYykKFXLQLBApzjPV7a3IWfKXkYt0e3323e"})
    @PUT("api/v1/live_streams/{id}/stop")
    Call<startStreamBean> stopStream(
            @Path("id") String id
    );

    @Headers({"Content-Type: application/json", "wsc-api-key: dicLNKTWDjmx14cgVpw1sLVuQxBQVbOVE0tpkf3y6VijiUln4sn3QJ2W5zKr3524", "wsc-access-key: tsaCoQS07GnFTTcel0L3gY59ELa7ouYykKFXLQLBApzjPV7a3IWfKXkYt0e3323e"})
    @GET("api/v1/live_streams/{id}/state")
    Call<startStreamBean> getState(
            @Path("id") String id
    );

    @Headers({"Content-Type: application/json", "wsc-api-key: dicLNKTWDjmx14cgVpw1sLVuQxBQVbOVE0tpkf3y6VijiUln4sn3QJ2W5zKr3524", "wsc-access-key: tsaCoQS07GnFTTcel0L3gY59ELa7ouYykKFXLQLBApzjPV7a3IWfKXkYt0e3323e"})
    @GET("api/v1/live_streams")
    Call<getWowzaStreamBean> getAllStreams();

}
