package com.carousel;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.carousel.bean.MainPhotoBean;
import com.carousel.bean.MatchListBean;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private BannerView bannerView;
    private List bannerlist = new ArrayList<>();;
    private BannerView myBannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10000L, TimeUnit.MILLISECONDS).readTimeout(10000L, TimeUnit.MILLISECONDS).build();
        OkHttpUtils.initClient(okHttpClient);
        Fresco.initialize(this, ConfigConstants.getImagePipelineConfig(this));



        myBannerView = (BannerView) findViewById(R.id.bannerView);
        String tlisturl = DosnapApp.apiHost + DosnapApp.apiDir + "home/index";
        OkHttpUtils.post().url(tlisturl)
                .addParams("identifier", DosnapApp.identifier)
                .addParams("token", DosnapApp.token)
                .addParams("type", "index")
                .addParams("page", 1 + "")
                .addParams("pagesize", "15")
                .addParams("ver", "2")
                .build()
                .execute(stringCallback);
    }


    private StringCallback stringCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
        }

        @Override
        public void onResponse(String response, int id) {
            LogUtil.e(response);
            MainPhotoBean mainPhotoBean = new Gson().fromJson(response, MainPhotoBean.class);
            bannerlist = mainPhotoBean.getMatchlist();
            myBannerView.set(bannerlist);
        }
    };

}
