package app.num.barcodescannerproject;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static app.num.barcodescannerproject.R.id.resultText;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {


    private ZXingScannerView mScannerView;

    private Activity mainActivity = this;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_MICROPHONE = 3;
    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    private TextView resultText;
    private Bundle savedInstanceState;

    WebView web;

    //comm
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cameraBtn = (Button)findViewById(R.id.cameraBtn);

        // Button storageBtn = (Button)findViewById(R.id.storageBtn);
        // Button micBtn = (Button)findViewById(R.id.micBtn);

        resultText = (TextView)findViewById(R.id.resultText);

        cameraBtn.setOnClickListener(buttonClickListener);
        // storageBtn.setOnClickListener(buttonClickListener);
        // micBtn.setOnClickListener(buttonClickListener);

        // webview
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(1);
        web = new WebView(this);
        WebSettings webSet = web.getSettings();
        webSet.setUseWideViewPort(true);
        webSet.setJavaScriptEnabled(true);
        webSet.setJavaScriptCanOpenWindowsAutomatically(true);
        webSet.setBuiltInZoomControls(false);
        webSet.setAllowFileAccessFromFileURLs(true);
        webSet.setAllowUniversalAccessFromFileURLs(true);
        webSet.setSupportMultipleWindows(true);
        webSet.setSaveFormData(false);
        webSet.setSavePassword(false);
        webSet.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient());
        web.loadUrl("http://45.76.205.98/");
        layout.addView(web);
        setContentView(layout);
    }

    public void onBackPressed() {
        if(web.canGoBack()) web.goBack();
        else finish();
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.cameraBtn:
                    int permissionCamera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                    if(permissionCamera == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                    } else {
                        resultText.setText("camera permission authorized");
                    }
                    break;
                // case R.id.storageBtn:
                //     int permissionReadStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                //     int permissionWriteStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                //     if(permissionReadStorage == PackageManager.PERMISSION_DENIED || permissionWriteStorage == PackageManager.PERMISSION_DENIED) {
                //         ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
                //     } else {
                //         resultText.setText("read/write storage permission authorized");
                //     }
                //     break;
                // case R.id.micBtn:
                //     int permissionAudio = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
                //     if(permissionAudio == PackageManager.PERMISSION_DENIED) {
                //         ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_MICROPHONE);
                //     } else {
                //         resultText.setText("recording audio permission authorized");
                //     }
                //     break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(Manifest.permission.CAMERA)) {
                        if(grantResult == PackageManager.PERMISSION_GRANTED) {
                            resultText.setText("camera permission authorized");
                        } else {
                            resultText.setText("camera permission denied");
                        }
                    }
                }
                break;
            // case REQUEST_EXTERNAL_STORAGE:
            //     for (int i = 0; i < permissions.length; i++) {
            //         String permission = permissions[i];
            //         int grantResult = grantResults[i];
            //         if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //             if(grantResult == PackageManager.PERMISSION_GRANTED) {
            //                 resultText.setText("read/write storage permission authorized");
            //             } else {
            //                 resultText.setText("read/write storage permission denied");
            //             }
            //         }
            //     }
            //     break;
            // case REQUEST_MICROPHONE:
            //     for (int i = 0; i < permissions.length; i++) {
            //         String permission = permissions[i];
            //         int grantResult = grantResults[i];
            //         if (permission.equals(Manifest.permission.RECORD_AUDIO)) {
            //             if(grantResult == PackageManager.PERMISSION_GRANTED) {
            //                 resultText.setText("recording audio permission authorized");
            //             } else {
            //                 resultText.setText("recording audio permission denied");
            //             }
            //         }
            //     }
            //     break;
        }
    }
    //commm

    public void QrScanner(View view){


        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);

        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera

    }

    // @Override
    // public void onPause() {
    //     super.onPause();
    //     mScannerView.stopCamera();           // Stop camera on pause
    // }

    @Override
    public void handleResult(Result rawResult) {

        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)

        // show the scanner result into dialog box.
        // Do something with the result here
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(rawResult.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();

        // If you would like to resume scanning, call this method below:
        // mScannerView.resumeCameraPreview(this);
    }
}


//////
