package id.ac.polinema.absensiprojectuas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import id.ac.polinema.absensiprojectuas.helper.Session;
import id.ac.polinema.absensiprojectuas.models.User;
import id.ac.polinema.absensiprojectuas.restapi.ApiClient;
import id.ac.polinema.absensiprojectuas.restapi.ApiInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginAdminActivity extends AppCompatActivity {
    private EditText inputUsername, inputPassword;
    private TextView result;
    private Button loginButton;
    private ConstraintLayout loginForm;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        session = new Session(getApplicationContext());
        inputUsername = findViewById(R.id.edt_username_admin);
        inputPassword = findViewById(R.id.edt_password_admin);
        result = findViewById(R.id.txt_login_guru);
        loginButton = findViewById(R.id.btn_login_admin);
        loginForm = findViewById(R.id.login_admin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin(inputUsername.getText().toString(), inputPassword.getText().toString());
            }
        });

        if (ContextCompat.checkSelfPermission(LoginAdminActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginAdminActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(LoginAdminActivity.this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else{
                ActivityCompat.requestPermissions(LoginAdminActivity.this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertMessageNoGps();
            }
        }
    private void AlertMessageNoGps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("YOUR GPS SEEMS TO DISABLED , DO YOU WANT TO ENABLE IT ?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);
        switch (requestCode){
            case 1: {
                if (grantResult.length>0 && grantResult[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(LoginAdminActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "PERMISSION GRANTED!", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(this, "PERMISSION DENIED!!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void userLogin(String username, String password) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.loginAdmin(new User(username, password));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray json = new JSONArray(response.body().string());
                        String username = json.getJSONObject(0).getString("username");
                        String password = json.getJSONObject(0).getString("password");

                        session.setLoggedInStatus(true);
                        session.setUsername(username);
                        session.setPassword(password);

                        Intent intent = new Intent(getApplicationContext(), OpeningActivity.class);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Credentials are not Valid.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void switchLoginGuru(View view) {
        Intent intent = new Intent(LoginAdminActivity.this, LoginGuruActivity.class);
        startActivity(intent);
    }
}
