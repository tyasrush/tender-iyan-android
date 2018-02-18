package com.tender.iyan.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tender.iyan.R;
import com.tender.iyan.entity.Deal;
import java.util.List;

/**
 * Created by tyas on 2/18/18.
 */

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.Holder> {

  private OnDealsClickListener onDealsClickListener;
  private List<Deal> deals;

  public void setOnDealsClickListener(OnDealsClickListener onDealsClickListener) {
    this.onDealsClickListener = onDealsClickListener;
  }

  public void setDeals(List<Deal> deals) {
    this.deals = deals;
  }

  @Override public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_two, parent, false);
    return new Holder(view);
  }

  @Override public void onBindViewHolder(Holder holder, int position) {
    Deal deal = deals.get(position);
    holder.tvTitle.setText(deal.getNamaPenawaran());
    holder.tvSubtitle.setText(deal.getHarga());
  }

  @Override public int getItemCount() {
    return deals.size();
  }

  public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tvTitle;
    public TextView tvSubtitle;

    public Holder(View itemView) {
      super(itemView);
      tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
      tvSubtitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
      if (onDealsClickListener != null) onDealsClickListener.onDealsClick(v, getAdapterPosition());
    }
  }

  public interface OnDealsClickListener {
    void onDealsClick(View view, int position);
  }
}
