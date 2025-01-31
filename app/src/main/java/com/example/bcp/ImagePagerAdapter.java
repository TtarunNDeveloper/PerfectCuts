package com.example.bcp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.viewpager.widget.PagerAdapter;
import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {
    private Context context;
    private List<byte[]> imageList;

    public  ImagePagerAdapter(Context context, List<byte[]> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);

        byte[] imageBytes = imageList.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            container.addView(imageView);
            return imageView;
        } else {
            return new View(context);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public byte[] getImageAtPosition(int currpos) {
        if (currpos >= 0 && currpos < imageList.size()) {
            return imageList.get(currpos);
        } else {
            return null;
        }
    }
}
