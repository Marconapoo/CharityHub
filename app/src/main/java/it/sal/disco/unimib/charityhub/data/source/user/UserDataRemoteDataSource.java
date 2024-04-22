package it.sal.disco.unimib.charityhub.data.source.user;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import it.sal.disco.unimib.charityhub.model.User;

public class UserDataRemoteDataSource extends BaseUserDataRemoteDataSource {

    private final FirebaseFirestore db;


    public UserDataRemoteDataSource() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void saveUserData(User user) {
        db.collection("Users")
                .document(user.getUid())
                .set(user)
                .addOnSuccessListener(unused -> userResponseCallback.onSuccessUserSaved(user))
                .addOnFailureListener(e -> userResponseCallback.onFailureAuthentication(e.getLocalizedMessage()));

    }

    @Override
    public void getUserCountry(User user) {
        db.collection("Users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String country = documentSnapshot.getString("countryOfInterest");
                    userResponseCallback.onCountryGotSuccess(new User(user.getName(), user.getEmail(), user.getUid(), country));
                })
                .addOnFailureListener(e -> userResponseCallback.onFailureAuthentication(e.getLocalizedMessage()));
    }

    @Override
    public void changeUserCountry(User user) {
        DocumentReference documentReference = db.collection("Users").document(user.getUid());
        documentReference
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userResponseCallback.onSuccessInfoChanged(user);
                    }
                    else {
                        userResponseCallback.onFailureAuthentication(task.getException().getLocalizedMessage());
                    }
                })
                .addOnFailureListener(e -> userResponseCallback.onFailureAuthentication(e.getLocalizedMessage()));
    }
}
