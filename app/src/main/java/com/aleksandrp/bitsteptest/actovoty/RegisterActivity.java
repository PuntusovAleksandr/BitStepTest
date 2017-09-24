package com.aleksandrp.bitsteptest.actovoty;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aleksandrp.bitsteptest.BuildConfig;
import com.aleksandrp.bitsteptest.R;
import com.aleksandrp.bitsteptest.api.service.ServiceApi;
import com.aleksandrp.bitsteptest.presenter.LoginPresenter;
import com.aleksandrp.bitsteptest.presenter.interfaces.MvpActionView;
import com.aleksandrp.bitsteptest.utils.FileUtils;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aleksandrp.bitsteptest.utils.FileUtils.onCaptureImageResult;
import static com.aleksandrp.bitsteptest.utils.ShowImages.showImageFromFile;
import static com.aleksandrp.bitsteptest.utils.ShowToast.showMessageError;

public class RegisterActivity extends AppCompatActivity implements MvpActionView {

    @Bind(R.id.progressBar_registration)
    RelativeLayout progressBar_registration;

    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Bind(R.id.iv_icon_user)
    ImageView iv_icon_user;

    @Bind(R.id.tv_sign_up)
    TextView tv_sign_up;

    @Bind(R.id.et_email)
    EditText et_email;
    @Bind(R.id.et_organisation)
    EditText et_organisation;
    @Bind(R.id.et_locale)
    EditText et_locale;
    @Bind(R.id.et_phone)
    EditText et_phone;
    @Bind(R.id.et_site)
    EditText et_site;
    @Bind(R.id.et_password)
    EditText et_password;
    @Bind(R.id.et_password_confirm)
    EditText et_password_confirm;


    private LoginPresenter mPresenter;
    private Intent serviceIntent;


    private Uri sourceImageUri;
    private String mPath;
    public static final int CAMERA_CAPTURE = 1,
            GALLERY_REQUEST = 2,
            GALLERY_KITKAT_INTENT_CALLED = 55,
            PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        iv_back.setVisibility(View.VISIBLE);

        serviceIntent = new Intent(this, ServiceApi.class);
        mPresenter = new LoginPresenter();
        mPresenter.setMvpView(this);
        mPresenter.init();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.registerSubscriber();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscriber();
    }


    @Override
    public void onDestroy() {
        mPresenter.unRegisterSubscriber();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
        stopService(serviceIntent);
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        super.onBackPressed();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == GALLERY_REQUEST ||
                    requestCode == GALLERY_KITKAT_INTENT_CALLED) {            //returning from gallery
                String mExtension = getFile(data.getData());

                if (mExtension.endsWith(".png") || mExtension.endsWith(".jpg")
                        || mExtension.endsWith(".jpeg") || mExtension.endsWith(".doc")
                        || mExtension.endsWith(".docx") || mExtension.endsWith(".rtf")
                        || mExtension.endsWith(".txt")) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (requestCode == GALLERY_KITKAT_INTENT_CALLED) {
                            Uri sendUri = data.getData();
                            final int takeFlags = data.getFlags()
                                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            // Check for the freshest data.
                            getContentResolver().takePersistableUriPermission(sendUri, takeFlags);
                        }
                    }
                    String stringUri = data.getData().toString();
                    File file = onCaptureImageResult(data.getData(), this);
                    if (file.exists()) {
                        this.mPath = file.getPath();
                    }
                    parseAnswer(file);
                } else {
                    showMessageError(getResources().getString(R.string.unkown_format));
                }

            } else if (requestCode == PERMISSION_REQUEST_CODE) {
                showDialogSelectPhoto();
            } else if (requestCode == CAMERA_CAPTURE) {                //returning from camera
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Intent intent = new Intent();
                    intent.setData(sourceImageUri);
                    onActivityResult(GALLERY_REQUEST,  resultCode,  intent);
                } else {
                    String path = sourceImageUri.getPath();
                    File file = new File(path);
                    FileUtils.reloadGallery(this, file);
                    this.mPath = path;
                    parseAnswer(file);
                }
            }
        }
    }
//    ==================================================

    @OnClick(R.id.iv_back)
    public void iv_backClick() {
        onBackPressed();
    }

    @OnClick(R.id.tv_sign_up)
    public void tv_sign_upClick() {
        register();
    }

    @OnClick(R.id.iv_icon_user)
    public void iv_icon_userClick() {
        showDialogSelectPhoto();
    }

    //    ==================================================

    private void register() {
    }



    private void parseAnswer(File file) {
        if (file.exists()) {
            Uri uri = Uri.parse(mPath);
            showImageFromFile(file, iv_icon_user);
        } else {
            iv_icon_user.setImageResource(0);
            this.mPath = "";
        }
    }

    //    ================================================
//    add PHOTO
//    ================================================
    public void showDialogSelectPhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.from_save_file)
                .setNegativeButton(R.string.make_photo,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                makeNewPhoto();
                                dialog.cancel();
                            }
                        })
                .setPositiveButton(R.string.from_gallery,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectPhotoFromGallery();
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * male photo camera
     */
    private void makeNewPhoto() {
        if (hasPermissions()) {
            // our app has permissions.
            try {
                Intent captureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    sourceImageUri = FileProvider.getUriForFile(this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            FileUtils.getNewFile());
                } else {
                    sourceImageUri = getOutputMediaFileUri();
                }
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, sourceImageUri);
                startActivityForResult(captureIntent, CAMERA_CAPTURE);
            } catch (ActivityNotFoundException e) {
                showMessageError(getResources().getString(R.string.camera_error));
            }
        } else {
            //our app doesn't have permissions, So i m requesting permissions.
            requestPermissionWithRationale();
        }

    }

    /**
     * select from gallery
     */
    private void selectPhotoFromGallery() {
        if (hasPermissions()) {
            // our app has permissions.
            Intent intent;
            int request = GALLERY_REQUEST;
            try {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    request = GALLERY_KITKAT_INTENT_CALLED;
                }
                intent.setType("image/*");
                startActivityForResult(intent, request);
            } catch (ActivityNotFoundException e) {
                showMessageError(getResources().getString(R.string.camera_gallery));
            }

        } else {
            //our app doesn't have permissions, So i m requesting permissions.
            requestPermissionWithRationale();
        }

    }

    private boolean hasPermissions() {
        int res = 0;
        //string array of permissions,
        String[] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        for (String perms : permissions) {
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
    }

    public void requestPermissionWithRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            final String message = "Storage permission is needed to show files count";
            showSnack(message);

        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            final String message = "Storage permission is needed to write files";
            showSnack(message);

        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            final String message = "Camera permission is needed to make photo";
            showSnack(message);

        } else {
            requestPerms();
        }

    }

    public void showSnack(String mMessage) {
        Snackbar.make(this.findViewById(R.id.progressBar_registration), mMessage, Snackbar.LENGTH_LONG)
                .setAction("GRANT", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestPerms();
                    }
                })
                .show();
    }

    private void requestPerms() {
        String[] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                for (int res : grantResults) {
                    // if user granted all permissions.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                // if user not granted permissions.
                allowed = false;
                break;
        }

        if (allowed) {
            //user granted all permissions we can perform our task.
            showDialogSelectPhoto();
        } else {
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(this, R.string.camera_permission_denied, Toast.LENGTH_SHORT).show();
                } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, R.string.storagel_permission_denied, Toast.LENGTH_SHORT).show();
                } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, R.string.storagel_permission_denied, Toast.LENGTH_SHORT).show();
                } else {
                    showNoStoragePermissionSnackbar();
                }
            }
        }
    }

    public void showNoStoragePermissionSnackbar() {
        Snackbar.make(this.findViewById(R.id.progressBar_registration), R.string.permissions_not_granted, Snackbar.LENGTH_LONG)
                .setAction("SETTINGS", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings();

                        Toast.makeText(getApplicationContext(),
                                R.string.open_permissions_grant,
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .show();
    }

    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE);
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(FileUtils.getNewFile());
    }


    private String getFile(Uri uri) {

        String fileName, scheme = uri.getScheme();
        switch (scheme) {
            case "file":
                fileName = uri.getLastPathSegment();
                break;
            case "content":
                fileName = uri.getLastPathSegment();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.getCount() != 0) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    cursor.moveToFirst();
                    fileName = cursor.getString(columnIndex);
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                break;
            default:
                fileName = uri.getLastPathSegment();
                break;
        }
        return fileName;
    }
}
