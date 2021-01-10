package dresta.putra.artakita.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.razir.progressbutton.DrawableButtonExtensionsKt;
import com.github.razir.progressbutton.ProgressParams;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import dresta.putra.artakita.ResponsePojo;
import dresta.putra.artakita.daftar.DaftarActivity;
import dresta.putra.artakita.MainActivity;
import dresta.putra.artakita.PrefManager;
import dresta.putra.artakita.R;
import dresta.putra.artakita.RetrofitClientInstance;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public class LoginActivity extends AppCompatActivity {
    EditText username_input,password_input,EtEmail;
    Button BtnLogin,BtnClose,BtnKirim;
    Vibrator v;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;
    private PrefManager prefManager;
    private TextView TxvForgetPass;



    interface MyAPIService {
        @FormUrlEncoded
        @POST("api/login/kolektor")
        Call<LoginResponsePojo> getDatapengguna(@Field("username") String username, @Field("password") String password);
        @FormUrlEncoded
        @POST("api/login/forget_password")
        Call<ResponsePojo> forgetPassword(@Field("email") String email);
    }
    private MyAPIService myAPIService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageView IvBack = findViewById(R.id.IvBack);
        myAPIService = RetrofitClientInstance.getRetrofitInstance(LoginActivity.this).create(MyAPIService.class);
        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bottom_sheet = findViewById(R.id.bottom_sheet);
        EtEmail = findViewById(R.id.EtEmail);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        TxvForgetPass = findViewById(R.id.TxvForgetPass);
        BtnClose = findViewById(R.id.BtnClose);
        BtnKirim = findViewById(R.id.BtnKirim);

        username_input = findViewById(R.id.userName);
        password_input = findViewById(R.id.loginPassword);
        BtnLogin = findViewById(R.id.BtnLogin);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        prefManager = new PrefManager(this);
        Boolean loggedInIdMahasiswa= prefManager.isKolektorLoggedIn();
        if (loggedInIdMahasiswa) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUserData();
            }
        });
        TxvForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        BtnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirim(BtnKirim);
            }
        });
        BtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });



    }
    private void kirim(final Button button){
        DrawableButtonExtensionsKt.showProgress(button, new Function1<ProgressParams, Unit>() {
            @Override
            public Unit invoke(ProgressParams progressParams) {
                progressParams.setButtonTextRes(R.string.loading);
                progressParams.setProgressColor(Color.BLACK);
                return Unit.INSTANCE;
            }
        });
        String EtEmails = EtEmail.getText().toString();
        button.setEnabled(false);
        if (TextUtils.isEmpty(EtEmails)) {
            EtEmail.setError("Email masih kosong");
            EtEmail.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "KIRIM");
            return;
        }
        if (!TextUtils.isEmpty(EtEmails) && !Patterns.EMAIL_ADDRESS.matcher(EtEmails).matches()) {
            EtEmail.setError("Format email tidak sesuai");
            EtEmail.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "KIRIM");
            return;
        }
        Call<ResponsePojo> responsePojoCall = myAPIService.forgetPassword(EtEmails);
        responsePojoCall.enqueue(new Callback<ResponsePojo>() {
            @Override
            public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                if (response.body()!=null){
                    if (response.body().getStatus()==200){
                        button.setEnabled(true);
                        DrawableButtonExtensionsKt.hideProgress(button, "KIRIM");
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        Toast.makeText(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }else{
                        button.setEnabled(true);
                        DrawableButtonExtensionsKt.hideProgress(button, "KIRIM");
                        Toast.makeText(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePojo> call, Throwable t) {
                button.setEnabled(true);
                DrawableButtonExtensionsKt.hideProgress(button, "KIRIM");
                Toast.makeText(LoginActivity.this, "Terjadi gangguan jaringan, coba lagi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void validateUserData() {
        //fflagirst getting the values
        final String username= username_input.getText().toString();
        final String password = password_input.getText().toString();

        //checking if username is empty
        if (TextUtils.isEmpty(username)) {
            username_input.setError("Username masih kosong");
            username_input.requestFocus();
            // Vibrate for 100 milliseconds
            v.vibrate(100);
            BtnLogin.setEnabled(true);
            return;
        }
        //checking if password is empty
        if (TextUtils.isEmpty(password)) {
            password_input.setError("Password masih kosong");
            password_input.requestFocus();
            //Vibrate for 100 milliseconds
            v.vibrate(100);
            BtnLogin.setEnabled(true);
            return;
        }



        //Login User if everything is fine
        loginUser(username,password);


    }

    private void loginUser(String username, final String password) {
        //making api call

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(LoginActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setTitle("Login");
        progressDoalog.setMessage("Login....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
        progressDoalog.show();

        Call<LoginResponsePojo> call = myAPIService.getDatapengguna(username,password);

        call.enqueue(new Callback<LoginResponsePojo>() {

            @Override
            public void onResponse(Call<LoginResponsePojo> call, Response<LoginResponsePojo> response) {

                if (response.body() != null) {
                    progressDoalog.dismiss();
                    if(response.body().getStatus()==200){
                        set_sess(response.body().getData().getId_kolektor(),response.body().getData().getUsername(),password);
                        Intent IHome = new Intent(LoginActivity.this,MainActivity.class);
                        finish();
                        startActivity(IHome);
                    }else{
                        Toast.makeText(LoginActivity.this,response.body().getMsg(),Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<LoginResponsePojo> call, Throwable throwable) {
                progressDoalog.dismiss();
                Toast.makeText(LoginActivity.this, "Gagal Login, Coba Lagi", Toast.LENGTH_LONG).show();
            }
        });

    }
    public void set_sess(String id_kolektor,String username,String password){
        prefManager.storeDataKolektor(id_kolektor,username,password);
    }




}
