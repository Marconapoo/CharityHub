package it.sal.disco.unimib.charityhub.data.source.projects;

public abstract class BaseProjectLocalDataSource {

    protected ProjectCallback projectCallback;

    public void setProjectCallback(ProjectCallback projectCallback) {
        this.projectCallback = projectCallback;
    }

    public abstract void deleteAll();


}
