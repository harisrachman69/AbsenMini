package id.ac.polinema.absensiprojectuas.models;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import id.ac.polinema.absensiprojectuas.R;

public class RekapItem extends AbstractItem<RekapItem, RekapItem.ViewHolder> {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRekap() {
        return rekap;
    }

    public void setRekap(String rekap) {
        this.rekap = rekap;
    }

    private String username;
    private String password;
    private String rekap;

    public RekapItem(String username, String password , String rekap) {
        this.username = username;
        this.password = password;
        this.rekap = rekap;
    }


    @NonNull
    @Override
    public RekapItem.ViewHolder getViewHolder(View v) { return new ViewHolder(v); }

    @Override
    public int getType() { return R.id.rv_rekap; }

    @Override
    public int getLayoutRes() { return R.layout.rekap_item; }

    public class ViewHolder extends FastAdapter.ViewHolder<RekapItem> {
        TextView username, password , rekap;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.Username_rekapitem);
            password = itemView.findViewById(R.id.Password_rekapitem);
            rekap = itemView.findViewById(R.id.Jumlah_rekapitem);
        }

        @Override
        public void bindView(RekapItem item, List<Object> payloads) {
            username.setText("Username : " + item.username);
            password.setText("Password : "+ item.password);
            rekap.setText("Jumlah Rekap : "+ item.rekap);

        }

        @Override
        public void unbindView(RekapItem item) {
            username.setText(null);
            password.setText(null);
            rekap.setText(null);
        }
    }
}
