package com.zbar.lib.guide;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbar.lib.R;

public class GuidePage4Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.layout_activity_guide_page_4, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView textView = getView().findViewById(R.id.app_go_textView);

        textView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "跳转到首页", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getActivity(),WelcomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

}
