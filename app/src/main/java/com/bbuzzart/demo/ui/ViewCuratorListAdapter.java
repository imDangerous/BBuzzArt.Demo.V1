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

public class ViewCuratorListAdapter extends RecyclerView.Adapter<ViewCuratorListAdapter.CustomViewHolder> {

    private static final String TAG = ViewCuratorListAdapter.class.getSimpleName();

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

        public CustomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * Click Listener
     */
    public interface AdapterListener {
        void onSelected(long curatorId, int position);
    }



    private List<CuratorPicks.Response.Datum.Curator> curators;
    private AdapterListener adapterListener;


    public void setAdapterListener(AdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    public ViewCuratorListAdapter(BuzzApplication application, List<CuratorPicks.Response.Datum.Curator> curators) {
        this.curators = curators;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_default_curator_item, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder vh, final int i) {
        final CuratorPicks.Response.Datum.Curator curator = curators.get(i);

        Picasso.with(vh.imagePoster.getContext())
                .load(Uri.parse(curator.getThumbnail()))
                .error(R.drawable.thumbnail_default_img)
                .placeholder(R.drawable.thumbnail_default_img)
                .fit()
                .centerCrop()
                .into(vh.imagePoster);

        vh.txtTitle.setText(curator.getUsername());

        vh.rippleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterListener.onSelected(curator.getId(), i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != curators ? curators.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return curators.get(position).getId();
    }

}
