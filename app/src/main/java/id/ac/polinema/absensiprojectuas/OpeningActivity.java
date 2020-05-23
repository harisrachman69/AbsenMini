package id.ac.polinema.absensiprojectuas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OpeningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
    }

    public void keAdmin (View view) {
        Intent i = new Intent(OpeningActivity.this, AdminActivity.class);
        startActivity(i);
    }

    public void keSiswa (View view) {
        Intent i = new Intent(OpeningActivity.this, SiswaActivity.class);
        startActivity(i);
    }

    public void terserah(View view) {
        Intent i = new Intent (OpeningActivity.this, RekapanActivity.class);
        startActivity(i);
    }
}
