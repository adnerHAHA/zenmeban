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
import android.widget.EditText;
import android.widget.ImageButton;

import com.fkl.story.entity.MyInfo;
import com.fkl.story.utils.PathUtils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class NickNameActivity extends AppCompatActivity implements View.OnClickListener{
private EditText edUpDataNickName;
private ImageButton btnEdtditGo,back;
    SharedPreferences myinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);
        MyApplication.getInstace().addActivity(this);
        edUpDataNickName= (EditText) findViewById(R.id.edUpDataNickName);
        btnEdtditGo= (ImageButton) findViewById(R.id.btnEdtditGo);
        back= (ImageButton) findViewById(R.id.back);
        myinfo=getSharedPreferences("loginData",MODE_PRIVATE);
        edUpDataNickName.setText(myinfo.getString("nickname",""));
        btnEdtditGo.setOnClickListener(this);
        back.setOnClickListener(this);


    }

    private void upDataNickName() {
        String nickname=edUpDataNickName.getText().toString();

        OkHttpUtils.post(PathUtils.changeNickName).params("uid",myinfo.getString("id",""))
                .params("userpass",myinfo.getString("userpass",""))
                .params("nickname",nickname).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int result = jsonObject.optInt("result");
                    if (result==1) {
                        String data = jsonObject.optString("data");
                        MyInfo.getInstace().setNickname(data);
                        myinfo.edit().putString("nickname",data).apply();
                        Log.e("birthday",data);
                        showDialog(DIARLOG);
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(NickNameActivity.this);
                        builder.setTitle("修改失败");
                        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(NickNameActivity.this, MyInfoActivity.class));
                            }
                        });
                        builder.setNegativeButton("重新修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.create();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    @Override
    public void onClick(View v) {
        if(v==back){
            NickNameActivity.this.finish();

        }else if (v==btnEdtditGo){
           upDataNickName();
        }
    }
    private static final int DIARLOG = 1;
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIARLOG:
                return buildDialog(NickNameActivity.this);

        }
        return null;
    }


    private Dialog buildDialog(NickNameActivity nickNameActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(nickNameActivity);
        builder.setTitle("修改成功！！！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                NickNameActivity.this.finish();
            }
        });

        return builder.create();
    }
}
