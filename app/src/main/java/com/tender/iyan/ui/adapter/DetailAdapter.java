package com.tender.iyan.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tender.iyan.R;
import com.tender.iyan.entity.Penawaran;
import com.tender.iyan.entity.Tender;
import com.tender.iyan.util.UserUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private final String TAG = getClass().getName();
    private final int HEADER = 0;
    private final int ITEM = 1;

    private Context context;
    private Tender tender;
    private List<Penawaran> penawaranList;

    private OnOpenMapClickListener onOpenMapClickListener;
    private OnDealClickListener onDealClickListener;

    public DetailAdapter(Context context, Tender tender, List<Penawaran> penawaranList) {
        this.context = context;
        this.tender = tender;
        this.penawaranList = penawaranList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

            holder.kategoriTextView.setText(tender.getKategori().getNama());

            Calendar start_calendar = Calendar.getInstance();
            Calendar end_calendar = Calendar.getInstance();

            start_calendar.setTime(new Date());
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Date date;
            try {
                date = df.parse(tender.getWaktu());
                end_calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (end_calendar.before(start_calendar)) {
                holder.penawaranTextView.setBackgroundColor(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? context.getColor(R.color.colorAccent) : context.getResources().getColor(R.color.colorAccent));
                holder.penawaranTextView.setText("Penawaran Ditutup");
            } else {
                holder.penawaranTextView.setText(penawaranList != null && !penawaranList.isEmpty() ? "Penawaran Tersedia" : "Belum Ada Penawaran");
            }


        }

        if (holder.holderViewType == ITEM) {
            if (penawaranList != null && !penawaranList.isEmpty()) {
                holder.userItemTextView.setText(penawaranList.get(position - 1).getNama());
                holder.descItemTextView.setText(penawaranList.get(position - 1).getDeskripsi());
                if (UserUtil.getInstance(context).getId() == tender.getIduser()) {
                    holder.priceItemTextView.setText(String.valueOf(penawaranList.get(position - 1).getHarga()));
                } else {
                    holder.priceItemTextView.setVisibility(View.GONE);
                    holder.dealButton.setVisibility(View.GONE);
                }

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

    public interface OnOpenMapClickListener {
        void onOpenMapClick(int position);
    }

    public interface OnDealClickListener {
        void onDealClick(int position);
    }

    public void setOnOpenMapClickListener(OnOpenMapClickListener onOpenMapClickListener) {
        this.onOpenMapClickListener = onOpenMapClickListener;
    }

    public void setOnDealClickListener(OnDealClickListener onDealClickListener) {
        this.onDealClickListener = onDealClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int holderViewType;

        private TextView descTextView;
        private TextView priceTextView;
        private TextView waktuTextView;
        private TextView kategoriTextView;
        private TextView penawaranTextView;

        private TextView userItemTextView;
        private TextView descItemTextView;
        private TextView priceItemTextView;
        private Button openLocationButton;
        private Button dealButton;
        private ImageView itemImageView;


        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            holderViewType = viewType;
            if (viewType == HEADER) {
                descTextView = (TextView) itemView.findViewById(R.id.tv_desc);
                priceTextView = (TextView) itemView.findViewById(R.id.tv_anggaran);
                waktuTextView = (TextView) itemView.findViewById(R.id.tv_waktu);
                penawaranTextView = (TextView) itemView.findViewById(R.id.tv_penawaran);
                kategoriTextView = (TextView) itemView.findViewById(R.id.tv_kategori);
            }

            if (viewType == ITEM) {
                itemImageView = (ImageView) itemView.findViewById(R.id.img_item_list_detail);
                userItemTextView = (TextView) itemView.findViewById(R.id.tv_user_detail);
                descItemTextView = (TextView) itemView.findViewById(R.id.tv_desc_detail);
                priceItemTextView = (TextView) itemView.findViewById(R.id.tv_price_detail);
                openLocationButton = (Button) itemView.findViewById(R.id.btn_open_map);
                dealButton = (Button) itemView.findViewById(R.id.btn_deal);
                openLocationButton.setOnClickListener(this);
                dealButton.setOnClickListener(this);
            }
        }

        @Override public void onClick(View v) {
            if (v.getId() == openLocationButton.getId()) {
                if (onOpenMapClickListener != null)
                    onOpenMapClickListener.onOpenMapClick(getAdapterPosition() - 1);

            }

            if (v.getId() == dealButton.getId()) {
                if (onDealClickListener != null)
                    onDealClickListener.onDealClick(getAdapterPosition() - 1);

            }
        }
    }
}
