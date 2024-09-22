package com.tlxsoft.lxeplayerapplication;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

/* compiled from: LxePlayer */
class PanAdapter extends ArrayAdapter<String> {
    List<String> mdata;
    Context savecontext;
    int saveresourceid;

    public PanAdapter(Context context, int i, List<String> list) {
        super(context, i, list);
        this.savecontext = context;
        this.mdata = list;
        this.saveresourceid = i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(this.savecontext).inflate(this.saveresourceid, viewGroup, false);
        }
        TextView textView = (TextView) view.findViewById(R.id.text1);
        if (textView != null) {
            textView.setText(Html.fromHtml(this.mdata.get(i)));
        }
        return view;
    }
}
