package imu.nvbsp.doh.imusync.libs;

import java.util.ArrayList;

import imu.nvbsp.doh.imusync.models.Donor;
import imu.nvbsp.doh.imusync.models.PullResponse;
import imu.nvbsp.doh.imusync.models.PushResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiRequest {

    @GET("pull/{id}")
    Call<PullResponse> pull(@Path("id") String id);

    @POST("push")
    Call<PushResponse> push(@Body String id, @Body ArrayList<Donor> donors);
}
