package bm.mobil.proje.penguen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SkorAdapter extends RecyclerView.Adapter<SkorAdapter.ViewHolder> {
    ArrayList<Kullanici> arrayListSkorVeri = new ArrayList<>();
    LayoutInflater layoutInflater;
    Context context;
    int i=1;
    //Veriler CalisVeri Adapter Sınıfında doldurulacak context te çalışacak
    public SkorAdapter(ArrayList<Kullanici> arrayListCalisveri, Context context) {
        this.arrayListSkorVeri = arrayListCalisveri;
        this.context = context;
    }
    @NonNull
    @Override
    public SkorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.activity_skor_satir,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SkorAdapter.ViewHolder holder, int position) {

        holder.skortxtisim.setText(arrayListSkorVeri.get(position).getJ_ad());
        holder.skortxtpuan.setText(Integer.toString(arrayListSkorVeri.get(position).getJ_skor()) );
        holder.skorlinerlayout.setTag(holder);

        holder.btnskorsirasi.setText(Integer.toString(i));
        i++;
    }

    @Override
    public int getItemCount() {
        return arrayListSkorVeri.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btnskorsirasi;
        TextView skortxtisim, skortxtpuan;
        LinearLayout skorlinerlayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnskorsirasi=itemView.findViewById(R.id.saltin);
            skortxtisim=itemView.findViewById(R.id.skullaniciAdi);
            skortxtpuan=itemView.findViewById(R.id.sskor);
            skorlinerlayout=itemView.findViewById(R.id.skor_ana_linerlayout);

        }
    }
}