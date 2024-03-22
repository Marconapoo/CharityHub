package it.sal.disco.unimib.charityhub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import it.sal.disco.unimib.charityhub.R;
import it.sal.disco.unimib.charityhub.model.Project;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private List<Project> projects;
    private Context context;

    public ProjectAdapter(List<Project> projects, Context context) {
        this.projects = projects;
        this.context = context;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_project, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project project = projects.get(position);
        // Bind data to views in the ViewHolder
        holder.title.setText(project.getTitle());
        holder.summary.setText(project.getSummary());
        Glide.with(context).load(project.getImageUrl()).into(holder.image);
        holder.progressBar.setProgress((int) (project.getFunding()*100/project.getGoal()));
        // Set other views as needed
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public Project getProject(int projectId) {
        return projects.get(projectId);
    }

    public static class ProjectViewHolder extends RecyclerView.ViewHolder {

        private final ImageView image;
        private final TextView title;
        private final TextView summary;
        private final ProgressBar progressBar;
        // Declare other views here

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.projectImage);
            title = itemView.findViewById(R.id.projectTitle);
            summary = itemView.findViewById(R.id.projectSummary);
            progressBar = itemView.findViewById(R.id.donationProgressBar);
            // Initialize other views here
        }

    }
}

