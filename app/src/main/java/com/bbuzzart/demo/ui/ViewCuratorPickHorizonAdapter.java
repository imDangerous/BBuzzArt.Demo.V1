package com.bbuzzart.demo.ui;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bbuzzart.demo.BuzzApplication;
import com.bbuzzart.demo.R;
import com.bbuzzart.demo.model.CuratorPicks;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewCuratorPickHorizonAdapter extends RecyclerView.Adapter<ViewCuratorPickHorizonAdapter.CustomViewHolder> {

    private static final String TAG = ViewCuratorPickHorizonAdapter.class.getSimpleName();

    /**
     * View Holder
     */
    static class CustomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imagePoster)
        ImageView imagePoster;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.rippleItem)
        MaterialRippleLayout rippleItem;

        CustomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * Click Listener
     */
    public interface AdapterListener {
        void onSelected(CuratorPicks.Response.Datum datum, int position);
    }


    private List<CuratorPicks.Response.Datum> data;
    private AdapterListener adapterListener;


    public void setAdapterListener(AdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    public ViewCuratorPickHorizonAdapter(BuzzApplication application, List<CuratorPicks.Response.Datum> data) {
        this.data = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_default_horizontal_item, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder vh, final int i) {
        final CuratorPicks.Response.Datum datum = data.get(i);

        Picasso.with(vh.imagePoster.getContext())
                .load(Uri.parse(datum.getWork().getAttachments().get(0).getThumbnail().getSmall()))
                .error(R.drawable.thumbnail_default_img)
                .placeholder(R.drawable.thumbnail_default_img)
                .fit()
                .centerCrop()
                .into(vh.imagePoster);

        vh.txtTitle.setText(datum.getWork().getTitle());

        vh.rippleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterListener.onSelected(datum, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }


    @Override
    public long getItemId(int position) {
        return data.get(position).getWork().getId();
    }

}
