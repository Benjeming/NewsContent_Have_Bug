package ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.ludans.newscontent.R;

import java.util.ArrayList;

import adapter.NewsTitleListViewAdapter;
import domain.NewsBean;
import domain.NewsBean1;

@SuppressLint("ValidFragment")
public class NewsTitlesView extends Fragment implements AdapterView.OnItemClickListener {
    private ArrayList<NewsBean1> mData;
    private FragmentManager fm ;
    private static final String TAG = "NewsTitlesView";


    public NewsTitlesView(ArrayList<NewsBean1> mData, FragmentManager fm) {
        this.mData = mData;
        this.fm = fm;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_news_titles, container, false);
        ListView  listView = view.findViewById(R.id.list_title);

        NewsTitleListViewAdapter myAdapter = new NewsTitleListViewAdapter(mData, getActivity());
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        NewsContentView newsContentView = new NewsContentView();
        Bundle bundle = new Bundle();
        bundle.putString("content",mData.get(position).getCotent());
        newsContentView.setArguments(bundle);

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_content,newsContentView);
        ft.addToBackStack(null);
        ft.commit();

    }
}
