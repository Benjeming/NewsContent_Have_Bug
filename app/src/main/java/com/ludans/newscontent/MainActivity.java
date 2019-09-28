package com.ludans.newscontent;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import ViewPager.NewsTitlesView;
import domain.JsonData;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utils.CacheUtils;
import utils.OkHttpUtils;

public class MainActivity extends AppCompatActivity {

    private ArrayList<JsonData> mData;
    private FragmentManager fManager;
    private long exitTime = 0;
    private static final String TAG = "MainActivity";
    private NewsTitlesView newsTitlesView = null;
    private Gson gson;
    private final String TARGET_URL = "http://download.ludashi123.cn/nongshizhidao.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fManager = getSupportFragmentManager();
        initView();
        String saveJson = CacheUtils.getString(MainActivity.this, TARGET_URL);
        if (!TextUtils.isEmpty(saveJson)) {
//            SharedPreferences中有值，则直接解析
            Log.e(TAG, "Shared中有数据，现在进行解析.....");
            gson = new Gson();
            mData = gson.fromJson(saveJson, new TypeToken<ArrayList<JsonData>>() {
            }.getType());
            Log.e(TAG, "解析的数据为： " + mData.get(1).getTitle());

            initJsonData();

        } else {
            Log.e(TAG, "初始化，联网请求");
            OkHttpUtils.sendRequestWithOkhttp(
                    "http://download.ludashi123.cn/nongshizhidao.json",
                    callback);

        }
    }

    Callback callback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.e(TAG, "联网请求失败");
        }

        /* *
         * @date 创建时间：2019/8/19  20:44
         * @author ludans
         * @MethodName: onResponse
         * @Param: [call, response]
         * @return: void
         * @Description :这个异步函数处理要注意了，这里之前出现了一个Bug，问题出在JSON的数据太大，
         * 就只能改成Url，然后再根据Url去爬取那些文章
         */
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//            将获取的数据缓存


            // 这里出现了一个 bug  , response.body 不能调用多次.
            String result = response.body().string();
            Log.e(TAG, "联网成功，获取数据:" );
            CacheUtils.putString(MainActivity.this, TARGET_URL, result);
//            用Gson解析数据
//            Log.e(TAG, "Respones"+response.body().string() );
            parseJsonWithGson(result);
//            将Json数据 传入适配器，然后 初始化页面数据
            initJsonData();

        }

        private void parseJsonWithGson(String response) throws IOException {

            gson = new Gson();
            Log.e(TAG, "响应数据:   " + response);
            mData = gson.fromJson(response, new TypeToken<ArrayList<JsonData>>() {
            }.getType());
        }
    };


    private void initJsonData() {

        newsTitlesView = new NewsTitlesView(mData, fManager);
        FragmentTransaction ft = fManager.beginTransaction();
        ft.replace(R.id.fl_content, newsTitlesView);
        ft.commit();
        Log.e(TAG, "提交NewsTitles");
    }

    private void initView() {
        FrameLayout frameLayout = findViewById(R.id.fl_content);
    }

    @Override
    public void onBackPressed() {
        if (fManager.getBackStackEntryCount() == 0) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        } else {
            fManager.popBackStack();
//            .setText("新闻列表");
        }
    }
}

