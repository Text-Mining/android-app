package ir.textmining.app.util;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {

  public static Retrofit getRetrofit(String token) {
    return new Retrofit.Builder().baseUrl(Constant.BASE_URL)
        .client(getHeader(token))
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  private static OkHttpClient getHeader(final String authorizationValue) {
    //delete this interceptor in released app
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    //end interceptor
    return new OkHttpClient.Builder()
        //.addInterceptor(interceptor)
        .addNetworkInterceptor(
            new Interceptor() {
              @Override
              public Response intercept(@NonNull Chain chain) throws IOException {
                Request request = null;
                if (authorizationValue != null) {
                  Request original = chain.request();
                  // Request customization: add request headers
                  Request.Builder requestBuilder = original.newBuilder()
                      .addHeader("Authorization", "Bearer " + authorizationValue)
                      .addHeader("Content-Type", "application/x-www-form-urlencoded");

                  request = requestBuilder.build();
                }
                assert request != null;
                return chain.proceed(request);
              }
            })
        .build();
  }
}
