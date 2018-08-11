package com.example.vikas.hyperledgerapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OptionsActivity extends AppCompatActivity{
    private TextView toolbarTitle;
    private ImageView imgLogout;
    private RecyclerView mRecyclerView;
    private OptionsRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context=this;
    private String[] optionsList;
    private Integer[] optionsIcon;

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
                        Intent intent=new Intent(OptionsActivity.this,GetAllProductsActivity.class);
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
        moveTaskToBack(true);
    }
}
