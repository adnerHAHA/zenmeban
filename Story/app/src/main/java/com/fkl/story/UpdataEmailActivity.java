package com.fkl.story;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class UpdataEmailActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton back,btnSend;
    private EditText edUpDataEmail;
    SharedPreferences myinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstace().addActivity(this);
        setContentView(R.layout.activity_updata_email);
        back= (ImageButton) findViewById(R.id.back);
        btnSend= (ImageButton) findViewById(R.id.btnSend);
        edUpDataEmail= (EditText) findViewById(R.id.edUpDataEmail);
        myinfo=getSharedPreferences("loginData",MODE_PRIVATE);
    edUpDataEmail.setText(myinfo.getString("useremail",""));
        back.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==back){
            UpdataEmailActivity.this.finish();
        }else if(v==btnSend){
        updataEmail();

        }
    }

    private void updataEmail() {
        String email=edUpDataEmail.getText().toString();
        OkHttpUtils.post(PathUtils.changeEmail).params("uid", myinfo.getString("id",""))
                .params("userpass",myinfo.getString("userpass",""))
                .params("useremail",email).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int result = jsonObject.optInt("result");
                    if (result==1) {
                        String data = jsonObject.optString("data");
                        MyInfo.getInstace().setUseremail(data);

                        myinfo.edit().putString("useremail",data).apply();
                        showDialog(DIARLOG);
                    }else {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private static final int DIARLOG = 1;
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIARLOG:
                return buildDialog(UpdataEmailActivity.this);

        }
        return null;
    }


    private Dialog buildDialog(UpdataEmailActivity updataEmailActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(updataEmailActivity);
        builder.setTitle("修改成功！！！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                UpdataEmailActivity.this.finish();


            }
        });

        return builder.create();
    }
}
