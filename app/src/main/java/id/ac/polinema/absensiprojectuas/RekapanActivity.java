package id.ac.polinema.absensiprojectuas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.ArrayList;
import java.util.List;

import id.ac.polinema.absensiprojectuas.helper.Session;
import id.ac.polinema.absensiprojectuas.models.RekapItem;
import id.ac.polinema.absensiprojectuas.restapi.ApiClient;
import id.ac.polinema.absensiprojectuas.restapi.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RekapanActivity extends AppCompatActivity {
    TextView rekap;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekapan);

        session = new Session(getApplicationContext());

        final RecyclerView rekapView = findViewById(R.id.rv_rekap);
        final ItemAdapter itemAdapter = new ItemAdapter<>();
        final FastAdapter fastAdapter = FastAdapter.with(itemAdapter);
        final List rekap = new ArrayList<>();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<RekapItem>> call = apiInterface.listRekap();

        call.enqueue(new Callback<List<RekapItem>>() {
            @Override
            public void onResponse(Call<List<RekapItem>> call, Response<List<RekapItem>> response) {
                List<RekapItem> RekapItems = response.body();
                if (response.isSuccessful()) {
                    for (RekapItem item : RekapItems) {
                        rekap.add(new RekapItem(item.getUsername(), item.getPassword(),item.getRekap()));
                    }

                    itemAdapter.add(rekap);
                    rekapView.setAdapter(fastAdapter);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    rekapView.setLayoutManager(layoutManager);
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal menampilkan data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RekapItem>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

        public void handleLogoutRekap(View view) {
            Intent i = new Intent(RekapanActivity.this, LoginAdminActivity.class);
            startActivity(i);
        }

    public void handlebalikRekapan (View view) {
        Intent i = new Intent ( RekapanActivity.this, OpeningActivity.class);
        startActivity(i);
    }

}
