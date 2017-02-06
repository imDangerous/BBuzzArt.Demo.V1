package com.bbuzzart.demo.ui;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbuzzart.demo.BaseUnbindFragment;
import com.bbuzzart.demo.BuzzConstants;
import com.bbuzzart.demo.R;
import com.bbuzzart.demo.model.CuratorPicks;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A placeholder fragment containing a simple view.
 */
public class CuratorDetailActivityFragment extends BaseUnbindFragment implements BuzzConstants {

    private static final String TAG = CuratorDetailActivityFragment.class.getSimpleName();


    @BindView(R.id.appbar) AppBarLayout appBarLayout;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.nestedScrollView) NestedScrollView nestedScrollView;

    @BindView(R.id.imageCover) ImageView imageCover;
    @BindView(R.id.txtTitle) TextView txtTitle;
    @BindView(R.id.txtCreatedDate) TextView txtCreatedDate;
    @BindView(R.id.txtLikeCount) TextView txtLikeCount;
    @BindView(R.id.txtFeedbackCount) TextView txtFeedbackCount;
    @BindView(R.id.txtFeedback) TextView txtFeedback;

    @BindView(R.id.imageArtist) CircleImageView imageArtist;
    @BindView(R.id.txtArtistName) TextView txtArtistName;
    @BindView(R.id.imageCurator) CircleImageView imageCurator;
    @BindView(R.id.txtCuratorName) TextView txtCuratorName;


    private CuratorPicks.Response.Datum datum;


    @OnClick(R.id.rippleGoTop)
    void rippleGoTop() {
        nestedScrollView.scrollTo(0, 0);
        appBarLayout.setExpanded(true, true);
    }

    @OnClick(R.id.rippleArtistAdd)
    void rippleArtistAdd() {
        Toast.makeText(getActivity(), "Follow Touched, Artist [" + datum.getWork().getCreatedBy().getUsername() + "]", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.rippleArtistEmail)
    void rippleArtistEmail() {
        Toast.makeText(getActivity(), "Email Touched, Artist [" + datum.getWork().getCreatedBy().getUsername() + "]", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.rippleArtistMore)
    void rippleArtistMore() {
        Toast.makeText(getActivity(), "More Touched, Artist [" + datum.getWork().getCreatedBy().getUsername() + "]", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.rippleCuratorAdd)
    void rippleCuratorAdd() {
        Toast.makeText(getActivity(), "Follow Touched, Curator [" + datum.getCurator().getUsername() + "]", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.rippleCuratorEmail)
    void rippleCuratorEmail() {
        Toast.makeText(getActivity(), "Email Touched, Curator [" + datum.getCurator().getUsername() + "]", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.rippleCuratorMore)
    void rippleCuratorMore() {
        Toast.makeText(getActivity(), "More Touched, Curator [" + datum.getCurator().getUsername() + "]", Toast.LENGTH_SHORT).show();
    }


    public static CuratorDetailActivityFragment create(CuratorPicks.Response.Datum datum) {
        CuratorDetailActivityFragment fragment = new CuratorDetailActivityFragment();
        Bundle args = new Bundle();
        args.putParcelable("datum", Parcels.wrap(datum));
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        ViewGroup viewGroup = (ViewGroup) getView();
        viewGroup.removeAllViewsInLayout();
        View view = onCreateView(getActivity().getLayoutInflater(), viewGroup, null);
        viewGroup.addView(view);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_detail;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (getArguments() != null) {
            datum = Parcels.unwrap(getArguments().getParcelable("datum"));

            Picasso.with(imageCover.getContext())
                    .load(Uri.parse(datum.getCroppedImage()))
                    .error(R.drawable.thumbnail_default_img)
                    .into(imageCover);

            Picasso.with(imageArtist.getContext())
                    .load(Uri.parse(datum.getWork().getCreatedBy().getThumbnail()))
                    .error(R.drawable.thumbnail_default_img)
                    .into(imageArtist);

            Picasso.with(imageCurator.getContext())
                    .load(Uri.parse(datum.getCurator().getThumbnail()))
                    .error(R.drawable.thumbnail_default_img)
                    .into(imageCurator);


            collapsingToolbar.setTitle(datum.getWork().getTitle());

            txtTitle.setText(datum.getWork().getTitle());
            txtCreatedDate.setText(datum.getWork().getCreatedDate().substring(0, 10));
            txtLikeCount.setText(String.valueOf(datum.getWork().getLikeCount()));
            txtFeedbackCount.setText(String.valueOf(datum.getWork().getFeedbackCount()));
            txtFeedback.setText(datum.getFeedback());
            txtArtistName.setText(datum.getWork().getCreatedBy().getUsername());
            txtCuratorName.setText(datum.getCurator().getUsername());

        } else {
            Toast.makeText(getActivity(), "[There's no data]", Toast.LENGTH_SHORT).show();

            getActivity().finish();
        }
    }

}
