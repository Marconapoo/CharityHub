package it.sal.disco.unimib.charityhub.data.repositories.project;

import androidx.lifecycle.MutableLiveData;

import it.sal.disco.unimib.charityhub.model.Result;

public interface IProjectRepository {
    MutableLiveData<Result> getImagesLiveData(String projectId);
}
