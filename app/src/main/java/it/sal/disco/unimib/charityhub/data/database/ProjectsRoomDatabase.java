package it.sal.disco.unimib.charityhub.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.sal.disco.unimib.charityhub.model.projects.Project;
import it.sal.disco.unimib.charityhub.utils.Converters;

@Database(entities = {Project.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class ProjectsRoomDatabase extends RoomDatabase {

    public abstract ProjectDAO projectDAO();

    private static volatile ProjectsRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ProjectsRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ProjectsRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ProjectsRoomDatabase.class, "projects_db").build();
                }
            }
        }
        return INSTANCE;
    }
}
