package com.example.kinjal.ohdeals;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import retrofit2.http.POST;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static java.util.concurrent.TimeUnit.SECONDS;

public class HttpApi {

	//public static final String BASE_URL = "http://websitedemo.co.in/phpdemoz/patholab/api_v6/";
	public static final String BASE_URL = "http://ohdeals.com/mobileapp/";
	private static HttpApi instance = null;
	private Map<String, String> headers = new HashMap<String, String>();
	private HttpBinService service;

	public static class HttpBinResponse {
		// the request msg
		public String Message;

		// the requester status
		public String Status;

		// all headers that have been sent
		public List<Map> PostOffice;

	}

	public interface HttpBinService {

		@POST("login.php?")
		Call<ModelLogin> postlogindata(String email,String password);

		/*@GET("forecast?")
		Call<Wheather> getwheather(@Query("zip") String zip,@Query("appid") String appid);

		@GET
		Call<HttpBinResponse> dynamicUrl(@Url String url);

		
		@GET("pincode/{pincode}")
		Call<PostCodeModel> pincodelist(@Path("pincode") String pincode);*/
		
	}

	private HttpApi() {
		// Http interceptor to add custom headers to every request
		OkHttpClient httpClient = new OkHttpClient();
		//httpClient.setReadTimeout(30, TimeUnit.SECONDS);
		httpClient.networkInterceptors().add(new Interceptor() {
			public Response intercept(Chain chain)
					throws IOException {
				Request.Builder builder = chain.request().newBuilder();

				System.out.println("Adding headers:" + headers);
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					builder.addHeader(entry.getKey(), entry.getValue());
				}

				return chain.proceed(builder.build());
			}
		});

		// Retrofit setup
		Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
				.client(httpClient)
				.addConverterFactory(GsonConverterFactory.create()).build();

		// Service setup
		service = retrofit.create(HttpBinService.class);
		
	}

	/**
	 * Get the HttpApi singleton instance
	 */
	public static HttpApi getInstance() {
		instance = new HttpApi();
		return instance;
	}

	/**
	 * Get the API service to execute calls with
	 */
	public HttpBinService getService() {
		return service;
	}

	/**
	 * Add a header which is added to every API request
	 */
	public void addHeader(String key, String value) {
		headers.put(key, value);
	}

	/**
	 * Add multiple headers
	 */
	public void addHeaders(Map<String, String> headers) {
		this.headers.putAll(headers);
	}

	/**
	 * Remove a header
	 */
	public void removeHeader(String key) {
		headers.remove(key);
	}

	/**
	 * Remove all headers
	 */
	public void clearHeaders() {
		headers.clear();
	}

	/**
	 * Get all headers
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}
}