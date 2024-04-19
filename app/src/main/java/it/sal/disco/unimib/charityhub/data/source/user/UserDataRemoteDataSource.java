package it.sal.disco.unimib.charityhub.data.source.user;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import it.sal.disco.unimib.charityhub.model.User;

public class UserDataRemoteDataSource extends BaseUserDataRemoteDataSource {

    private final FirebaseFirestore db;


    public UserDataRemoteDataSource() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void saveUserData(User user) {
        db.collection("Users")
                .document(user.getEmail())
                .set(user)
                .addOnSuccessListener(unused -> userResponseCallback.onSuccessUserSaved(user))
                .addOnFailureListener(e -> userResponseCallback.onFailureAuthentication(e.getLocalizedMessage()));

    }

    @Override
    public void getUserCountry(User user) {
        db.collection("Users")
                .document(user.getEmail())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String country = documentSnapshot.getString("countryOfInterest");
                    userResponseCallback.onCountryGotSuccess(new User(user.getName(), user.getEmail(), user.getUid(), country));
                })
                .addOnFailureListener(e -> userResponseCallback.onFailureAuthentication(e.getLocalizedMessage()));
    }

}
