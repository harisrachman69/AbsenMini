package id.ac.polinema.absensiprojectuas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import id.ac.polinema.absensiprojectuas.restapi.ApiClient;
import id.ac.polinema.absensiprojectuas.restapi.ApiInterface;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormActivity extends AppCompatActivity {
    private Button btnTambah, btnUpload;
    private EditText inputId, inputNama, inputAlamat, inputTelp, inputUsername, inputPassword;
    private RadioGroup radioGroup;
    private RadioButton selected;
    private ImageView fotoProfil;
    private Bitmap foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        btnTambah = findViewById(R.id.btn_tambah_data_guru);
        btnUpload = findViewById(R.id.btn_upload_foto);
        fotoProfil = findViewById(R.id.img_foto);
//        inputId = findViewById(R.id.edt_id);
        inputNama = findViewById(R.id.edt_nama);
        inputAlamat = findViewById(R.id.edt_alamat);
        radioGroup = findViewById(R.id.group_jk);
        inputTelp = findViewById(R.id.edt_telp);
        inputUsername = findViewById(R.id.edt_username);
        inputPassword = findViewById(R.id.edt_password);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission();
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahData();
            }
        });
    }

    private void selectImage() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 1);
    }

    private void requestCameraPermission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        selectImage();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if(response.isPermanentlyDenied())
                            showDialog();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FormActivity.this);
        builder.setTitle("Need Permission");
        builder.setMessage("IZINKAN SAYA MENGGUNAKAN KAMERA");
        builder.setPositiveButton("IZINKAN DONG", new DialogInterface.OnClickListener() {
            @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                selectImage();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            foto = photo;
            fotoProfil.setImageBitmap(foto);
        }
    }

    private File createTempFile(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() +"_image.jpeg");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG,85,stream);

        byte[] bitmapdata = stream.toByteArray();

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private void tambahData() {
//        String id_guru = inputId.getText().toString();
        String nama = inputNama.getText().toString();
        String alamat = inputAlamat.getText().toString();
        selected = findViewById(radioGroup.getCheckedRadioButtonId());
        String jenis_kelamin = "";
        if (selected != null) {
            jenis_kelamin = selected.getText().toString();
        }
        String no_telp = inputTelp.getText().toString();
        String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();

        HashMap<String, RequestBody> map = new HashMap<>();
//        map.put("id_guru", createPartFromString(id_guru));
        map.put("nama", createPartFromString(nama));
        map.put("alamat", createPartFromString(alamat));
        map.put("jenis_kelamin", createPartFromString(jenis_kelamin));
        map.put("no_telp", createPartFromString(no_telp));
        map.put("username", createPartFromString(username));
        map.put("password", createPartFromString(password));

        File file = createTempFile(foto);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("foto", file.getName(), reqFile);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.tambahGuru(body, map);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private RequestBody createPartFromString(String description) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, description);
    }
}
