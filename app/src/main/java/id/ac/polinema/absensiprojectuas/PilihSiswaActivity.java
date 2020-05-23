package id.ac.polinema.absensiprojectuas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.ArrayList;
import java.util.List;

import id.ac.polinema.absensiprojectuas.helper.Session;
import id.ac.polinema.absensiprojectuas.models.PilihSiswaItem;
import id.ac.polinema.absensiprojectuas.models.SiswaItem;
import id.ac.polinema.absensiprojectuas.restapi.ApiClient;
import id.ac.polinema.absensiprojectuas.restapi.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PilihSiswaActivity extends AppCompatActivity {
    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_siswa);

        session = new Session(getApplicationContext());

        final RecyclerView siswaView = findViewById(R.id.rv_siswaPilih);
        final ItemAdapter itemAdapter = new ItemAdapter<>();
        final FastAdapter fastAdapter = FastAdapter.with(itemAdapter);
        final List pilih = new ArrayList<>();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<PilihSiswaItem>> call = apiInterface.getSemuaSiswa();

        call.enqueue(new Callback<List<PilihSiswaItem>>() {
            @Override
            public void onResponse(Call<List<PilihSiswaItem>> call, Response<List<PilihSiswaItem>> response) {
                List<PilihSiswaItem> PilihSiswaItems = response.body();
                if (response.isSuccessful()) {
                    for (PilihSiswaItem item : PilihSiswaItems) {
                        pilih.add(new PilihSiswaItem(item.getNim(), item.getNama(), item.getAlamat(),
                                item.getJenis_kelamin(), item.getTanggal_lahir(), item.getKelas()));
                    }
                    itemAdapter.add(pilih);
                    siswaView.setAdapter(fastAdapter);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    siswaView.setLayoutManager(layoutManager);
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal menampilkan data", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<PilihSiswaItem>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//
//    public void handleLogoutSiswa(View view) {
//        Intent i = new Intent(PilihSiswaActivity.this, LoginAdminActivity.class);
//        startActivity(i);
//    }

//    public void handleTambahSiswa(View view) {
//        Intent i = new Intent ( SiswaActivity.this, TambahSiswa.class);
//        startActivity(i);
//    }

//    public void handlebalikSiswa (View view) {
//        Intent i = new Intent ( PilihSiswaActivity.this, OpeningActivity.class);
//        startActivity(i);
//    }


//    public void tambahsiswa(View view) {
//        Intent i = new Intent (PilihSiswaActivity.this, FormSiswaActivity.class);
//        startActivity(i);
//    }
}
