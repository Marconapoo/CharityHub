package it.sal.disco.unimib.charityhub.data.source.projects;

import android.app.Application;

import it.sal.disco.unimib.charityhub.utils.Constants;
import it.sal.disco.unimib.charityhub.utils.SharedPreferencesUtil;

public class ProjectLocalDataSource extends BaseProjectLocalDataSource {

    private final SharedPreferencesUtil sharedPreferencesUtil;

    public ProjectLocalDataSource(Application application) {
        this.sharedPreferencesUtil = new SharedPreferencesUtil(application);
    }
    @Override
    public void deleteAll() {
        sharedPreferencesUtil.deleteAll(Constants.SHARED_PREFERENCES_FILE_NAME);
    }
}
