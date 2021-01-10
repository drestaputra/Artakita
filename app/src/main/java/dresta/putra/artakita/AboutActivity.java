package dresta.putra.artakita;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class AboutActivity extends AppCompatActivity {

    interface APIFragmentThree{
        @GET("api/about/index")
        Call<AboutPojo> getAbout();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        final TextView TxvAppName = findViewById(R.id.TxvAppName);
        final TextView TxvAppDescription = findViewById(R.id.TxvAppDescription);
        final TextView TxvAppDev = findViewById(R.id.TxvAppDev);
        final TextView TxvAppDevLink = findViewById(R.id.TxvAppDevLink);
        final TextView TxvAppAddress = findViewById(R.id.TxvAppAddress);
        final TextView TxvInstagram = findViewById(R.id.TxvInstagram);
        final TextView TxvFacebook = findViewById(R.id.TxvFacebook);
        final TextView TxvTwitter = findViewById(R.id.TxvTwitter);
        final TextView TxvWhatsapp = findViewById(R.id.TxvWhatsapp);
        final TextView TxvNoHp = findViewById(R.id.TxvNoHp);
        final TextView TxvEmail = findViewById(R.id.TxvEmail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        APIFragmentThree apiAbout = RetrofitClientInstance.getRetrofitInstance(this).create(APIFragmentThree.class);
        Call<AboutPojo> aboutPojoResponseCall = apiAbout.getAbout();
        aboutPojoResponseCall.enqueue(new Callback<AboutPojo>() {
            @Override
            public void onResponse(@NonNull Call<AboutPojo> call, @NonNull Response<AboutPojo> response) {
                if (response.body()!=null){

                    final AboutPojo configPojos= response.body();
                    TxvAppName.setText(Html.fromHtml(configPojos.getApp_names() != null ? configPojos.getApp_names() : ""));
                    TxvAppDescription.setText(Html.fromHtml(configPojos.getApp_description() != null ? configPojos.getApp_description() : ""));
                    TxvAppDev.setText(Html.fromHtml(configPojos.getApp_dev() != null ? configPojos.getApp_dev() : ""));
                    TxvAppDevLink.setText(Html.fromHtml(configPojos.getApp_dev_web() != null ? configPojos.getApp_dev_web() : ""));
                    TxvAppAddress.setText(Html.fromHtml(configPojos.getApp_contact_address() != null ? configPojos.getApp_contact_address() : ""));
                    TxvInstagram.setText(Html.fromHtml(configPojos.getApp_contact_ig() != null ? configPojos.getApp_contact_ig() : ""));
                    TxvFacebook.setText(Html.fromHtml(configPojos.getApp_contact_fb() != null ? configPojos.getApp_contact_fb() : ""));
                    TxvTwitter.setText(Html.fromHtml(configPojos.getApp_contact_twitter() != null ? configPojos.getApp_contact_twitter() : ""));
                    TxvWhatsapp.setText(Html.fromHtml(configPojos.getApp_contact_wa() != null ? configPojos.getApp_contact_wa() : ""));
                    TxvNoHp.setText(Html.fromHtml(configPojos.getApp_contact_phone() != null ? configPojos.getApp_contact_phone() : ""));
                    TxvEmail.setText(Html.fromHtml(configPojos.getApp_contact_mail() != null ? configPojos.getApp_contact_mail() : ""));
                    TxvAppDevLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intentl= new Intent(AboutActivity.this, WebActivity.class);
                            String url = configPojos.getApp_dev_web() != null ? configPojos.getApp_dev_web() : "";
                            intentl.putExtra("url", url);
                            startActivity(intentl);
                        }
                    });
                    LinearLayout LlTelp = findViewById(R.id.LlTelp);
                    LlTelp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            String no_telp = configPojos.getApp_contact_phone() != null ? configPojos.getApp_contact_phone() : "";
                            intent.setData(Uri.parse("tel:"+no_telp));
                            startActivity(intent);
                        }
                    });
                    LinearLayout LlFacebook = findViewById(R.id.LlFacebook);
                    LlFacebook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intentl= new Intent(AboutActivity.this, WebActivity.class);
                            String fb = configPojos.getApp_contact_fb() != null ? configPojos.getApp_contact_fb() : "";
                            intentl.putExtra("url","https://web.facebook.com/"+fb);
                            startActivity(intentl);
                        }
                    });
                    LinearLayout LlWhatsapp = findViewById(R.id.LlWhatsapp);
                    LlWhatsapp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String wa = configPojos.getApp_contact_wa() != null ? configPojos.getApp_contact_wa() : "";
                            String url = "https://api.whatsapp.com/send?phone="+wa;
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                    });
                    LinearLayout LlEmail = findViewById(R.id.LlEmail);
                    LlEmail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String email = configPojos.getApp_contact_mail() != null ? configPojos.getApp_contact_mail() : "";
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto",email, null));
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Informasi Koperasi");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Halo saya ingin tanya-tanya tentang koperasi ...");
                            startActivity(Intent.createChooser(emailIntent, "Send email..."));
                        }
                    });
                    LinearLayout LlInstagram = findViewById(R.id.LlInstagram);
                    LlInstagram.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String ig = configPojos.getApp_contact_ig() != null ? configPojos.getApp_contact_ig() : "";
                            Uri uri = Uri.parse("https://instagram.com/_u/"+ig);
                            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                            likeIng.setPackage("com.instagram.android");

                            try {
                                startActivity(likeIng);
                            } catch (ActivityNotFoundException e) {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://instagram.com/"+ig)));
                            }
                        }
                    });
                    LinearLayout LlTwitter = findViewById(R.id.LlTwitter);
                    LlTwitter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = null;
                            String twitter = configPojos.getApp_contact_twitter() != null ? configPojos.getApp_contact_twitter() : "";
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/"+twitter));
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<AboutPojo> call, @NonNull Throwable t) {

            }
        });
    }
    class AboutPojo {         
        @SerializedName("app_contact_address")
        String app_contact_address;
        @SerializedName("app_contact_fb")
        String app_contact_fb;
        @SerializedName("app_contact_ig")
        String app_contact_ig;
        @SerializedName("app_contact_mail")
        String app_contact_mail;
        @SerializedName("app_contact_phone")
        String app_contact_phone;
        @SerializedName("app_contact_twitter")
        String app_contact_twitter;
        @SerializedName("app_contact_wa")
        String app_contact_wa;
        @SerializedName("app_description")
        String app_description;
        @SerializedName("app_dev")
        String app_dev;
        @SerializedName("app_dev_web")
        String app_dev_web;
        @SerializedName("app_name")
        String app_names;

        public AboutPojo(String app_contact_address, String app_contact_fb, String app_contact_ig, String app_contact_mail, String app_contact_phone, String app_contact_twitter, String app_contact_wa, String app_description, String app_dev, String app_dev_web, String app_names) {
            this.app_contact_address = app_contact_address;
            this.app_contact_fb = app_contact_fb;
            this.app_contact_ig = app_contact_ig;
            this.app_contact_mail = app_contact_mail;
            this.app_contact_phone = app_contact_phone;
            this.app_contact_twitter = app_contact_twitter;
            this.app_contact_wa = app_contact_wa;
            this.app_description = app_description;
            this.app_dev = app_dev;
            this.app_dev_web = app_dev_web;
            this.app_names = app_names;
        }

        public AboutPojo() {
        }

        public String getApp_contact_address() {
            return app_contact_address;
        }

        public void setApp_contact_address(String app_contact_address) {
            this.app_contact_address = app_contact_address;
        }

        public String getApp_contact_fb() {
            return app_contact_fb;
        }

        public void setApp_contact_fb(String app_contact_fb) {
            this.app_contact_fb = app_contact_fb;
        }

        public String getApp_contact_ig() {
            return app_contact_ig;
        }

        public void setApp_contact_ig(String app_contact_ig) {
            this.app_contact_ig = app_contact_ig;
        }

        public String getApp_contact_mail() {
            return app_contact_mail;
        }

        public void setApp_contact_mail(String app_contact_mail) {
            this.app_contact_mail = app_contact_mail;
        }

        public String getApp_contact_phone() {
            return app_contact_phone;
        }

        public void setApp_contact_phone(String app_contact_phone) {
            this.app_contact_phone = app_contact_phone;
        }

        public String getApp_contact_twitter() {
            return app_contact_twitter;
        }

        public void setApp_contact_twitter(String app_contact_twitter) {
            this.app_contact_twitter = app_contact_twitter;
        }

        public String getApp_contact_wa() {
            return app_contact_wa;
        }

        public void setApp_contact_wa(String app_contact_wa) {
            this.app_contact_wa = app_contact_wa;
        }

        public String getApp_description() {
            return app_description;
        }

        public void setApp_description(String app_description) {
            this.app_description = app_description;
        }

        public String getApp_dev() {
            return app_dev;
        }

        public void setApp_dev(String app_dev) {
            this.app_dev = app_dev;
        }

        public String getApp_dev_web() {
            return app_dev_web;
        }

        public void setApp_dev_web(String app_dev_web) {
            this.app_dev_web = app_dev_web;
        }

        public String getApp_names() {
            return app_names;
        }

        public void setApp_names(String app_names) {
            this.app_names = app_names;
        }
    }

}
