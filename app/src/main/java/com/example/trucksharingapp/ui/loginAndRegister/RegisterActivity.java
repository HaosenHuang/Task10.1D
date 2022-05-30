package com.example.trucksharingapp.ui.loginAndRegister;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.trucksharingapp.R;
import com.example.trucksharingapp.db.AppDataBase;
import com.example.trucksharingapp.model.UserModel;
import com.example.trucksharingapp.utli.GlideEngine;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.language.LanguageConfig;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.io.File;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText fullName;
    private EditText account;
    private EditText password;
    private EditText confirmPassword;
    private EditText phone;
    private Button register;
    private String photoPath = "";
    private FrameLayout allImageLayout;
    private ImageView addImage;
    private ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        fullName = (EditText) findViewById(R.id.fullName);
        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        phone = (EditText) findViewById(R.id.phone);
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);

        allImageLayout = (FrameLayout) findViewById(R.id.allImageLayout);
        addImage = (ImageView) findViewById(R.id.addImage);
        photo = (ImageView) findViewById(R.id.photo);
        allImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                submit();
                break;
        }
    }

    /**
     * Load selection avatar frame
     */
    private void selectImage() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .isAndroidQTransform(true)
                .maxSelectNum(1)
                .imageEngine(GlideEngine.createGlideEngine())

                .setLanguage(LanguageConfig.ENGLISH)
                .selectionMode(PictureConfig.SINGLE)
                .previewImage(true)
                .previewVideo(true)
                .queryMaxFileSize(50)
                .isCamera(false)
                .compress(true)
                .isGif(false)
                .isSingleDirectReturn(true)

                .synOrAsy(false)
                .enableCrop(true)
//                .circleDimmedLayer(true)
                .showCropFrame(false)

                .showCropGrid(false)
                .minimumCompressSize(100)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        Log.e("TAG", "onCancel:result" + result.size());

                        if (result.size() > 0) {
                            LocalMedia media = result.get(0);
                            if (media.getCutPath() != null && !media.getCutPath().equals("")) {
                                loadImageByPath(media.getCutPath());
                            } else if (media.getCompressPath() != null && !media.getCompressPath().equals("")) {
                                loadImageByPath(media.getCompressPath());
                            } else if (media.getPath() != null && !media.getPath().equals("")) {
                                loadImageByPath(media.getPath());
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Selection failed. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(RegisterActivity.this, "You have deselected", Toast.LENGTH_SHORT).show();
                    }

                });
//                        .setLanguage(PictureSelectionConfig.)
    }

    /**
     * load image
     * @param cutPath
     */
    private void loadImageByPath(String cutPath) {

        photoPath = cutPath;
        photo.setVisibility(View.VISIBLE);
        addImage.setVisibility(View.GONE);

        Glide.with(this)
                .load(new File(cutPath))
                .into(photo);

    }

    private void submit() {
        // validate

        if (photoPath.equals("")){
            Toast.makeText(this, "Please add a image!", Toast.LENGTH_SHORT).show();

            return;
        }

        String fullNameString = fullName.getText().toString().trim();
        if (TextUtils.isEmpty(fullNameString)) {
            Toast.makeText(this, "Please enter the full name!", Toast.LENGTH_SHORT).show();
            return;
        }

        String accountString = account.getText().toString().trim();
        if (TextUtils.isEmpty(accountString)) {
            Toast.makeText(this, "Please enter the username!", Toast.LENGTH_SHORT).show();
            return;
        }
        String passwordString = password.getText().toString().trim();
        if (TextUtils.isEmpty(passwordString)) {
            Toast.makeText(this, "Please input a password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (passwordString.length() <= 5) {
            Toast.makeText(this, "Please enter a password of 6 digits or more!", Toast.LENGTH_SHORT).show();
            return;
        }
        String confirmPasswordString = confirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(confirmPasswordString)) {
            Toast.makeText(this, "Please enter the confirm password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!passwordString.equals(confirmPasswordString)) {
            Toast.makeText(this, "The passwords entered twice are inconsistent!", Toast.LENGTH_SHORT).show();
            return;
        }

        String phoneString = phone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneString)) {
            Toast.makeText(this, "Please enter the phone number!", Toast.LENGTH_SHORT).show();
            return;
        }
        register();
    }

    private void register() {
        String accountString = account.getText().toString().trim();
        String passwordString = password.getText().toString().trim();
        String fullNameString = fullName.getText().toString().trim();
        String phoneString = phone.getText().toString().trim();

        UserModel user = AppDataBase.getInstance().userDao().getByAccount(accountString);

        if (user != null) {
            Toast.makeText(this, "This account has been registered!", Toast.LENGTH_SHORT).show();
            return;
        }
        UserModel data = new UserModel()
                .setUserId(System.currentTimeMillis() + "")
                .setHeadPortraitView(photoPath)
                .setFullName(fullNameString)
                .setPhoneString(phoneString)
                .setAccount(accountString)
                .setPassword(passwordString);
        AppDataBase.getInstance().userDao().insert(data);
        finish();
        Toast.makeText(this, "Congratulations on your successful registration!", Toast.LENGTH_SHORT).show();
    }
}