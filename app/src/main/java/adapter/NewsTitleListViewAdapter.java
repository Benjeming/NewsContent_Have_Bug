package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ludans.newscontent.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import domain.NewsBean;
import domain.JsonData;

public class NewsTitleListViewAdapter extends BaseAdapter {
    private ArrayList<JsonData> mData = null;
    private Context context;


    public NewsTitleListViewAdapter(ArrayList<JsonData> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    private class ViewHodler {
        TextView textView;
        TextView textView_time;
    }

    ;

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//            初始化视图
            viewHodler = initViewHodlerView(convertView);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        // 这个地方是设置 Text值的地方
        setViewHolderText(position, viewHodler);
        return convertView;
    }

    @NotNull
    private ViewHodler initViewHodlerView(View convertView) {
        ViewHodler viewHodler;
        viewHodler = new ViewHodler();
        viewHodler.textView = convertView.findViewById(R.id.item_text);
        viewHodler.textView_time = convertView.findViewById(R.id.item_text_time);
        return viewHodler;
    }

    private void setViewHolderText(int position, ViewHodler viewHodler) {
//        Log.e("Test", "Url : " +  mData.get(position).getUrl());
        viewHodler.textView.setText(mData.get(position).getTitle());
        viewHodler.textView_time.setText(mData.get(position).getTime());
    }
}
