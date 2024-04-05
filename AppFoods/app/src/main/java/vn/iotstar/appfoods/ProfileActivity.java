package vn.iotstar.appfoods;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    TextView id, userName,fName, userEmail, gender;
    Button btnLogout;
    ImageView imageViewprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            id = findViewById(R.id.textViewId);
            userName = findViewById(R.id.textViewUsername);
            fName = findViewById(R.id.textViewFName);
            userEmail = findViewById(R.id.textViewEmail);
            gender = findViewById(R.id.textViewGender);
            btnLogout = findViewById(R.id.buttonLogout);
            imageViewprofile = findViewById(R.id.imageViewProfile);

            User user = SharedPrefManager.getInstance(this).getUSer();
            id.setText(String.valueOf(user.getId()));
            userEmail.setText(user.getEmail());
            fName.setText(user.getFname());
            gender.setText(user.getGender());
            userName.setText(user.getName());
            Glide.with(getApplicationContext())
                    .load(user.getImages())
                    .into(imageViewprofile);
            btnLogout.setOnClickListener(this);

            // Thêm sự kiện lắng nghe cho imageViewprofile
            imageViewprofile.setOnClickListener(this);
        } else {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnLogout)) {
            SharedPrefManager.getInstance(getApplicationContext()).logout();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (view.equals(imageViewprofile)) {
            // Nếu nhấn vào imageViewprofile, mở MainActivity
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            User user = SharedPrefManager.getInstance(this).getUSer();
            intent.putExtra("user", user); // Truyền dữ liệu qua Intent
            startActivity(intent);
        }
    }
}