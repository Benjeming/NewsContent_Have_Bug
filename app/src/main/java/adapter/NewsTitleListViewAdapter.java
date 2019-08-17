package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ludans.newscontent.R;

import java.util.ArrayList;

import domain.NewsBean;
import domain.NewsBean1;

public class NewsTitleListViewAdapter extends BaseAdapter {
    private ArrayList<NewsBean1> mData = null;
    private Context context;

    public NewsTitleListViewAdapter(ArrayList<NewsBean1> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    private class ViewHodler {
        TextView textView;
    };
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
          convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
            viewHodler = new ViewHodler();
            viewHodler.textView = convertView.findViewById(R.id.item_text);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        // 这个地方是设置 Text值的地方
        viewHodler.textView.setText(mData.get(position).getTitle());
        return convertView;
    }
}
