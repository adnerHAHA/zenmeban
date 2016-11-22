package com.fkl.story;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.fkl.story.utils.PathUtils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class UpdataPWDActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton back;
    private Button btnUpdataPwd;
    private EditText edPerPwd,edNewPwd,edRePwd;
    private static final int DIARLOG = 1;
    private static final int DIARLOG1 = 2;
    SharedPreferences myinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstace().addActivity(this);
        setContentView(R.layout.activity_updata_pwd);
        back= (ImageButton) findViewById(R.id.toolbar_back);
        btnUpdataPwd= (Button) findViewById(R.id.btnUpdataPwd);
        edPerPwd= (EditText) findViewById(R.id.edPerPwd);
        edNewPwd= (EditText) findViewById(R.id.edNewPwd);
        edRePwd= (EditText) findViewById(R.id.edRePwd);
        back.setOnClickListener(this);
        btnUpdataPwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==back){
            UpdataPWDActivity.this.finish();
        }else if(v==btnUpdataPwd){
          upDataPassWord();
        }

    }

    private void upDataPassWord() {
        String oldPWD=edPerPwd.getText().toString();
        String newPWD=edNewPwd.getText().toString();
        String rePWD=edRePwd.getText().toString();
        if (newPWD.equals(rePWD)) {
             myinfo=getSharedPreferences("loginData",MODE_PRIVATE);
            OkHttpUtils.post(PathUtils.changePassword).params("uid", myinfo.getString("id",""))

                    .params("oldpass",oldPWD).params("newpass",newPWD).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        int result = jsonObject.optInt("result");
                        if (result==1){
                            String data = jsonObject.optString("data");
                            Log.e("pass", data);
                            myinfo.edit().putString("usersex",data).apply();
                            showDialog(DIARLOG);
                        }else{
                            showDialog(DIARLOG1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIARLOG:
                return buildDialog(UpdataPWDActivity.this);
            case DIARLOG1:
                return buildDialog1(UpdataPWDActivity.this);
        }
        return null;
    }


    private Dialog buildDialog(UpdataPWDActivity updataPWDActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(updataPWDActivity);
        builder.setTitle("密码已修改，请重新登陆！！！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

               startActivity(new Intent(UpdataPWDActivity.this,LoginActivity.class));
            }
        });

        return builder.create();
    }
    private Dialog buildDialog1(UpdataPWDActivity updataPWDActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(updataPWDActivity);
        builder.setTitle("密码输入不一致！！！");

        builder.setPositiveButton("重修修改", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                edPerPwd.setText("");
                edRePwd.setText("");
                edNewPwd.setText("");

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

}
