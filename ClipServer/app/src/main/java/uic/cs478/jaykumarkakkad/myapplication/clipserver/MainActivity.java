package uic.cs478.jaykumarkakkad.myapplication.clipserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onStart() {
        super.onStart() ;
        if (ActivityCompat.checkSelfPermission(
                this,"android.permission.POST_NOTIFICATIONS")
                == PackageManager.PERMISSION_DENIED)
        {
            requestPermissions(new String[] {"android.permission.POST_NOTIFICATIONS"}, 0) ;
        }
    }

    public void onRequestPermissionsResult(int code, String[] permissions, int[] result) {
        super.onRequestPermissionsResult(code, permissions, result) ;
        if (result.length >0) {
            if (result[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "App will not show notifications", Toast.LENGTH_SHORT).show() ;
            }
        }
    }
}