package id.ac.polinema.absensiprojectuas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;

import id.ac.polinema.absensiprojectuas.models.SiswaItem;
import id.ac.polinema.absensiprojectuas.restapi.ApiClient;
import id.ac.polinema.absensiprojectuas.restapi.ApiInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormSiswaActivity extends AppCompatActivity {
    private EditText inputNama, inputAlamat, inputTglLahir, inputKelas;
    private RadioGroup radioGroup;
    private RadioButton selected;
    private Button btnTambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_siswa);

        btnTambah = findViewById(R.id.btn_tambah_data_siswa);
        inputNama = findViewById(R.id.edt_nama_siswa);
        inputAlamat = findViewById(R.id.edt_alamat_siswa);
        inputKelas = findViewById(R.id.edt_kelas);
        radioGroup = findViewById(R.id.group_jk_siswa);
        inputTglLahir = findViewById(R.id.edt_tgl_lahir);
        inputTglLahir.setInputType(InputType.TYPE_NULL);
        inputTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog picker = new DatePickerDialog(FormSiswaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        inputTglLahir.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahData();
            }
        });
    }

    private void tambahData() {
        String nama = inputNama.getText().toString();
        String alamat = inputAlamat.getText().toString();
        selected = findViewById(radioGroup.getCheckedRadioButtonId());
        String jenis_kelamin = "";
        if (selected != null) {
            jenis_kelamin = selected.getText().toString();
        }
        String tanggal_lahir = inputTglLahir.getText().toString();
        String kelas = inputKelas.getText().toString();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.tambahSiswa(new SiswaItem(nama, alamat, jenis_kelamin, tanggal_lahir, kelas));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Berhasil Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), SiswaActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}

