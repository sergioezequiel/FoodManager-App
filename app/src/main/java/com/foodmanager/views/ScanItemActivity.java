package com.foodmanager.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.foodmanager.R;
import com.foodmanager.listeners.ScannedBarcodeListener;
import com.foodmanager.models.CodigoBarras;
import com.foodmanager.models.SingletonDatabaseManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.Result;

import java.util.Objects;

public class ScanItemActivity extends AppCompatActivity implements ScannedBarcodeListener {

    private CodeScanner mCodeScanner;
    private ToneGenerator toneGen1;
    private TextView barcodeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_item);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.nivel6)));

        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        barcodeText = findViewById(R.id.barcode_text);

        FloatingActionButton fab = findViewById(R.id.fabScanItem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ScanItemActivity.this, barcodeText.getText(), Toast.LENGTH_SHORT).show();
                //addItemDialog(barcodeText.getText());

            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 123);
        } else {
            startScanning();
        }

    }

    private void startScanning() {
        CodeScannerView mCodeScannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, mCodeScannerView);
        mCodeScanner.startPreview();   // this line is very important, as you will not be able to scan your code without this, you will only get blank screen
        SingletonDatabaseManager.getInstance(getApplicationContext()).setScannedBarcodeListener(this);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Debug", "Decode");
                        barcodeText.setText(result.getText());
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                        vibrateDevice(getApplicationContext());
                        SingletonDatabaseManager.getInstance(getApplicationContext()).getCodigoBarrasAPI(result.getText(), getApplicationContext());
                    }
                });
            }
        });

        //now if you want to scan again when you click on scanner then do this.
        mCodeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcodeText.setText("");
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                startScanning();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void vibrateDevice(Context mContext) {
        Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(150);
    }

    /*Edit Values Dialog*/
    /*public void addItemDialog(CharSequence productName) {
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View titleView = inflater.inflate(R.layout.alert_dialog_add_scan_inventory_title, null);

        final AlertDialog diag = new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setCustomTitle(titleView)
                .setPositiveButton(Html.fromHtml("<font color='#FEB117'><strong>Add Item</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setView(R.layout.alert_dialog_add_scan_inventory_body)
                .create();
        diag.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(diag.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        diag.getWindow().setAttributes(lp);


        Button btnAddQty = diag.findViewById(R.id.btn_alert_dialog_add_item_qty);
        Button btnRemoveQty = diag.findViewById(R.id.btn_alert_dialog_remove_item_qty);
        final TextView txtQty = diag.findViewById(R.id.txt_qty_item);
        TextView txtProductName = diag.findViewById(R.id.txt_alert_dialog_product_name);
        txtProductName.setText(productName);

        btnAddQty.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt((String) txtQty.getText());
                qty += 1;
                txtQty.setText(Integer.toString(qty));
            }
        });

        btnRemoveQty.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt((String) txtQty.getText());
                if (qty > 1)
                    qty -= 1;
                txtQty.setText(Integer.toString(qty));
            }
        });

    }*/


    @Override
    public void openAddDialog(CodigoBarras barcode) {
        Log.d("Debug", "Entrou no openAddDialog");
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View titleView = inflater.inflate(R.layout.alert_dialog_add_scan_inventory_title, null);

        final AlertDialog diag = new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setCustomTitle(titleView)
                .setPositiveButton(Html.fromHtml("<font color='#FEB117'><strong>Add Item</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setView(R.layout.alert_dialog_add_scan_inventory_body)
                .create();
        diag.show();
        Log.d("Debug", "Aberto o dialog");

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(diag.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        diag.getWindow().setAttributes(lp);


        Button btnAddQty = diag.findViewById(R.id.btn_alert_dialog_add_item_qty);
        Button btnRemoveQty = diag.findViewById(R.id.btn_alert_dialog_remove_item_qty);
        final TextView txtQty = diag.findViewById(R.id.txt_qty_item);
        TextView txtProductName = diag.findViewById(R.id.txt_alert_dialog_product_name);
        TextView txtDescription = diag.findViewById(R.id.product_description);
        txtProductName.setText(barcode.getNome());
        txtDescription.setText(barcode.getMarca());

        btnAddQty.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt((String) txtQty.getText());
                qty += 1;
                txtQty.setText(Integer.toString(qty));
            }
        });

        btnRemoveQty.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt((String) txtQty.getText());
                if (qty > 1)
                    qty -= 1;
                txtQty.setText(Integer.toString(qty));
            }
        });
    }
}