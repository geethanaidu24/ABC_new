package com.example.admin.abc;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import static com.example.admin.abc.R.id.name8;

public class AddGridProductSizes extends AppCompatActivity implements View.OnClickListener {
    private static final String addGridData = Config.productSizesGridsCRUD;
   // private static final String addSpinData = Config.sizeSpinner;
    private static final int IMAGE_REQUEST_CODE = 3;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private ImageView imageView;
    private TextView Path,dispSize,dispProduct;
    private EditText name,brand,color;
    private Button btnadd;
    private Bitmap bitmap;
    private Uri filePath;
    Context context;
    private static int finalProId,finalProSizeId,finalWidth,finalLength,finalHeight;
    private static String finalProName,finalSelProductSize,finalSelProSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grid_product_sizes);
        Intent intent = getIntent();
        finalProId= intent.getExtras().getInt("PRODUCTID_KEY");
        finalProSizeId = intent.getExtras().getInt("PRODUCTSIZEID_KEY");
        finalProName = intent.getExtras().getString("PRODUCTNAME_KEY");
        finalSelProductSize = intent.getExtras().getString("FINALPROSELSIZE_KEY");
        finalLength = intent.getExtras().getInt("PRODUCTSIZELENGTH_KEY");
        finalWidth = intent.getExtras().getInt("PRODUCTSIZEWIDTH_KEY");
        finalHeight = intent.getExtras().getInt("PRODUCTSIZEHEIGHT_KEY");
        if(finalLength !=0 && finalWidth !=0 && finalHeight !=0){
            finalSelProSize =  finalWidth + "X" + finalHeight + "X" + finalLength;

        }else if(finalLength ==0 && finalWidth !=0 && finalHeight !=0){
            finalSelProSize =  finalWidth + "X" + finalHeight;

        }else if(finalLength !=0 && finalWidth ==0 && finalHeight !=0){
            finalSelProSize =  finalLength + "X" + finalHeight;

        }else if(finalLength !=0 && finalWidth !=0 && finalHeight ==0 ){
            finalSelProSize =  finalLength + "X" + finalHeight ;

        }else if(finalLength ==0 && finalWidth !=0 && finalHeight ==0 ){
            finalSelProSize = finalWidth + "" ;

        }else if(finalLength !=0 && finalWidth ==0 && finalHeight ==0 ){
            finalSelProSize = finalLength + "" ;

        }else if(finalLength ==0 && finalWidth ==0 && finalHeight !=0 ){
            finalSelProSize = finalHeight + "" ;

        }

        Toolbar actionbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != actionbar) {
            actionbar.setNavigationIcon(R.mipmap.backbutton);

            //  actionbar.setTitle(R.string.title_activity_settings);
            actionbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(AddGridProductSizes.this, Main2Activity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                     startActivity(in);
                  //  finish();
                }
            });
            imageView = (ImageView) findViewById(R.id.image8);
            name = (EditText) findViewById(name8);
            name.setInputType(InputType.TYPE_CLASS_TEXT);
            Path = (TextView) findViewById(R.id.brandpath);
            brand = (EditText) findViewById(R.id.brand8);
            brand .setInputType(InputType.TYPE_CLASS_TEXT);
            color = (EditText) findViewById(R.id.color6);
            color.setInputType(InputType.TYPE_CLASS_TEXT);
           dispSize = (TextView)findViewById(R.id.sizeTxt);
           dispProduct = (TextView) findViewById(R.id.productTxt);
           dispSize.setText(finalSelProSize);
           dispProduct.setText(finalProName);

            btnadd = (Button) findViewById(R.id.btnadd10);

            requestStoragePermission();

            imageView.setOnClickListener(this);
            btnadd.setOnClickListener(this);
        }
    }
    public void onBackPressed() {
        //finishAffinity();
        Intent in = new Intent(AddGridProductSizes.this, Main2Activity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(in);
    }


    @Override
    public void onClick(View v) {
        if (v == imageView) {
          /*  Intent intent = new Intent();
            intent.setType("image*//*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Complete action using"), IMAGE_REQUEST_CODE);*/
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
        } else if (v == btnadd) {
            checkData();
            //uploadMultipart();
        }
    }
    private void checkData() {
        if (name.length() < 1 || Path.length() < 1|| brand.length() < 1 || color.length() < 1 || bitmap==null) {
            Toast toast = Toast.makeText(AddGridProductSizes.this,
                    "Fill All",
                    Toast.LENGTH_SHORT);

            View toastView = toast.getView();
            toastView.setBackgroundResource(R.drawable.toast_drawable);
            toast.show();
            //Toast.makeText(AddGridProductSizes.this, "Fill All", Toast.LENGTH_SHORT).show();
        } else {
            uploadMultipart();
            Toast toast = Toast.makeText(this, "Successfully Completed", Toast.LENGTH_SHORT);

            View toastView = toast.getView();
            toastView.setBackgroundResource(R.drawable.toast_drawable);
            toast.show();
           // Toast.makeText(this, "Successfully Completed", Toast.LENGTH_SHORT).show();
            name.setText("");
            Path.setText("");
            brand.setText("");
            color.setText("");
            imageView.setImageResource(R.mipmap.browseimage);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Path.setText("Path: ".concat(getPath(filePath)));
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadMultipart() {

        String namec = name.getText().toString().trim();
        String brandc = brand.getText().toString().trim();
        String colorc = color.getText().toString().trim();
       /* String spinSelVal1 = sp1.getSelectedItem().toString();
        String spinSelVal2=sp2.getSelectedItem().toString();
*/

        //getting the actual path of the image
        String path = getPath(filePath);


        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, addGridData)
                    .addParameter("action","save")
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("caption", namec) //Adding text parameter to the request
                    .addParameter("brand", brandc)
                    .addParameter("color", colorc)
                    .addParameter("productsizeid", String.valueOf(finalProSizeId))
                    .addParameter("productid", String.valueOf(finalProId))
                    .addParameter("productname",finalProName)
                    .addParameter("selsize",finalSelProSize)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

}