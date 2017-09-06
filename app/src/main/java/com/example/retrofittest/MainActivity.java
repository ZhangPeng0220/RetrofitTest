package com.example.retrofittest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testRetrofitHttpGet();
    }
    private void testRetrofitHttpGet() {
        // step1
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apicloud.mob.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // step2

        //get方法
        DemoService service = retrofit.create(DemoService.class);
        Map<String,String> params =new HashMap<>();
        params.put("key","appkey");
        params.put("city","通州");
        params.put("province","北京");
        Call<JsonObject> call = service.testHttpGet(params);

        //post方法
        upLoadService2 service2 = retrofit.create(upLoadService2.class);
        try {
            //提交汉字会出现乱码
            String name =  URLEncoder.encode("张德帅","UTF-8");
            Call<JsonObject> call2 = service2.uploadNewUser(name,"male",24);
            //@FieldMap供我们使用，使用方法参照@QueryMap
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        // step4
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.d("yeszp", response.body().result.size()+"");
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("nozp", t.getMessage());
            }
        });
    }
    //http://apicloud.mob.com/v1/weather/query?key=appkey&city=通州&province=北京
    public interface DemoService {
        @GET("v1/weather/query")
        Call<JsonObject> testHttpGet(@QueryMap Map<String,String> params);
    }

    //post提交参数，参数用user封装，以json形式提交
    public interface upLoadService1 {
        @POST("v1/weather/query")
        Call<JsonObject> uploadNewUser(@Body User user);
    }
    //post提交参数，参数用user封装，以json形式提交
    public interface upLoadService2 {
        // @Headers("Content-type:application/x-www-form-urlencoded;charset=UTF-8")也可以用于解决汉字乱码问题
        //或者设定动态header,也可以用于动态变换格式
        Call<JsonObject> uploadNewUser(@Header("Content-Type") String contentType, @Field("username") String username, @Field("gender") String male, @Field("age") int age);
        @FormUrlEncoded
        @POST("api/users")
        Call<JsonObject> uploadNewUser(@Field("username") String username, @Field("gender") String male, @Field("age") int age);
    }

}
