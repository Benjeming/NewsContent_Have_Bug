package ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ludans.newscontent.MainActivity;
import com.ludans.newscontent.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import adapter.NewsTitleListViewAdapter;
import domain.JsonData;
import utils.CacheUtils;

@SuppressLint("ValidFragment")
public class NewsTitlesView extends Fragment implements AdapterView.OnItemClickListener {
    private ArrayList<JsonData> mData;
    private FragmentManager fm;
    private NewsContentView newsContentView = null;
    private static final String TAG = "NewsTitlesView";
    private Bundle bundle = new Bundle();
    private final String BASE_URL = "http://nyncj.changde.gov.cn";


    public NewsTitlesView(ArrayList<JsonData> mData, FragmentManager fm) {
        this.mData = mData;
        this.fm = fm;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_news_titles, container, false);
        ListView listView = view.findViewById(R.id.list_title);

        NewsTitleListViewAdapter myAdapter = new NewsTitleListViewAdapter(mData, getActivity());
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(this);
        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
        String contentUrl = CacheUtils.getString(view.getContext(), mData.get(position).getUrl());
        if (!TextUtils.isEmpty(contentUrl)) {
            Log.e(TAG, "Shared中有数据:" + contentUrl);
            putDataToNewsContent(position, contentUrl);
//                切换页面
            replaceNewsContent();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String TATGET_URL = BASE_URL + mData.get(position).getUrl();
                    String content_total = "";
                    try {
                        Document doc = Jsoup.connect(TATGET_URL).get();
                        Elements elementsId_Zoom = doc.getElementsByAttributeValue("id", "zoom");
                        String clean = Jsoup.clean(elementsId_Zoom.first().html(), Whitelist.basic());
                        Log.e(TAG, "联网抓取.....");
                        String replace = clean.replace("<br>", "\n")
                                .replace("&nbsp;", " ")
                                .replace("<p>", "\t")
                                .replace("</p>", "\n");
                        content_total = "\t" + replace + "\n";
                        CacheUtils.putString(view.getContext(), mData.get(position).getUrl(), content_total);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
//               将数据打包，传递数据
                        putDataToNewsContent(position, content_total);
//                切换页面
                        replaceNewsContent();
                    }
                }
            }).start();
        }


    }

    private void replaceNewsContent() {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_content, newsContentView);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void putDataToNewsContent(int position, String content) {
        newsContentView = new NewsContentView();
        //根据 传递过来的Url进行爬虫，然后将数据传递给NewsContent


        bundle.putString("content", content);
        bundle.putString("title", mData.get(position).getTitle());


        newsContentView.setArguments(bundle);
    }
}
