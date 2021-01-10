package dresta.putra.artakita;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.Objects;

import dresta.putra.artakita.angsuran.BayarAngsuranActivity;
import dresta.putra.artakita.daftar.DaftarActivity;
import dresta.putra.artakita.informasi_program.InformasiProgramActivity;
import dresta.putra.artakita.jadwal.JadwalActivity;
import dresta.putra.artakita.login.LoginActivity;
import dresta.putra.artakita.nasabah.NasabahActivity;
import dresta.putra.artakita.oper_berkas.OperBerkasNasabahActivity;
import dresta.putra.artakita.paket.AdapterPaket;
import dresta.putra.artakita.paket.PaketPojo;
import dresta.putra.artakita.paket.PaketPojoResponse;
import dresta.putra.artakita.pengaduan.PengaduanActivity;
import dresta.putra.artakita.setoran.LihatSetoranActivity;
import dresta.putra.artakita.simulasi.SimulasiActivity;
import dresta.putra.artakita.slider.AdapterSlider;
import dresta.putra.artakita.slider.SliderPojoResponse;
import dresta.putra.artakita.transaksi.TransaksiActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class HomeFragment extends Fragment {
    private ViewPager viewPager,viewPagerPaket;
    private AdapterSlider adapter;
    private AdapterPaket adapterPaket;
    private PrefManager prefManager;
    private ShimmerFrameLayout mShimmerViewContainerPaket,mShimmerViewContainerSlider;
    private LinearLayout LlNasabah,LlOperBerkas,LlInformasiProgram,LlSimulasi,LlPengaduan,LlJadwal,LlAngsur,LlTransaksi,LlLihatSetoran;

    public HomeFragment() {
        // Required empty public constructor
    }
    interface APIHomeFragment{
        @FormUrlEncoded
        @POST("api/slider/data_slider")
        Call<SliderPojoResponse> getSlider(@Field("page") int page, @Field("perPage") int perPage);
        @FormUrlEncoded
        @POST("api/paket/data_paket")
        Call<PaketPojoResponse> getPaket(@Field("page") int page, @Field("perPage") int perPage);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        Button BtnDaftar = view.findViewById(R.id.BtnDaftar);
        Button BtnLogin = view.findViewById(R.id.BtnLogin);
        TextView TxvUsername = view.findViewById(R.id.TxvUsername);
        viewPager = view.findViewById(R.id.viewPager);
        viewPagerPaket = view.findViewById(R.id.viewPagerPaket);
        prefManager = new PrefManager(getContext());
        LlNasabah = view.findViewById(R.id.LlNasabah);
        LlOperBerkas = view.findViewById(R.id.LlOperBerkas);
        LlInformasiProgram = view.findViewById(R.id.LlInformasiProgram);
        LlSimulasi = view.findViewById(R.id.LlSimulasi);
        LlPengaduan = view.findViewById(R.id.LlPengaduan);
        LlJadwal = view.findViewById(R.id.LlJadwal);
        LlAngsur = view.findViewById(R.id.LlAngsur);
        LlTransaksi = view.findViewById(R.id.LlTransaksi);
        LlLihatSetoran = view.findViewById(R.id.LlLihatSetoran);
        mShimmerViewContainerPaket = view.findViewById(R.id.shimmer_card_view_paket);
        mShimmerViewContainerSlider = view.findViewById(R.id.shimmer_card_view_slider);
        mShimmerViewContainerPaket.startShimmerAnimation();
        mShimmerViewContainerSlider.startShimmerAnimation();

        LlNasabah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefManager.isKolektorLoggedIn()){
                    Intent INasabah = new Intent(getContext(), NasabahActivity.class);
                    startActivity(INasabah);
                }else{
                    Toast.makeText(getContext(), "Anda perlu login untuk mengakses fitur ini", Toast.LENGTH_SHORT).show();
                }
            }
        });
        LlOperBerkas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefManager.isKolektorLoggedIn()){
                    Intent IOperBerkas = new Intent(getContext(), OperBerkasNasabahActivity.class);
                    startActivity(IOperBerkas);
                }else{
                    Toast.makeText(getContext(), "Anda perlu login untuk mengakses fitur ini", Toast.LENGTH_SHORT).show();
                }
            }
        });
        LlInformasiProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent IOperBerkas = new Intent(getContext(), InformasiProgramActivity.class);
                    startActivity(IOperBerkas);
            }
        });
        LlSimulasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ISimulasi = new Intent(getContext(), SimulasiActivity.class);
                startActivity(ISimulasi);
            }
        });
        LlPengaduan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ISimulasi = new Intent(getContext(), PengaduanActivity.class);
                startActivity(ISimulasi);
            }
        });
        LlJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IJadwal = new Intent(getContext(), JadwalActivity.class);
                startActivity(IJadwal);
            }
        });
        LlAngsur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefManager.isKolektorLoggedIn()) {
                    Intent IAngsur = new Intent(getContext(), BayarAngsuranActivity.class);
                    startActivity(IAngsur);
                }else{
                    Toast.makeText(getContext(), "Anda perlu login untuk mengakses fitur ini", Toast.LENGTH_SHORT).show();
                }
            }
        });
        LlTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefManager.isKolektorLoggedIn()) {
                    Intent ITransaksi = new Intent(getContext(), TransaksiActivity.class);
                    startActivity(ITransaksi);
                }else{
                    Toast.makeText(getContext(), "Anda perlu login untuk mengakses fitur ini", Toast.LENGTH_SHORT).show();
                }
            }
        });
        LlLihatSetoran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefManager.isKolektorLoggedIn()) {
                    Intent ILihatSetoran = new Intent(getContext(), LihatSetoranActivity.class);
                    startActivity(ILihatSetoran);
                }else{
                    Toast.makeText(getContext(), "Anda perlu login untuk mengakses fitur ini", Toast.LENGTH_SHORT).show();
                }
            }
        });



        if (prefManager.isKolektorLoggedIn()){
            BtnLogin.setVisibility(View.GONE);
            BtnDaftar.setVisibility(View.GONE);
            TxvUsername.setText("Halo "+prefManager.LoggedInKolektorUsername());
        }
        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Ilogin = new Intent(getContext(), LoginActivity.class);
                startActivity(Ilogin);
            }
        });
        BtnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IDaftar = new Intent(getContext(), DaftarActivity.class);
                startActivity(IDaftar);
            }
        });

        APIHomeFragment apiHomeFragment = RetrofitClientInstance.getRetrofitInstance(getContext()).create(APIHomeFragment.class);
        Call<SliderPojoResponse> sliderPojoResponseCall = apiHomeFragment.getSlider(0,5);
        mShimmerViewContainerSlider.setVisibility(View.VISIBLE);
        mShimmerViewContainerSlider.startShimmerAnimation();
        sliderPojoResponseCall.enqueue(new Callback<SliderPojoResponse>() {
            @Override
            public void onResponse(Call<SliderPojoResponse> call, final Response<SliderPojoResponse> response) {
                if (Objects.requireNonNull(response.body()).getStatus()==200){
                    adapter = new AdapterSlider(response.body().getData(), Objects.requireNonNull(getActivity()).getApplicationContext());
                    viewPager.setAdapter(adapter);
                    viewPager.setPadding(0, 0, 30, 0);
                }
                mShimmerViewContainerSlider.stopShimmerAnimation();
                mShimmerViewContainerSlider.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<SliderPojoResponse> call, Throwable t) {
                mShimmerViewContainerSlider.stopShimmerAnimation();
                mShimmerViewContainerSlider.setVisibility(View.GONE);
            }
        });
        Call<PaketPojoResponse> paketPojoResponseCall = apiHomeFragment.getPaket(0,3);
        mShimmerViewContainerPaket.setVisibility(View.VISIBLE);
        mShimmerViewContainerPaket.startShimmerAnimation();
        paketPojoResponseCall.enqueue(new Callback<PaketPojoResponse>() {
            @Override
            public void onResponse(Call<PaketPojoResponse> call, Response<PaketPojoResponse> response) {
                if (response.body()!=null){
                    if (response.body().getStatus()==200){
                        adapterPaket = new AdapterPaket(response.body().getData(), Objects.requireNonNull(getContext()));
                        viewPagerPaket.setAdapter(adapterPaket);
                        viewPagerPaket.setPadding(0, 0, 30, 0);
                    }
                }
                mShimmerViewContainerPaket.stopShimmerAnimation();
                mShimmerViewContainerPaket.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PaketPojoResponse> call, Throwable t) {
                mShimmerViewContainerPaket.stopShimmerAnimation();
                mShimmerViewContainerPaket.setVisibility(View.GONE);
            }
        });

//        slider
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        new Handler().postDelayed(new Runnable() {
//            @Override public void run() {
//                int page = viewPager.getCurrentItem();
//                if (adapter.getCount() == page+1) {
//                    page = 0;
//                }else {
//                    page++;
//                }
//                viewPager.setCurrentItem(page, true);
//            }
//        }, 5000);


        return view;
    }


}
