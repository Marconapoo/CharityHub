package it.sal.disco.unimib.charityhub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

import it.sal.disco.unimib.charityhub.R;
import it.sal.disco.unimib.charityhub.model.projects.Image;
import it.sal.disco.unimib.charityhub.model.projects.Project;
import it.sal.disco.unimib.charityhub.ui.main.ProjectDetailsFragmentDirections;

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
        if(images.get(position) != null && images.get(position).getUrl() != null && !images.get(position).getUrl().isEmpty()) {
                Glide.with(context).load(images.get(position).getUrl())
                        .placeholder(R.mipmap.ic_launcher_foreground)
                        .into(holder.image);
        } else {
            Glide.with(context).load(R.mipmap.ic_launcher_foreground).into(holder.image);
        }
        
        switch (position) {
            case 0:
                holder.linearProgressIndicator.setProgress(25);
                holder.title.setText(R.string.summary);
                holder.description.setText(project.getSummary());
                break;
            case 1:
                holder.linearProgressIndicator.setProgress(50);
                holder.title.setText(R.string.challenge);
                holder.description.setText(project.getChallenge());
                break;
            case 2:
                holder.linearProgressIndicator.setProgress(75);
                holder.title.setText(R.string.solution);
                holder.description.setText(project.getSolution());
                break;
            case 3:
                holder.linearProgressIndicator.setProgress(100);
                holder.title.setText(R.string.long_term_impact);
                holder.description.setText(project.getLongTermImpact());
                break;
            default:
                break;
        }
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
