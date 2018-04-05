package ca.realsense.realsensegtadev.data;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by joewang on 2017-08-31.
 */

public class DatabaseManager {

    private static com.google.firebase.database.FirebaseDatabase firebaseDatabase = null;
    private static DatabaseReference firebaseDatabaseRefrence = null;

    public static DatabaseReference getDatabaseReference() {
        if (firebaseDatabase == null) {
            firebaseDatabase = com.google.firebase.database.FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
            firebaseDatabaseRefrence = firebaseDatabase.getReference();
        }
        return firebaseDatabaseRefrence;
    }
}
