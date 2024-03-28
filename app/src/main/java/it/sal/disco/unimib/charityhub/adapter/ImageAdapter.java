package it.sal.disco.unimib.charityhub.adapter;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.carousel.MaskableFrameLayout;
import com.google.android.material.carousel.OnMaskChangedListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

import it.sal.disco.unimib.charityhub.R;
import it.sal.disco.unimib.charityhub.model.Image;
import it.sal.disco.unimib.charityhub.model.ImagesApiResponse;
import it.sal.disco.unimib.charityhub.model.Project;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {


    private final List<Image> images;
    private final Context context;
    private final Project project;
    public ImageAdapter(List<Image> images, Context context, Project project) {
        this.images = images;
        this.context = context;
        this.project = project;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        switch (position) {
            case 0:
                holder.linearProgressIndicator.setProgress(25);
                holder.title.setText("Summary");
                holder.description.setText(project.getSummary());
                break;
            case 1:
                holder.linearProgressIndicator.setProgress(50);
                holder.title.setText("Challenge");
                holder.description.setText(project.getChallenge());
                break;
            case 2:
                holder.linearProgressIndicator.setProgress(75);
                holder.title.setText("Solution");
                holder.description.setText(project.getSolution());
                break;
            case 3:
                holder.linearProgressIndicator.setProgress(100);
                holder.title.setText("Long Term");
                holder.description.setText(project.getLongTermImpact());
                break;
            default:
                break;
        }
        Glide.with(context).load(images.get(position).getUrl()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public Image getImage(int position) {
        return images.get(position);
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        private final ImageView image;

        private final TextView title;
        private final TextView description;
        private final LinearProgressIndicator linearProgressIndicator;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.carousel_image_view);
            title = itemView.findViewById(R.id.titleDetailTextView);
            description = itemView.findViewById(R.id.descriptionDetailTextView);
            linearProgressIndicator = itemView.findViewById(R.id.currentPage);

        }


    }
}
