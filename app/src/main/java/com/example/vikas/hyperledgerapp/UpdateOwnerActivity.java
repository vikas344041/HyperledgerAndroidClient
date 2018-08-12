package com.example.vikas.hyperledgerapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;

public class UpdateOwnerActivity extends AppCompatActivity {
    private TextView toolbarTitle,txtFeedback;
    private EditText txtOwner,txtID;
    private Button btnUpdate;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_owner);

        txtFeedback=(TextView)findViewById(R.id.txtFeedback);
        toolbarTitle=(TextView)findViewById(R.id.toolbar_title);
        txtID=(EditText)findViewById(R.id.txtID);
        txtOwner=(EditText)findViewById(R.id.txtOwner);
        btnUpdate=(Button)findViewById(R.id.btnOk);

        toolbarTitle.setText(Config.mUserType);
        txtFeedback.setVisibility(View.GONE);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }

        });
    }

    private void validateData(){
        if(txtID.getText().toString().equals("") || txtOwner.getText().toString().equals("")){
            Snackbar.with(getApplicationContext()).text("All fields are mandatory").show(this);
        }else{
            updateProduct();
        }
    }

    private void updateProduct(){
        String uri=txtID.getText().toString().trim()+"-"+txtOwner.getText().toString().trim();
        final String URL_CREATE_PRODUCT="http://"+Config.ServerIP+":"+Config.Port+"/change_holder/"+uri;
        class GetJSON extends AsyncTask<Void,Void,String> {
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                progressDialog=new ProgressDialog(UpdateOwnerActivity.this);
                progressDialog.setMessage("Submitting Transaction");
                progressDialog.show();
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s;
                try{
                    s = rh.sendGetRequest(URL_CREATE_PRODUCT);
                }catch (Exception e){
                    s="";
                    progressDialog.dismiss();
                }

                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                progressDialog.dismiss();
                setResult();
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void setResult(){
        if(JSON_STRING.equals("")){
            txtFeedback.setVisibility(View.VISIBLE);
            txtFeedback.setText("Failure");
        }else{
            txtFeedback.setVisibility(View.VISIBLE);
            txtFeedback.setText("Success! TxID : "+JSON_STRING);
        }
    }

}
