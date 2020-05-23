package id.ac.polinema.absensiprojectuas.models;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import id.ac.polinema.absensiprojectuas.R;

public class GuruItem extends AbstractItem<GuruItem, GuruItem.ViewHolder> {
    private String id_guru;
    private String nama;
    private String alamat;
    private String jenis_kelamin;
    private String no_telp;
    private String foto;
    private String username;
    private String password;

    public GuruItem(String id_guru, String nama, String alamat, String jenis_kelamin, String no_telp, String foto, String username, String password) {
        this.id_guru = id_guru;
        this.nama = nama;
        this.alamat = alamat;
        this.jenis_kelamin = jenis_kelamin;
        this.no_telp = no_telp;
        this.foto = foto;
        this.username = username;
        this.password = password;
    }

    public String getId_guru() {
        return id_guru;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public String getFoto() {
        return foto;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @NonNull
    @Override
    public GuruItem.ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.rv_guru;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_guru;
    }

    public class ViewHolder extends FastAdapter.ViewHolder<GuruItem> {
        ImageView profil;
        TextView id_guru, nama, alamat, jenis_kelamin, no_telp, username, password;

        public ViewHolder(View itemView) {
            super(itemView);
            profil = itemView.findViewById(R.id.foto_profil);
            id_guru = itemView.findViewById(R.id.id_guru);
            nama = itemView.findViewById(R.id.nama_guru);
            alamat = itemView.findViewById(R.id.alamat_guru);
            jenis_kelamin = itemView.findViewById(R.id.jenis_kelamin_guru);
            no_telp = itemView.findViewById(R.id.telp_guru);
            username = itemView.findViewById(R.id.username_guru);
            password = itemView.findViewById(R.id.password_guru);
        }

        @Override
        public void bindView(GuruItem item, List<Object> payloads) {
            Picasso.get().load(item.getFoto()).into(profil);
            id_guru.setText(item.id_guru);
            nama.setText(item.nama);
            alamat.setText(item.alamat);
            jenis_kelamin.setText(item.jenis_kelamin);
            no_telp.setText(item.no_telp);
            username.setText(item.username);
            password.setText(item.password);
        }

        @Override
        public void unbindView(GuruItem item) {
            profil.setImageBitmap(null);
            id_guru.setText(null);
            nama.setText(null);
            alamat.setText(null);
            jenis_kelamin.setText(null);
            no_telp.setText(null);
            username.setText(null);
            password.setText(null);
        }
    }
}
