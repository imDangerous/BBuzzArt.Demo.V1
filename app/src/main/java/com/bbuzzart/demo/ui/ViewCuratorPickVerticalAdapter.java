package com.bbuzzart.demo.ui;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
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

public class ViewCuratorPickVerticalAdapter extends RecyclerView.Adapter<ViewCuratorPickVerticalAdapter.CustomViewHolder> {

    /**
     * View Holder
     */
    static class CustomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageCover) ImageView imageCover;
        @BindView(R.id.imageBookmark) ImageView imageBookmark;
        @BindView(R.id.imageLike) ImageView imageLike;

        @BindView(R.id.txtTitle) TextView txtTitle;
        @BindView(R.id.txtCreatedDate) TextView txtCreatedDate;
        @BindView(R.id.txtLikeCount) TextView txtLikeCount;
        @BindView(R.id.txtFeedbackCount) TextView txtFeedbackCount;

        @BindView(R.id.rippleItem) MaterialRippleLayout rippleItem;

        public CustomViewHolder(View itemView) {
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



    private BuzzApplication application;
    private List<CuratorPicks.Response.Datum> data;
    private AdapterListener adapterListener;


    public void setAdapterListener(AdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    public ViewCuratorPickVerticalAdapter(BuzzApplication application, List<CuratorPicks.Response.Datum> data) {
        this.application = application;
        this.data = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_default_vertical_item, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder vh, final int i) {
        final CuratorPicks.Response.Datum datum = data.get(i);

        vh.txtTitle.setText(datum.getWork().getTitle());
        vh.txtCreatedDate.setText(datum.getWork().getCreatedDate().substring(0, 10));
        vh.txtLikeCount.setText(String.valueOf(datum.getWork().getLikeCount()));
        vh.txtFeedbackCount.setText(String.valueOf(datum.getWork().getFeedbackCount()));


        Picasso.with(vh.imageCover.getContext())
                .load(Uri.parse(datum.getWork().getAttachments().get(0).getThumbnail().getSmall()))
                .error(R.drawable.thumbnail_default_img)
                .placeholder(R.drawable.thumbnail_default_img)
                .fit()
                .centerCrop()
                .into(vh.imageCover);

        vh.rippleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterListener.onSelected(datum, i);
            }
        });


        setDrawableImage(vh.imageBookmark, datum.getWork().isLiked(), R.drawable.ic_bookmark_black_24dp, R.drawable.ic_bookmark_border_black_24dp);
        setDrawableImage(vh.imageLike, datum.getWork().isBookmarked(), R.drawable.ic_favorite_black_24dp, R.drawable.ic_favorite_border_black_24dp);

    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }


    @Override
    public long getItemId(int position) {
        return data.get(position).getWork().getId();
    }


    /**
     * XML Resource Image 처리
     *
     * @param view
     * @param marked
     * @param markedResId
     * @param unmarkedResId
     */
    private void setDrawableImage(ImageView view, boolean marked, @DrawableRes int markedResId, @DrawableRes int unmarkedResId) {
        if (marked) {
            Drawable bg;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                bg = VectorDrawableCompat.create(application.getResources(), markedResId, null);
                view.setColorFilter(ContextCompat.getColor(application, R.color.md_white_1000), PorterDuff.Mode.MULTIPLY);
            } else {
                bg = ContextCompat.getDrawable(application, markedResId);
                DrawableCompat.setTint(bg, ContextCompat.getColor(application, R.color.md_white_1000));
            }

            view.setImageDrawable(bg);
        } else {
            Drawable bg;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                bg = VectorDrawableCompat.create(application.getResources(), unmarkedResId, null);
                view.setColorFilter(ContextCompat.getColor(application, R.color.md_white_1000), PorterDuff.Mode.MULTIPLY);
            } else {
                bg = ContextCompat.getDrawable(application, unmarkedResId);
                DrawableCompat.setTint(bg, ContextCompat.getColor(application, R.color.md_white_1000));
            }

            view.setImageDrawable(bg);
        }
    }

}
