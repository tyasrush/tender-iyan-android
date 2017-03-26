package com.tender.iyan.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tender.iyan.entity.Kategori;

import org.w3c.dom.Text;

import java.util.List;

public class KategoriAdapter extends BaseAdapter {

    private List<Kategori> list;

    public KategoriAdapter(List<Kategori> list) {
        this.list = list;
    }

    @Override public int getCount() {
        return list.size();
    }

    @Override public Object getItem(int position) {
        return list.get(position);
    }

    @Override public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View createView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        TextView textView = (TextView) createView.findViewById(android.R.id.text1);
        textView.setText(list.get(position).getNama());

        return createView;
    }
}
