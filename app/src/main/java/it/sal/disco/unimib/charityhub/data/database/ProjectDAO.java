package it.sal.disco.unimib.charityhub.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import it.sal.disco.unimib.charityhub.model.projects.Project;

@Dao
public interface ProjectDAO {

    @Query("SELECT * FROM project")
    List<Project> getAll();

    @Query("SELECT * FROM project WHERE id = :id")
    List<Project> getProjects(int id);

    @Query("SELECT * FROM project WHERE iso3166CountryCode = :country")
    List<Project> getProjectsByCountry(String country);

    @Query("SELECT * FROM project WHERE themeName = :themeName AND iso3166CountryCode = :country")
    List<Project> getProjectsByTheme(String themeName, String country);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProjects(List<Project> projectList);

    @Query("DELETE FROM project")
    void deleteAll();
}
