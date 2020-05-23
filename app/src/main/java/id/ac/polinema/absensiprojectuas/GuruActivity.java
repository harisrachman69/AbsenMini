package id.ac.polinema.absensiprojectuas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.ac.polinema.absensiprojectuas.helper.Session;
import id.ac.polinema.absensiprojectuas.models.AbsenItem;
import id.ac.polinema.absensiprojectuas.models.GuruItem;
import id.ac.polinema.absensiprojectuas.restapi.ApiClient;
import id.ac.polinema.absensiprojectuas.restapi.ApiInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuruActivity extends AppCompatActivity {
    private ImageView profil;
    private TextView id_guru, nama, alamat, jenis_kelamin, no_telp, username, password;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru);

        session = new Session(getApplicationContext());

        final RecyclerView absenView = findViewById(R.id.rv_absen);
        final ItemAdapter itemAdapter = new ItemAdapter<>();
        final FastAdapter fastAdapter = FastAdapter.with(itemAdapter);

        final List absen = new ArrayList<>();

        profil = findViewById(R.id.foto_profil);
        id_guru = findViewById(R.id.id_guru);
        nama = findViewById(R.id.nama_guru);
        alamat = findViewById(R.id.alamat_guru);
        jenis_kelamin = findViewById(R.id.jenis_kelamin_guru);
        no_telp = findViewById(R.id.telp_guru);
        username = findViewById(R.id.username_guru);
        password = findViewById(R.id.password_guru);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        Call<List<GuruItem>> call = apiInterface.getGuruByUsername(session.getUsername());
//
//        call.enqueue(new Callback<List<GuruItem>>() {
//            @Override
//            public void onResponse(Call<List<GuruItem>> call, Response<List<GuruItem>> response) {
//                if (response.isSuccessful()) {
//                    GuruItem item = response.body().get(0);
//
////                    Picasso.get().load(item.getFoto()).into(profil);
//                    id_guru.setText(item.getId_guru());
//                    nama.setText(item.getNama());
//                    alamat.setText(item.getAlamat());
//                    jenis_kelamin.setText(item.getJenis_kelamin());
//                    no_telp.setText(item.getNo_telp());
//                    username.setText(item.getUsername());
//                    password.setText(item.getPassword());
//                } else {
//                    Toast.makeText(getApplicationContext(), "Gagal menampilkan data", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<GuruItem>> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

        Call<List<AbsenItem>> call1 = apiInterface.getAbsenByUsername(session.getUsername());

        call1.enqueue(new Callback<List<AbsenItem>>() {
            @Override
            public void onResponse(Call<List<AbsenItem>> call, Response<List<AbsenItem>> response) {
                String x = getIntent().getStringExtra("nama");
                String y = getIntent().getStringExtra("alamat");
                String z = getIntent().getStringExtra("nim");
                if (response.isSuccessful()) {
                    List<AbsenItem> absenItems = response.body();

                    for (AbsenItem item : absenItems) {
                        absen.add(new AbsenItem(item.getUsername(), item.getPassword(), item.getJam_login(),
                                item.getJam_logout(), item.getTanggal(), item.getLokasi_latitude(), item.getLokasi_longitude(),
                                item.getNama(), item.getAlamat(), item.getNim()));
                    }

                    absen.add(new AbsenItem(session.getUsername(), session.getPassword(), session.getLoginTime(),
                            session.getLogoutTime(), session.getDate(), session.getLocLatitude(), session.getLocLongitude(),
                            x,y,z));

                    itemAdapter.add(absen);
                    absenView.setAdapter(fastAdapter);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    absenView.setLayoutManager(layoutManager);
                } else {
                    Toast.makeText(getApplicationContext(), "Data gagal ditampilkan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AbsenItem>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onClickLogout(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GuruActivity.this);
        builder.setCancelable(false);
        builder.setMessage("Apakah kamu ingin logout?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                session.setLogoutTime(currentTime);

                String username = session.getUsername();
                String password = session.getPassword();
                String jam_login = session.getLoginTime();
                String jam_logout = session.getLogoutTime();
                String tanggal = session.getDate();
                double lokasi_latitude = session.getLocLatitude();
                double lokasi_longitude = session.getLocLongitude();
                String namasiswa = getIntent().getStringExtra("nama");
                String alamatsiswa = getIntent().getStringExtra("alamat");
                String nimsiswa = getIntent().getStringExtra("nim");

                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

                Call<ResponseBody> call = apiInterface.absenGuru(new AbsenItem(username, password, jam_login, jam_logout, tanggal,
                        lokasi_latitude, lokasi_longitude,
                        namasiswa , alamatsiswa , nimsiswa));

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            session.logout();
                            Toast.makeText(getApplicationContext(), "Logout Berhasil", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginGuruActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Logout Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}