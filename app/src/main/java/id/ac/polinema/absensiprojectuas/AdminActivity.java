package id.ac.polinema.absensiprojectuas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import id.ac.polinema.absensiprojectuas.models.GuruItem;
import id.ac.polinema.absensiprojectuas.restapi.ApiClient;
import id.ac.polinema.absensiprojectuas.restapi.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {
    private Session session;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        session = new Session(getApplicationContext());

        final RecyclerView guruView = findViewById(R.id.rv_guru);
        final ItemAdapter itemAdapter = new ItemAdapter<>();
        final FastAdapter fastAdapter = FastAdapter.with(itemAdapter);

        final List guru = new ArrayList<>();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<GuruItem>> call = apiInterface.getGuru();

        call.enqueue(new Callback<List<GuruItem>>() {
            @Override
            public void onResponse(Call<List<GuruItem>> call, Response<List<GuruItem>> response) {
                List<GuruItem> guruItems = response.body();

                for (GuruItem item : guruItems) {
                    guru.add(new GuruItem(item.getId_guru(), item.getNama(), item.getAlamat(), item.getJenis_kelamin(),
                            item.getNo_telp(), item.getFoto(), item.getUsername(), item.getPassword()));
                }

                itemAdapter.add(guru);
                guruView.setAdapter(fastAdapter);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                guruView.setLayoutManager(layoutManager);
            }

            @Override
            public void onFailure(Call<List<GuruItem>> call, Throwable t) {
                error.setText(t.getMessage());
            }
        });
    }

    public void myOnClickAddSiswa(View view) {
        Intent intent = new Intent(getApplicationContext(), FormSiswaActivity.class);
        startActivity(intent);
    }

    public void myOnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), FormActivity.class);
        startActivity(intent);
    }

    public void myOnClickLogout(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
        builder.setCancelable(false);
        builder.setMessage("Apakah kamu ingin logout?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                session.logout();
                Toast.makeText(getApplicationContext(), "Logout Berhasil", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginAdminActivity.class);
                startActivity(intent);
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