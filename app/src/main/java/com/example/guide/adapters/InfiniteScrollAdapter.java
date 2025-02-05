package com.example.guide.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.guide.Model.Places;
import com.example.guide.R;
import com.example.guide.interfaces.GalleryTagsListInterface;

import java.util.List;

public class InfiniteScrollAdapter extends PagerAdapter {

    List<Places> lstImages;
    Context context;
    LayoutInflater layoutInflater;
    GalleryTagsListInterface galleryTagsListInterface;

    public InfiniteScrollAdapter(List<Places> lstImages, Context context, GalleryTagsListInterface galleryTagsListInterface) {
        this.lstImages = lstImages;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.galleryTagsListInterface = galleryTagsListInterface;
    }
    public InfiniteScrollAdapter(List<Places> lstImages, Context context) {
        this.lstImages = lstImages;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return lstImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.card_item, container, false);
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textView = view.findViewById(R.id.description);
        Glide.with(view)
                .load(context.getResources().getIdentifier(lstImages.get(position).getImage(),"drawable", context.getPackageName()))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.code_icon)
                .error(R.drawable.ic_close_black_24dp)
                .override(1000,1000)
                .fitCenter()
                .into(imageView)
        ;
       textView.setText(lstImages.get(position).getDescription());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(galleryTagsListInterface!=null) {
                    galleryTagsListInterface.onTagClicked(lstImages.get(position).getImage(), lstImages.get(position).getDescription(), position);

                }
            }
        });
        container.addView(view);
        return view;
    }
}
