package id.ac.polinema.absensiprojectuas.restapi;

import java.util.List;
import java.util.Map;

import id.ac.polinema.absensiprojectuas.models.AbsenItem;
import id.ac.polinema.absensiprojectuas.models.GuruItem;
import id.ac.polinema.absensiprojectuas.models.PilihSiswaItem;
import id.ac.polinema.absensiprojectuas.models.RekapItem;
import id.ac.polinema.absensiprojectuas.models.SiswaItem;
import id.ac.polinema.absensiprojectuas.models.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("loginAdmin")
    Call<ResponseBody> loginAdmin(@Body User user);

    @POST("loginGuru")
    Call<ResponseBody> loginGuru(@Body User user);

    @GET("guru")
    Call<List<GuruItem>> getGuru();

    @GET("guru")
    Call<List<GuruItem>> getGuruByUsername(
            @Query("username") String username
    );

    @GET("absenGuru")
    Call<List<AbsenItem>> getAbsenByUsername(
            @Query("username") String username
    );

    @POST("absenGuru")
    Call<ResponseBody> absenGuru(@Body AbsenItem absen);

    @GET("Rekapan")
    Call<List<RekapItem>> listRekap();


    @GET("Siswa")
    Call<List<PilihSiswaItem>> getSemuaSiswa();


    @GET("Siswa")
    Call<List<SiswaItem>> getSiswa();

    @POST("Siswa")
    Call<ResponseBody> tambahSiswa(@Body SiswaItem siswa);


    @Multipart
    @POST("guru")
    Call<ResponseBody> tambahGuru(
            @Part MultipartBody.Part photo,
            @PartMap Map<String, RequestBody> text);

}