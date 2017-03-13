package com.tender.iyan.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tender.iyan.R;
import com.tender.iyan.entity.Penawaran;
import com.tender.iyan.entity.Tender;

import java.util.List;

/**
 * Created by tyas on 7/15/16.
 */
public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private final String TAG = getClass().getName();
    private final int HEADER = 0;
    private final int ITEM = 1;

    private Context context;
    private Tender tender;
    private List<Penawaran> penawaranList;

    public DetailAdapter(Tender tender, List<Penawaran> penawaranList) {
        this.tender = tender;
        this.penawaranList = penawaranList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_header_detail, parent, false);
            return new ViewHolder(view, HEADER);
        }

        if (viewType == ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_list_detail, parent, false);
            return new ViewHolder(view, ITEM);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.holderViewType == HEADER) {
            holder.descTextView.setText(tender.getDeskripsi());
            holder.priceTextView.setText(String.valueOf(tender.getAnggaran()));
            holder.waktuTextView.setText(tender.getWaktu());
            holder.penawaranTextView.setText(penawaranList != null && !penawaranList.isEmpty() ? "Penawaran Tersedia" : "Belum Ada Penawaran");
        }

        if (holder.holderViewType == ITEM) {
            if (penawaranList != null && !penawaranList.isEmpty()) {
                holder.userItemTextView.setText(penawaranList.get(position - 1).getNama());
                holder.descItemTextView.setText(penawaranList.get(position - 1).getDeskripsi());
                Glide.with(context)
                        .load(penawaranList.get(position - 1).getFoto())
                        .into(holder.itemImageView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return penawaranList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == HEADER ? HEADER : ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private int holderViewType;

        private TextView descTextView;
        private TextView priceTextView;
        private TextView waktuTextView;
        private TextView penawaranTextView;

        private TextView userItemTextView;
        private TextView descItemTextView;
        private ImageView itemImageView;


        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            holderViewType = viewType;
            if (viewType == HEADER) {
                descTextView = (TextView) itemView.findViewById(R.id.tv_desc);
                priceTextView = (TextView) itemView.findViewById(R.id.tv_anggaran);
                waktuTextView = (TextView) itemView.findViewById(R.id.tv_waktu);
                penawaranTextView = (TextView) itemView.findViewById(R.id.tv_penawaran);
            }

            if (viewType == ITEM) {
                itemImageView = (ImageView) itemView.findViewById(R.id.img_item_list_detail);
                userItemTextView = (TextView) itemView.findViewById(R.id.tv_user_detail);
                descItemTextView = (TextView) itemView.findViewById(R.id.tv_desc_detail);
            }
        }
    }
}
