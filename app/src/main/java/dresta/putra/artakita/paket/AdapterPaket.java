package dresta.putra.artakita.paket;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import dresta.putra.artakita.R;
import dresta.putra.artakita.WebActivity;

public class AdapterPaket extends PagerAdapter {

    private List<PaketPojo> models;
    private Context context;

    public AdapterPaket(List<PaketPojo> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.adapter_jenis_pembayaran, container, false);
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        TextView TxvNamaPaket, TxvDeskripsiPaket, TxvHargaPaket;

        TxvNamaPaket = view.findViewById(R.id.TxvNamaPaket);
        TxvDeskripsiPaket = view.findViewById(R.id.TxvDeskripsiPaket);
        TxvHargaPaket = view.findViewById(R.id.TxvHargaPaket);

        TxvNamaPaket.setText(models.get(position).getNama_paket());
        TxvDeskripsiPaket.setText(models.get(position).getDeskripsi_paket());
        TxvHargaPaket.setText(formatRupiah.format(Float.parseFloat(models.get(position).getHarga_paket())));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentl= new Intent(context.getApplicationContext(), RequestActivty.class);
                intentl.putExtra("id_paket",models.get(position).getId_paket());
                intentl.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentl);
            }
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
