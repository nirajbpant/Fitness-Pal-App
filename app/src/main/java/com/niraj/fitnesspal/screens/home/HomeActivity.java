package com.niraj.fitnesspal.screens.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.niraj.fitnesspal.R;
import com.niraj.fitnesspal.screens.base.BaseActivity;
import com.niraj.fitnesspal.screens.helper.Classifier;
import com.niraj.fitnesspal.screens.helper.PhotoPickHelper;
import com.niraj.fitnesspal.screens.loginandregister.LoginActivity;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HomeActivity extends BaseActivity implements PhotoPickHelper.PhotoPickCallback {
    private static final int mInputSize = 256;
    private static final String mModelPath = "model.tflite";
    private static final String mLabelPath = "labels_food.txt";
    private static String TAG = "HomeActivity";
    private Toolbar toolbar;
    private ImageView preview, createIcon;
    private TextView foodName, foodStatus;
    private ImageButton clearIcon, addIcon;
    private Button scanBtn;
    private View foodStatusLayout;
    private Classifier classifier;

    private PhotoPickHelper photoPickHelper = new PhotoPickHelper(this);
    private HomeViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.homeToolbar);
        scanBtn = findViewById(R.id.scanBtn);
        preview = findViewById(R.id.preview);
        createIcon = findViewById(R.id.createIcon);
        clearIcon = findViewById(R.id.clearIcon);
        addIcon = findViewById(R.id.addIcon);
        foodName = findViewById(R.id.foodName);
        foodStatus = findViewById(R.id.foodStatus);
        foodStatusLayout = findViewById(R.id.foodStatusLayout);

        setSupportActionBar(toolbar);

        initClassifier();

        createIcon.setOnClickListener(v -> showImageSelection());
        clearIcon.setOnClickListener(v -> removeImage());
        photoPickHelper.addPhotoPickCallback(this);
        photoPickHelper.setActivity(this);
        scanBtn.setOnClickListener(v -> predict());
    }

    private void initClassifier() {
        AsyncTask.execute(() -> {
            if (classifier != null) return;
            try {
                classifier = new Classifier(getAssets(), mModelPath, mLabelPath, mInputSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void removeImage() {
        scanBtn.setEnabled(false);
        createIcon.setVisibility(View.VISIBLE);
        foodStatusLayout.setVisibility(View.GONE);

        clearIcon.setVisibility(View.GONE);
        preview.setVisibility(View.GONE);
    }

    private void showImageSelection() {
        // Menu for selecting either: a) take new photo b) select from existing
        PopupMenu popup = new PopupMenu(this, createIcon);
        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.select_images_from_local) {
                photoPickHelper.requestPickPhoto();
                return true;
            } else if (itemId == R.id.take_photo_using_camera) {
                photoPickHelper.requestTakePhoto();
                return true;
            }
            return false;
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.camera_button_menu, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.actionLogout) {
            logoutUser();
        }
        return true;
    }

    private void logoutUser() {
        showLoading();
        viewModel.logoutUser(result -> {
            hideLoading();
            switch (result.getStatus()) {
                case SUCCESS:
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finishAffinity();
                    break;
                case ERROR:
                    showToast("Something went wrong");
                    break;
            }
        });
    }

    @Override
    public void showError(boolean b, Throwable e) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        photoPickHelper.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setUpImage(String currentPhotoPath) {
        scanBtn.setEnabled(true);

        createIcon.setVisibility(View.GONE);
        foodStatusLayout.setVisibility(View.GONE);

        clearIcon.setVisibility(View.VISIBLE);
        preview.setVisibility(View.VISIBLE);

        preview.setImageURI(Uri.fromFile(new File(currentPhotoPath)));
    }

    private void predict() {
        scanBtn.setEnabled(false);
        scanBtn.setText(R.string.loading_text);
        Bitmap bitmap = ((BitmapDrawable) preview.getDrawable()).getBitmap();
        showLoading();
        AsyncTask.execute(() -> {
            if (classifier != null) {
                List<Classifier.Recognition> result = classifier.recognizeImage(bitmap);
                runOnUiThread(() -> {
                            hideLoading();
                            scanBtn.setEnabled(true);
                            scanBtn.setText(R.string.predict_text);
                            if (result.size() > 0) {
                                foodStatusLayout.setVisibility(View.VISIBLE);
                                Classifier.FoodItem foodItem = result.get(0).getFoodItem();
                                foodName.setText(String.format(getString(R.string.food_name_format), foodItem.getTitle(), foodItem.getItemUnit()));
                                foodStatus.setText(String.format(getString(R.string.food_status_format), foodItem.getCalories(), foodItem.getCarb(), foodItem.getProtein(), foodItem.getFat()));
                                addIcon.setOnClickListener(v -> onItemAdded(foodItem));
                            } else {
                                foodStatusLayout.setVisibility(View.GONE);
                                showToast("No item found");
                            }
                        }
                );
            } else {
                Log.e(TAG, "Classifier was null");
            }
        });

    }

    private void onItemAdded(Classifier.FoodItem foodItem) {
        showLoading();
        viewModel.addFoodItem(foodItem, 1, result -> {
            hideLoading();
            switch (result.getStatus()) {
                case SUCCESS:
                    showToast("Item Added");
                    break;
                case ERROR:
                    showToast(result.getMessage());
                    break;
            }
        });
    }
}
