package com.example.garage_examen_mob;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

public class Api {
    public static final String BASE_URL = "http://10.89.247.60/GARAGE/api/"; // Vérifiez l'IP !
    private static Service service;

    public static Service get() {
        if (service == null) {
            HttpLoggingInterceptor log = new HttpLoggingInterceptor();
            log.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(log).connectTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).build();
            service = new Retrofit.Builder().baseUrl(BASE_URL).client(client)
                    .addConverterFactory(GsonConverterFactory.create()).build().create(Service.class);
        }
        return service;
    }

    public static class Res<T> {
        @SerializedName("success") public boolean success;
        @SerializedName("data")    public T data;
        @SerializedName("message") public String message;
    }

    public interface Service {
        @POST("login.php") Call<Res<Models.LoginRes>> login(@Body Map<String, String> creds);

        @GET("clients.php") Call<Res<List<Models.Client>>> getClients();
        @POST("clients.php") Call<Res<Models.Client>> createClient(@Body Models.Client c);
        @PUT("clients.php") Call<Res<Models.Client>> updateClient(@Query("id") int id, @Body Models.Client c);
        @DELETE("clients.php") Call<Res<Map<String,Object>>> deleteClient(@Query("id") int id);

        @GET("mecaniciens.php") Call<Res<List<Models.Mecanicien>>> getMecaniciens();
        @POST("mecaniciens.php") Call<Res<Models.Mecanicien>> createMecanicien(@Body Models.Mecanicien m);
        @PUT("mecaniciens.php") Call<Res<Models.Mecanicien>> updateMecanicien(@Query("id") int id, @Body Models.Mecanicien m);
        @DELETE("mecaniciens.php") Call<Res<Map<String,Object>>> deleteMecanicien(@Query("id") int id);

        @GET("vehicules.php") Call<Res<List<Models.Vehicule>>> getVehicules();
        @POST("vehicules.php") Call<Res<Models.Vehicule>> createVehicule(@Body Models.Vehicule v);
        @PUT("vehicules.php") Call<Res<Models.Vehicule>> updateVehicule(@Query("id") int id, @Body Models.Vehicule v);
        @DELETE("vehicules.php") Call<Res<Map<String,Object>>> deleteVehicule(@Query("id") int id);

        @GET("interventions.php") Call<Res<List<Models.Intervention>>> getInterventions();
        @POST("interventions.php") Call<Res<Models.Intervention>> createIntervention(@Body Models.Intervention i);
        @PUT("interventions.php") Call<Res<Models.Intervention>> updateIntervention(@Query("id") int id, @Body Models.Intervention i);
        @DELETE("interventions.php") Call<Res<Map<String,Object>>> deleteIntervention(@Query("id") int id);

        @GET("statistiques.php") Call<Res<Models.Statistiques>> getStatistiques();
    }
}