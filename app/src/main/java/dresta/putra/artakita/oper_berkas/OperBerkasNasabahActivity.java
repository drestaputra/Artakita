package dresta.putra.artakita.oper_berkas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dresta.putra.artakita.PrefManager;
import dresta.putra.artakita.R;
import dresta.putra.artakita.RetrofitClientInstance;
import dresta.putra.artakita.nasabah.NasabahPojo;
import dresta.putra.artakita.nasabah.NasabahResponsePojo;
import dresta.putra.artakita.nasabah.PaginationAdapterNasabah;
import dresta.putra.artakita.pinjaman.PinjamanPojo;
import dresta.putra.artakita.pinjaman.PinjamanResponsePojo;
import dresta.putra.artakita.utils.PaginationScrollListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public class OperBerkasNasabahActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int PER_PAGE=20;
    private int currentPage = PAGE_START;
    private SearchView mSearchView;
    private String query_pencarian="",id_kategori_buku;
    private PaginationAdapterNasabah adapter;
    private GridLayoutManager linearLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;
    private String nama_nasabah,no_nasabah,username;
    private PrefManager prefManager;

    interface APINasabah{
        @FormUrlEncoded
        @POST("api/nasabah/data_nasabah")
        Call<NasabahResponsePojo> getDataNasabah(
                @Field("nama_nasabah") String nama_nasabah,
                @Field("no_nasabah") String no_nasabah,
                @Field("username") String username,
                @Field("pencarian") String pencarian,
                @Field("page") int page,
                @Field("perPage") int perPage
        );
    }
    private APINasabah ServicePojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oper_berkas_nasabah);
        progressBar = findViewById(R.id.main_progress);
        mSearchView = findViewById(R.id.mSearchView);
        mShimmerViewContainer = findViewById(R.id.shimmer_card_view);
        mShimmerViewContainer.startShimmerAnimation();
        RecyclerView rv = findViewById(R.id.RvNasabah);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        id_kategori_nasabah = getIntent().getStringExtra("id_kategori_nasabah");
        prefManager = new PrefManager(this);

//        filter tambahan
        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        ImageButton ImBFilter = findViewById(R.id.ImBFilter);
        Button BtnClose = findViewById(R.id.BtnClose);
        BtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        ImageView IvRiwayat = findViewById(R.id.IvRiwayat);
        IvRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Iriwayat = new Intent(OperBerkasNasabahActivity.this,RiwayatOperBerkasActivity.class);
                startActivity(Iriwayat);
                overridePendingTransition(0, 0);
            }
        });


        //init service and load data
        ServicePojo = RetrofitClientInstance.getRetrofitInstance(OperBerkasNasabahActivity.this).create(APINasabah.class);
        loadFirstPage();
        adapter = new PaginationAdapterNasabah(OperBerkasNasabahActivity.this);
        linearLayoutManager = new GridLayoutManager(this,2);
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
                query_pencarian=query;
                init_pencarian();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {

                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
                query_pencarian=query;
                init_pencarian();
                return false;
            }
        });
    }
    private void init_pencarian(){
        currentPage=0;
        isLastPage = false;
        NasabahResponsePojoCall().enqueue(new Callback<NasabahResponsePojo>() {
            @Override
            public void onResponse(Call<NasabahResponsePojo> call, Response<NasabahResponsePojo> response) {

                if (response.body() != null) {
                    if(response.body().getStatus()==200 && response.body().getTotalRecords()!=0) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        adapter.clear();
                        List<NasabahPojo> results = fetchResults(response);
                        adapter.addAll(results);
                        TOTAL_PAGES=response.body().getTotalPage();
                        if (currentPage <= TOTAL_PAGES-1) {
                            adapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                        }
                    }else{
                        adapter.clear();
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NasabahResponsePojo> call, @NonNull Throwable t) {
                List<NasabahPojo> nasabahPojos = prefManager.getNasabahPojos("","","",query_pencarian,0,99999);
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                if (nasabahPojos.size()!=0){
                    PER_PAGE=99999;
                    adapter.clear();
                    adapter.addAll(nasabahPojos);
                    TOTAL_PAGES=0;

                    isLastPage = true;
                }else{
                    t.printStackTrace();
                    adapter.clear();
                    Toast.makeText(OperBerkasNasabahActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
    private void loadFirstPage() {
        NasabahResponsePojoCall().enqueue(new Callback<NasabahResponsePojo>() {
            @Override
            public void onResponse(Call<NasabahResponsePojo> call, Response<NasabahResponsePojo> response) {
                // Got data. Send it to adapter
                assert response.body() != null;
                if(response.body().getStatus()==200 && response.body().getTotalRecords()!=0) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    List<NasabahPojo> results = fetchResults(response);
                    progressBar.setVisibility(View.GONE);
                    adapter.addAll(results);
                    TOTAL_PAGES=response.body().getTotalPage();
//
//
                    if (currentPage <= TOTAL_PAGES-1) {
                        adapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                }else{
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    Toast.makeText(OperBerkasNasabahActivity.this, "Belum ada Data untuk saat ini", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<NasabahResponsePojo> call, Throwable t) {
                List<NasabahPojo> nasabahPojos = prefManager.getNasabahPojos("","","",query_pencarian,0,99999);
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                if (nasabahPojos.size()!=0){
                    PER_PAGE=99999;
                    adapter.clear();
                    adapter.addAll(nasabahPojos);
                    TOTAL_PAGES=0;

                    isLastPage = true;
                }else{
                    t.printStackTrace();
                    adapter.clear();
                    Toast.makeText(OperBerkasNasabahActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private void loadNextPage() {
        NasabahResponsePojoCall().enqueue(new Callback<NasabahResponsePojo>() {
            @Override
            public void onResponse(Call<NasabahResponsePojo> call, Response<NasabahResponsePojo> response) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                adapter.removeLoadingFooter();
                isLoading = false;

                List<NasabahPojo> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<NasabahResponsePojo> call, Throwable t) {
                List<NasabahPojo> nasabahPojos = prefManager.getNasabahPojos("","","",query_pencarian,0,99999);
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                if (nasabahPojos.size()!=0){
                    PER_PAGE=99999;
                    adapter.clear();
                    adapter.addAll(nasabahPojos);
                    TOTAL_PAGES=0;

                    isLastPage = true;
                }else{
                    t.printStackTrace();
                    adapter.clear();
                    Toast.makeText(OperBerkasNasabahActivity.this, "Belum ada data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * @param response extracts List<{@link NasabahPojo>} from response
     * @return
     */
    private List<NasabahPojo> fetchResults(Response<NasabahResponsePojo> response) {
        NasabahResponsePojo topRatedMovies = response.body();
        return topRatedMovies.getData();
    }
    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    private Call<NasabahResponsePojo> NasabahResponsePojoCall() {
        return ServicePojo.getDataNasabah(
                nama_nasabah,
                no_nasabah,
                username,
                query_pencarian,
                currentPage,
                PER_PAGE
        );
    }
}
