package com.example.clockme_in.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.clockme_in.Controller.callfunction;
import com.example.clockme_in.R;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class cameraview extends AppCompatActivity {


    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private PreviewView previewView;
    private ExecutorService cameraExecutor;
    final String url="https://clockin.smartskep.com/";
    private com.google.mlkit.vision.face.FaceDetector faceDetector;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameraview);



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }


        previewView = findViewById(R.id.previewView);
        cameraExecutor = Executors.newSingleThreadExecutor();

        initializeFaceDetector();
        initializeCamera();
        getImage();
    }

    public void getImage(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        callfunction retrofitAPI  = retrofit.create(callfunction.class);
        retrofitAPI.getImage("getImage","email").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        byte[] bytes = response.body().bytes();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageView.setImageBitmap(bitmap);
                        detectFaces(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(cameraview.this, "Failed to fetch image", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(cameraview.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void detectFaces(Bitmap bitmap) {
        // Initialize the FaceDetector
        FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.ACCURATE_MODE)
                .build();

        // Process the image
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> faces = detector.detect(frame);

        // Iterate through detected faces
        for (int i = 0; i < faces.size(); ++i) {
            Face face = faces.valueAt(i);
            // Extract face information (e.g., face position, landmarks)
        }

        // Release resources
        detector.release();
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeCamera();
            } else {
                Toast.makeText(this, "Permission not Granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initializeFaceDetector() {
        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST) 
                .build();
        faceDetector = FaceDetection.getClient(options);
    }
    private void initializeCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                        .build();

                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }
}