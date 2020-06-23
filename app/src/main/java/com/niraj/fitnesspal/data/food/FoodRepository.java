package com.niraj.fitnesspal.data.food;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.niraj.fitnesspal.data.helpers.ActionWrapper;
import com.niraj.fitnesspal.data.helpers.DatabaseCompletionListener;
import com.niraj.fitnesspal.data.helpers.FirebaseChildListener;
import com.niraj.fitnesspal.data.session.SessionRepository;
import com.niraj.fitnesspal.utils.Result;

import java.util.HashMap;
import java.util.Map;

import static com.niraj.fitnesspal.data.Constant.TABLE_FOOD;
import static com.niraj.fitnesspal.data.DatabaseNames.DELETED_FLAG;

public class FoodRepository {
    private static final String TAG = FoodRepository.class.getSimpleName();

    private static FoodRepository instance;
    private final FirebaseDatabase firebaseInstance = FirebaseDatabase.getInstance();
    private DatabaseCompletionListener databaseCompletionListener;

    private FoodRepository(Context context) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseCompletionListener = new DatabaseCompletionListener(context);
    }

    public static FoodRepository getInstance(Context context) {
        if (instance == null) {
            instance = new FoodRepository(context);
        }
        return instance;
    }

    private DatabaseReference getFoodDatabaseRef() {
        String userId = SessionRepository.getInstance().getUserId();
        return firebaseInstance.getReference(TABLE_FOOD + "/" + userId);
    }

    public void getItems(Result.OnResultData<ActionWrapper<FoodEntry>> onResult) {
        getFoodDatabaseRef()
                .orderByChild(DELETED_FLAG)
                .equalTo(false)
                .addChildEventListener(new FirebaseChildListener<FoodEntry>() {
                    @Override
                    public void onChildChanged(FoodEntry snapShot) {
                        onResult.onResult(Result.success(new ActionWrapper<>(snapShot, ActionWrapper.Action.UPDATED)));
                    }

                    @Override
                    public void onChildAdded(FoodEntry snapShot) {
                        onResult.onResult(Result.success(new ActionWrapper<>(snapShot, ActionWrapper.Action.ADDED)));
                    }

                    @Override
                    public void onChildRemoved(FoodEntry snapShot) {
                        onResult.onResult(Result.success(new ActionWrapper<>(snapShot, ActionWrapper.Action.REMOVED)));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void createOrUpdateItem(final FoodEntry food, Result.OnResult onResult) {
        String key;
        if (TextUtils.isEmpty(food.getId())) {
            key = getFoodDatabaseRef().push().getKey();
        } else {
            key = food.getId();
        }
        Map<String, Object> postValues = food.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, postValues);
        databaseCompletionListener.updateChildren(getFoodDatabaseRef(), onResult, childUpdates);
    }

    public void deleteItem(final String id, Result.OnResult onResult) {
        databaseCompletionListener.removeChildren(getFoodDatabaseRef(), onResult, id, DELETED_FLAG);
    }
}