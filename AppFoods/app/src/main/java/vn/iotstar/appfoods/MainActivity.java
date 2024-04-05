package vn.iotstar.appfoods;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.iotstar.appfoods.Utils.RealPathUtil;

public class MainActivity extends AppCompatActivity {
    Button btnChoose, btnUpload;
    ImageView imageViewChoose, imageViewUpload;
    EditText editTextUserName;
    TextView textViewUserName;
    private Uri mUri;
    private ProgressDialog mProgressDialog;
    public static final int MY_REQUEST_CODE = 100;
    public static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AnhXa();

        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Please wait upload...");

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckPermission();
                //chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUri != null) {
                    uploadImage1();
                } else {
                    Toast.makeText(MainActivity.this, "mUri null", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void uploadImage1() {
        mProgressDialog.show();

        String username = editTextUserName.getText().toString().trim();
        RequestBody requestUserName = RequestBody.create(MediaType.parse("multipart/form-data"), username);

        String IMAGE_PATH = RealPathUtil.getRealPath(this, mUri);
        Log.e("ffff", IMAGE_PATH);
        File file = new File(IMAGE_PATH);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part partbodyavatar =
                MultipartBody.Part.createFormData(Const.MY_IMAGES, file.getName(), requestFile);

        ServiceAPI.serviceapi.upload(requestUserName, partbodyavatar).enqueue(new Callback<List<ImagesUpload>>() {
            @Override
            public void onResponse(Call<List<ImagesUpload>> call, Response<List<ImagesUpload>> response) {
                mProgressDialog.dismiss();
                List<ImagesUpload> imagesUpload = response.body();
                if (imagesUpload.size() > 0) {
                    for (int i = 0; i < imagesUpload.size(); i++) {
                        textViewUserName.setText(imagesUpload.get(i).getUsername());
                        Glide.with(MainActivity.this)
                                .load(imagesUpload.get(i).getAvatar())
                                .into(imageViewUpload);
                        Toast.makeText(MainActivity.this, "Thành công", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Thất bại", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ImagesUpload>> call, Throwable t) {
                mProgressDialog.dismiss();
                Log.e("TAG", t.toString());
                Toast.makeText(MainActivity.this, "Gọi APT thất bại", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void AnhXa() {
        btnChoose = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imageViewUpload = findViewById(R.id.imgMultipart);
        editTextUserName = findViewById(R.id.editUserName);
        textViewUserName = findViewById(R.id.tvUserName);
        imageViewUpload = findViewById(R.id.imgChoose);
    }

    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }

    private void CheckPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        }
        else {
            requestPermissions(permissions(), MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Chọn ảnh"));
    }

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.e(TAG, "onActivityResult");
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            return;
                        }
                        Uri uri = data.getData();
                        mUri = uri;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imageViewChoose.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
}