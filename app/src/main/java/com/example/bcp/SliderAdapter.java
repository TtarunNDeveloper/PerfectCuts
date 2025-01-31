package com.example.bcp;// SliderAdapter.java
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.bcp.BitmapUtils;
import com.example.bcp.R;

import java.util.List;

public class SliderAdapter extends PagerAdapter {
    private Context context;
    private List<byte[]> imageList;

    public SliderAdapter(Context context, List<byte[]> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        Log.d("ImageCount", "Count: " + imageList.size());
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.d("ImagePosition", "Position: " + position + ", Size: " + imageList.get(position).length);
        ImageView imageView = new ImageView(context);

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageList.get(position), 0, imageList.get(position).length);

        if (bitmap != null) {
            Toast.makeText(context, "Not null", Toast.LENGTH_SHORT).show();
            imageView.setImageBitmap(bitmap);
            container.addView(imageView);
            return imageView;
        } else {
            Toast.makeText(context, "Null", Toast.LENGTH_SHORT).show();
            return new View(context);
        }
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
