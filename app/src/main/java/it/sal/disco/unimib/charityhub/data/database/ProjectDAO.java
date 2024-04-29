package it.sal.disco.unimib.charityhub.data.database;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import it.sal.disco.unimib.charityhub.model.projects.Project;

@Dao
public interface ProjectDAO {

    @Query("SELECT * FROM project")
    List<Project> getAll();

    @Query("SELECT * FROM project WHERE id = :id")
    List<Project> getProjects(int id);


}
