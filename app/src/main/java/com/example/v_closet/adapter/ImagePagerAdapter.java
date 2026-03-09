package com.example.v_closet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v_closet.R;
import com.example.v_closet.entity.ProductImage;

import java.util.List;

public class ImagePagerAdapter extends RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder> {
    
    private Context context;
    private List<ProductImage> images;
    
    public ImagePagerAdapter(Context context, List<ProductImage> images) {
        this.context = context;
        this.images = images;
    }
    
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_pager, parent, false);
        return new ImageViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ProductImage image = images.get(position);
        
        if (image.getImageUrl() != null) {
            int resourceId = context.getResources().getIdentifier(
                image.getImageUrl(), "drawable", context.getPackageName());
            holder.imageView.setImageResource(resourceId != 0 ? resourceId : R.drawable.img_unavailable);
        }
    }
    
    @Override
    public int getItemCount() {
        return images != null ? images.size() : 0;
    }
    
    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
