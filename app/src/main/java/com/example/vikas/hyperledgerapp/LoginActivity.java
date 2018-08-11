package com.example.vikas.hyperledgerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nispok.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText inputUsername,inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin=(Button)findViewById(R.id.btn_login);
        inputUsername=(EditText)findViewById(R.id.input_username);
        inputPassword=(EditText)findViewById(R.id.input_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                checkData();
            }
        });
    }

    private void checkData(){
        if(inputPassword.getText().toString().equals("")&& inputUsername.getText().toString().equals("")){
            Snackbar.with(getApplicationContext()).text("Username | Password can't be empty").show(this);
        }else if(inputUsername.getText().toString().toLowerCase().equals("admin")){
            Config.mUserType="Admin";
            gotoOptionsActivity();
        }else if(inputUsername.getText().toString().toLowerCase().equals("regulator")){
            Config.mUserType="Regulator";
            gotoOptionsActivity();
        }else if(inputUsername.getText().toString().toLowerCase().equals("user")){
            Config.mUserType="User";
            gotoOptionsActivity();
        }else{
            Snackbar.with(getApplicationContext()).text("Invalid User").show(this);
        }
    }

    private void gotoOptionsActivity(){
        Intent intent=new Intent(LoginActivity.this,OptionsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out,R.anim.slide_in);
        finish();
    }
}
