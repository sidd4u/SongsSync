package com.example.siddesh.songssync;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainScreenActivity extends AppCompatActivity {

    Button btnCheckConnection;
    // Progress Dialog
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // fetching button id
        btnCheckConnection = (Button)findViewById(R.id.Check_Connection);
    }

    // When Test connection button will be pressed below function will be executed
    public void TestConnection(View v) {

            new Makeconnection().execute();


    }


    /**
     * Background Async Task to test connection  making HTTP Request
     * */
    class Makeconnection extends AsyncTask<String,String,String>
    {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainScreenActivity.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {

            // Creating JSON Parser object
            JSONParser jParser = new JSONParser();

            // url to check connection
            //String url_all_products = "http://10.0.2.2:80/info.php";
            String url_all_products ="http://10.0.2.2:80/upload.php";
            //Building parameters
            HashMap<String, String> params =new HashMap<String,String>();
            params.put("Check","Siddesh");
            int permissionCheck = 0;
            final int  permissionRequest  = 1001;
            String []Permission ={
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE

            };


            if(Build.VERSION.SDK_INT >= 23) {

                permissionCheck = ContextCompat.checkSelfPermission(MainScreenActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainScreenActivity.this,Permission,
                            permissionRequest
                            );
                }
            }
            //hit the server
            JSONObject json = jParser.makeHttpRequest(url_all_products,
                    "POST", params);

            // check json success tag
            try {
                int success = json.getInt("success");

                if (success == 1) {
                    // successfully updated
                    //  Intent i = getIntent();
                    // send result code 100 to notify about product update
                    // setResult(100, i);
                    Log.d("CHECKING",json.getString("message"));
                    finish();
                } else {
                    // failed to update product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                                  }
            });

        }

        public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

            switch(permsRequestCode){

                case 1001:

                    boolean read = grantResults[0]==PackageManager.PERMISSION_GRANTED;

                    boolean cameraAccepted = grantResults[1]==PackageManager.PERMISSION_GRANTED;

                    break;

            }

        }

    }
}
