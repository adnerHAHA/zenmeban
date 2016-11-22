package com.fkl.story;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.fkl.story.entity.MyInfo;
import com.fkl.story.utils.PathUtils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class UpdataSexActivity extends AppCompatActivity implements View.OnClickListener{
    private Button man,woMan;
    private ImageView isMan,isWoman;
    int isManOrWoman=0;
    SharedPreferences myinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updata_sex);
        MyApplication.getInstace().addActivity(this);
        finView();
    }

    private void finView() {
        man= (Button) findViewById(R.id.man);
        woMan= (Button) findViewById(R.id.woMan);
        isMan= (ImageView) findViewById(R.id.isMan);
        isWoman= (ImageView) findViewById(R.id.isWoMan);
        man.setOnClickListener(this);
        woMan.setOnClickListener(this);
        isMan.setOnClickListener(this);
        isWoman.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if (v==man||v==isMan){
            isMan.setImageResource(R.mipmap.icon_gou);
            isWoman.setImageResource(R.mipmap.nopress);
            isManOrWoman=0;

        }else if(v==woMan||v==isWoman){
            isMan.setImageResource(R.mipmap.nopress);
            isWoman.setImageResource(R.mipmap.icon_gou);
            isManOrWoman=1;

        }
       upDataSex();
    }
    private void upDataSex() {
         myinfo=getSharedPreferences("loginData",MODE_PRIVATE);
        OkHttpUtils.post(PathUtils.changeSex).params("uid", myinfo.getString("id",""))
                .params("userpass",myinfo.getString("userpass",""))
                .params("usersex",isManOrWoman).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int result = jsonObject.optInt("result");
                    if (result==1) {
                        int data = jsonObject.optInt("data");
                        MyInfo.getInstace().setUsersex(data);
                        myinfo.edit().putInt("usersex",data).apply();
                        showDialog(DIARLOG);
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
                return buildDialog(UpdataSexActivity.this);

        }
        return null;
    }


    private Dialog buildDialog(UpdataSexActivity updataSexActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(updataSexActivity);
        builder.setTitle("修改成功！！！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                UpdataSexActivity.this.finish();


            }
        });

        return builder.create();
    }
}

