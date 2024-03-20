package it.sal.disco.unimib.charityhub.data.source.user;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import it.sal.disco.unimib.charityhub.model.User;

public class UserAuthenticationDataSource extends BaseUserAuthenticationDataSource {

    private final FirebaseAuth firebaseAuth;

    public UserAuthenticationDataSource() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void logIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null) {
                    userResponseCallback.onSuccessAuthentication(new User(firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getUid()));
                }
                else {
                    userResponseCallback.onFailureAuthentication("Error getting user");
                }
            }
            else {
                userResponseCallback.onFailureAuthentication(task.getException().getLocalizedMessage());
            }
        }).addOnFailureListener(e -> {
            userResponseCallback.onFailureAuthentication(e.getLocalizedMessage());
        });
    }

    @Override
    public void signIn(String email, String password, String fullName) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null) {
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(fullName).build();
                    firebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()) {
                            userResponseCallback.onSuccessAuthentication(new User(firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getUid()));
                        }
                        else {
                            userResponseCallback.onFailureAuthentication(task.getException().getLocalizedMessage());
                        }
                    }).addOnFailureListener(e -> {
                        userResponseCallback.onFailureAuthentication(e.getLocalizedMessage());
                    });
                }
            }
            else {
                userResponseCallback.onFailureAuthentication(task.getException().getLocalizedMessage());
            }
        }).addOnFailureListener(e -> {
           userResponseCallback.onFailureAuthentication(e.getLocalizedMessage());
        });
    }

    @Override
    public void logOut() {
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    firebaseAuth.removeAuthStateListener(this);
                    userResponseCallback.onSuccessLogout();
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
        firebaseAuth.signOut();
    }

    @Override
    public User getLoggedUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null) {
            return new User(firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getUid());
        }
        else {
            return null;
        }
    }
}
