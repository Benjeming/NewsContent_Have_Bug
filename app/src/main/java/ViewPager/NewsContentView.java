package ViewPager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ludans.newscontent.R;

public class NewsContentView extends Fragment {
    private TextView textView = null;
    private TextView textView_title = null;
    private static final String TAG = "NewsContentView";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_news_content, container, false);
        textView = view.findViewById(R.id.fg_newsContent_text);
        textView_title = view.findViewById(R.id.fg_newsContent_text_title);

        //下面是设置值的位置
        Bundle bundle = getArguments();
        textView.setText(bundle.getString("content"));
        textView_title.setText(bundle.getString("title"));

        return view;
    }
}
