package com.niraj.fitnesspal.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.niraj.fitnesspal.R;
import com.niraj.fitnesspal.screens.helper.PhotoPickHelper;
import com.niraj.fitnesspal.screens.loginandregister.LoginActivity;

import java.io.File;

public class HomeActivity extends AppCompatActivity implements PhotoPickHelper.PhotoPickCallback {
    private static String TAG = "HomeActivity";
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    private Toolbar toolbar;
    private ImageView preview, createIcon;
    private ImageButton clearIcon;
    private Button scanBtn;
    private View foodStatusLayout;

    private PhotoPickHelper photoPickHelper = new PhotoPickHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.homeToolbar);
        scanBtn = findViewById(R.id.scanBtn);
        preview = findViewById(R.id.preview);
        createIcon = findViewById(R.id.createIcon);
        clearIcon = findViewById(R.id.clearIcon);
        foodStatusLayout = findViewById(R.id.foodStatusLayout);

        setSupportActionBar(toolbar);

        createIcon.setOnClickListener(v -> showImageSelection());
        clearIcon.setOnClickListener(v -> removeImage());
        photoPickHelper.addPhotoPickCallback(this);
        photoPickHelper.setActivity(this);
    }

    private void removeImage() {
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
            mFirebaseAuth.signOut();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        }
        return true;
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
}
