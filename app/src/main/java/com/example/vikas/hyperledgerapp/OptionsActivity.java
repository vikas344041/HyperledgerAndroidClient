package com.example.vikas.hyperledgerapp;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;

public class OptionsActivity extends AppCompatActivity{
    private TextView toolbarTitle;
    private ImageView imgLogout;
    private RecyclerView mRecyclerView;
    private OptionsRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context=this;
    private String[] optionsList;
    private Integer[] optionsIcon;
    private Class<?> mClss;
    private static final int ZXING_CAMERA_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        toolbarTitle=(TextView)findViewById(R.id.toolbar_title);
        imgLogout=(ImageView)findViewById(R.id.imgLogout);
        mRecyclerView = (RecyclerView) findViewById(R.id.options_recycler_view);

        toolbarTitle.setText(Config.mUserType);
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                logout();
            }
        });

        if(Config.mUserType.toLowerCase().equals("admin")){
            optionsList=getResources().getStringArray(R.array.adminOptions);
            optionsIcon=new Integer[5];
            optionsIcon[0]=R.mipmap.ic_get_all;
            optionsIcon[1]=R.mipmap.ic_get_one;
            optionsIcon[2]=R.mipmap.ic_qr;
            optionsIcon[3]=R.mipmap.ic_create;
            optionsIcon[4]=R.mipmap.ic_update;
        }else if(Config.mUserType.toLowerCase().equals("regulator")){
            optionsList=getResources().getStringArray(R.array.regulatorOptions);
            optionsIcon=new Integer[3];
            optionsIcon[0]=R.mipmap.ic_get_all;
            optionsIcon[1]=R.mipmap.ic_get_one;
            optionsIcon[2]=R.mipmap.ic_qr;
        }else{
            optionsList=getResources().getStringArray(R.array.regulatorOptions);
            optionsIcon=new Integer[3];
            optionsIcon[0]=R.mipmap.ic_get_all;
            optionsIcon[1]=R.mipmap.ic_get_one;
            optionsIcon[2]=R.mipmap.ic_qr;
        }
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(10000);
        itemAnimator.setRemoveDuration(10000);
        mRecyclerView.setItemAnimator(itemAnimator);
        mAdapter = new OptionsRecyclerAdapter(optionsList,optionsIcon,context);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnOptionsRecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClicked(int position, int id) {
                if(id==-1){
                    if(position==0){
                        Config.IfGetAllProducts=true;
                        Intent intent=new Intent(OptionsActivity.this,GetAllProductsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_out,R.anim.slide_in);
                    }else if(position==1){
                        Config.IfGetAllProducts=false;
                        showCustomDialog();
                    }else if(position==2){
                        Config.IfGetAllProducts=false;
                        launchActivity(ScannerActivity.class);
                        overridePendingTransition(R.anim.slide_out,R.anim.slide_in);
                    }else if(position==3){
                        Intent intent=new Intent(OptionsActivity.this,NewProductActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_out,R.anim.slide_in);
                    }else if(position==4){
                        Intent intent=new Intent(OptionsActivity.this,UpdateOwnerActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_out,R.anim.slide_in);
                    }
                }
            }
        });
    }

    public  void logout(){
        class LogoutAsync extends AsyncTask<Void, Void, Void> {
            ProgressDialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(OptionsActivity.this, "Please wait", "Logging out...");
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (loadingDialog!=null && loadingDialog.isShowing())
                {
                    loadingDialog.dismiss();
                    Intent intent = new Intent(OptionsActivity.this, LoginActivity.class);
                    finish();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.back_slide_out, R.anim.back_slide_in);
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                try{
                    SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.commit();
                    Thread.sleep(3000);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                return (Void)null;
            }

        }
        LogoutAsync loa = new LogoutAsync();
        loa.execute();

    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    private void showCustomDialog(){
        // Create custom dialog object
        final Dialog dialog = new Dialog(OptionsActivity.this);
        // remove dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include custom_dialog.xml file
        dialog.setContentView(R.layout.custom_popup);

        Button btnOk=(Button)dialog.findViewById(R.id.btnOk);
        final EditText txtID=(EditText)dialog.findViewById(R.id.txtID);
        dialog.show();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtID.getText().toString().equals("")){
                    showError();
                }else{
                    dialog.dismiss();
                    Config.ProductID=txtID.getText().toString().trim();
                    Intent intent=new Intent(OptionsActivity.this,GetAllProductsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_out,R.anim.slide_in);
                }
            }
        });
    }

    private void showError(){
        Snackbar.with(getApplicationContext()).text("Product ID cannot be empty").show(this);
    }

    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent intent = new Intent(this, ScannerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_out, R.anim.slide_out);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }
}
