package com.tender.iyan.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tender.iyan.BuildConfig;
import com.tender.iyan.R;
import com.tender.iyan.entity.Tender;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private OnItemTenderClickListener onItemTenderClickListener;
    private List<Tender> tenders;
    private Context context;

    public HomeAdapter(List<Tender> tenders) {
        this.tenders = tenders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titleView.setText(tenders.get(position).getName());
        holder.priceView.setText("Anggaran : " + String.valueOf(tenders.get(position).getAnggaran()));
        holder.kategoriView.setText(tenders.get(position).getKategori().getNama());
        Glide.with(context)
                .load(tenders.get(position).getFoto())
                .into(holder.itemImageView);
    }

    @Override
    public int getItemCount() {
        return tenders.size();
    }

    public void setOnItemTenderClickListener(OnItemTenderClickListener onItemTenderClickListener) {
        this.onItemTenderClickListener = onItemTenderClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView itemImageView;
        private TextView titleView;
        private TextView priceView;
        private TextView kategoriView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImageView = (ImageView) itemView.findViewById(R.id.image_item_home);
            titleView = (TextView) itemView.findViewById(R.id.title_item_home);
            priceView = (TextView) itemView.findViewById(R.id.price_item_home);
            kategoriView = (TextView) itemView.findViewById(R.id.price_item_kategori);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemTenderClickListener != null) {
                onItemTenderClickListener.onItemTenderClick(view, getAdapterPosition());
            }
        }
    }

    public interface OnItemTenderClickListener {
        void onItemTenderClick(View view, int position);
    }
}
