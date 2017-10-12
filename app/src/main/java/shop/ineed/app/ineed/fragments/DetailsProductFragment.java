package shop.ineed.app.ineed.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import org.parceler.Parcels;

import java.util.Timer;
import java.util.TimerTask;

import me.gujun.android.taggroup.TagGroup;
import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.adapter.SlideAdapter;
import shop.ineed.app.ineed.domain.Product;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsProductFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    private Product mProduct;
    private ViewPager mPager;
    private static int CURRENT_PAGE = 0;
    private static int NUM_PAGES = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details_product, container, false);
        mProduct = Parcels.unwrap(getArguments().getParcelable("product"));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView txtTitleProduct = (TextView) view.findViewById(R.id.txtTitleProduct);
        txtTitleProduct.setText(mProduct.getName());
        TextView txtDescriptionProduct = (TextView) view.findViewById(R.id.txtDescription);
        txtDescriptionProduct.setText(mProduct.getDescription());

        TagGroup mTagGroup = (TagGroup) view.findViewById(R.id.tag_group);
        mTagGroup.setTags(mProduct.getCategories());

        initSlide(view);

    }

    private void initSlide(View view) {
        mPager = (ViewPager) view.findViewById(R.id.sliderDetailsProductFragment);
        mPager.setAdapter(new SlideAdapter(getActivity(), mProduct.getPictures()));


        CirclePageIndicator indicator = (CirclePageIndicator)
                view.findViewById(R.id.indicatorDetailsSlideFragment);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius(5 * density);


        NUM_PAGES = mProduct.getPictures().size();


        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (CURRENT_PAGE == NUM_PAGES) {
                    CURRENT_PAGE = 0;
                }
                mPager.setCurrentItem(CURRENT_PAGE++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 4000);

        indicator.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        CURRENT_PAGE = position;
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
