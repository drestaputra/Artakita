package dresta.putra.artakita.paket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.razir.progressbutton.DrawableButtonExtensionsKt;
import com.github.razir.progressbutton.ProgressParams;

import java.util.Objects;

import dresta.putra.artakita.R;
import dresta.putra.artakita.ResponsePojo;
import dresta.putra.artakita.RetrofitClientInstance;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class RequestActivty extends AppCompatActivity {
    private Button BtnRequest;
    private EditText EtUsername,EtNoHp,EtPassword,EtConfPassword,EtEmail;
    private String id_paket;
    interface APIRequest{
        @FormUrlEncoded
        @POST("api/paket/request")
        Call<ResponsePojo> requestOwner(@Field("username") String username, @Field("email") String email, @Field("no_hp") String no_hp, @Field("password") String password, @Field("id_paket") String id_paket);
    }
    private APIRequest servicePojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        id_paket = getIntent().getStringExtra("id_paket");
        Button IvBack = findViewById(R.id.IvBack);
        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        servicePojo =  RetrofitClientInstance.getRetrofitInstance(RequestActivty.this).create(APIRequest.class);
        BtnRequest = findViewById(R.id.BtnRequest);
        EtPassword = findViewById(R.id.EtPassword);
        EtConfPassword = findViewById(R.id.EtConfPassword);
        EtUsername = findViewById(R.id.EtUsername);
        EtEmail = findViewById(R.id.EtEmail);
        EtNoHp = findViewById(R.id.EtNoHp);
        BtnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request(BtnRequest);
            }
        });
    }
    private void request(final Button button){
        DrawableButtonExtensionsKt.showProgress(button, new Function1<ProgressParams, Unit>() {
            @Override
            public Unit invoke(ProgressParams progressParams) {
                progressParams.setButtonTextRes(R.string.loading);
                progressParams.setProgressColor(Color.BLACK);
                return Unit.INSTANCE;
            }
        });
        button.setEnabled(false);
        final String EtPasswords = EtPassword.getText().toString();
        final String EtConfPasswords = EtConfPassword.getText().toString();
        final String EtUsernames = EtUsername.getText().toString();
        final String EtEmails = EtEmail.getText().toString();
        final String EtNoHps = EtNoHp.getText().toString();

        if (TextUtils.isEmpty(EtUsernames)) {
            EtUsername.setError("Username masih kosong");
            EtUsername.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Request");
            return;
        }
        if (TextUtils.isEmpty(EtEmails)) {
            EtEmail.setError("Email masih kosong");
            EtEmail.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Request");
            return;
        }
        if (TextUtils.isEmpty(EtNoHps)) {
            EtNoHp.setError("Nomor HP masih kosong");
            EtNoHp.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Request");
            return;
        }
        if (TextUtils.isEmpty(EtPasswords)) {
            EtPassword.setError("Password masih kosong");
            EtPassword.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Request");
            return;
        }
        if (TextUtils.isEmpty(EtConfPasswords)) {
            EtConfPassword.setError("Konfirmasi password masih kosong");
            EtConfPassword.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Request");
            return;
        }
        if (!EtPasswords.equals(EtConfPasswords)) {
            EtConfPassword.setError("Konfirmasi password tidak sama");
            EtConfPassword.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Request");
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<ResponsePojo> responsePojoCall = servicePojo.requestOwner(EtUsernames,EtEmails,EtNoHps,EtPasswords,id_paket);
                responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                    @Override
                    public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                        if (response.body()!=null){
                            if (response.body().getStatus() == 200){
                                button.setEnabled(true);
                                DrawableButtonExtensionsKt.hideProgress(button, "Request");
                                Toast.makeText(RequestActivty.this, "Permintaan berhasil, Anda akan segera kami hubungi", Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                button.setEnabled(true);
                                DrawableButtonExtensionsKt.hideProgress(button, "Request");
                                Toast.makeText(RequestActivty.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsePojo> call, Throwable t) {
                        button.setEnabled(true);
                        Toast.makeText(RequestActivty.this, "Gagal, coba lagi", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        },3000);
    }
}
