package bm.mobil.proje.penguen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CalisVeriAdapter extends RecyclerView.Adapter<CalisVeriAdapter.ViewHolder> {
    ArrayList<CalisVeri> arrayListCalisveri = new ArrayList<>();
    LayoutInflater layoutInflater;
    Context context;
    //Veriler CalisVeri Adapter Sınıfında doldurulacak context te çalışacak
    public CalisVeriAdapter(ArrayList<CalisVeri> arrayListCalisveri, Context context) {
        this.arrayListCalisveri = arrayListCalisveri;
        this.context = context;
    }

    //Her bir satır için temsil edilecek arayüz seçme işlemi yapılır.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.activity_calis1_satir,parent,false);
        ViewHolder vh = new ViewHolder(v); // v yi viewHolder a dönüştürdük
        return vh;
    }
    //her bir görünümün içeriğini belirtmeliyiz
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.btnTR.setText(arrayListCalisveri.get(position).getJ_kelimeTR());
        holder.btnEn.setText(arrayListCalisveri.get(position).getJ_kelimeEN());
        holder.calisanalayout.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return arrayListCalisveri.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        Button btnEn, btnTR;
        LinearLayout calisanalayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnEn=itemView.findViewById(R.id.calis1btnEn);
            btnTR=itemView.findViewById(R.id.calis1btnTR);


            calisanalayout=itemView.findViewById(R.id.calis1_satir_analinerlayout);
            btnEn.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    btnTR.setBackgroundColor(R.color.black);
                }
            });
            btnTR.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    btnEn.setBackgroundColor(R.color.white);
                }
            });
            calisanalayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toast=Toast.makeText(itemView.getContext(), "Bildiğiniz kelimeleri Sola Bilmediğiniz kelimeleri Sağa Sürükleyin", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            });
        }
    }
}