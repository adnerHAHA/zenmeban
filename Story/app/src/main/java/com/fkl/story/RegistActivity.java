package com.fkl.story;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.fkl.story.utils.PathUtils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnRegist;
    private EditText edRegUser, edRegNickName, edRegPwd;
    private ImageButton toolBarBack;
    private ImageView imgPortrait;
    public static final int TO_SELECT_PHOTO = 1;
    private String picPath = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        MyApplication.getInstace().addActivity(this);
        findView();
        setClicListener();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO)
        {
            picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);

            Bitmap bm = BitmapFactory.decodeFile(picPath);
            imgPortrait.setImageBitmap(bm);

            Log.e("***", picPath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void setClicListener() {
        btnRegist.setOnClickListener(this);
        toolBarBack.setOnClickListener(this);
        imgPortrait.setOnClickListener(this);
    }

    private void findView() {
        btnRegist = (Button) findViewById(R.id.btnReg);
        toolBarBack = (ImageButton) findViewById(R.id.toolbar_back);
        edRegUser = (EditText) findViewById(R.id.edRegUser);
        edRegNickName = (EditText) findViewById(R.id.edRegNickName);
        edRegPwd = (EditText) findViewById(R.id.edRegPwd);
        imgPortrait= (ImageView) findViewById(R.id.imgPortrait);
    }

    private static final int DIARLOG = 1;
    private static final int DIARLOG2 = 2;

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIARLOG:
                return buildDialog(RegistActivity.this);
            case DIARLOG2:
                return buildDialog2(RegistActivity.this);

        }
        return null;
    }

    private Dialog buildDialog(RegistActivity updataEmailActivity) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(updataEmailActivity);
        builder.setTitle("注册成功！！！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(RegistActivity.this, LoginActivity.class));
            }
        });

        return builder.create();
    }

    private Dialog buildDialog2(RegistActivity updataEmailActivity) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(updataEmailActivity);
        builder.setTitle("输入信息有误或用户已存在！！！");
        builder.setPositiveButton("重新注册", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                edRegUser.setText("");
            }
        });

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReg:
                register();

                break;
            case R.id.toolbar_back:
                RegistActivity.this.finish();
                break;
            case R.id.imgPortrait:
                Intent intent = new Intent(this,SelectPicActivity.class);
                startActivityForResult(intent, TO_SELECT_PHOTO);
                break;
        }

    }

    public static boolean isLetterDigigitOrChinise(String str) {
        String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";
        return str.matches(regex);
    }

    private void register() {
        String nikename = edRegNickName.getText().toString();
        String username = edRegUser.getText().toString();
        String password = edRegPwd.getText().toString();
        if (!(isLetterDigigitOrChinise(username) && isLetterDigigitOrChinise(password))) {
            showDialog(DIARLOG2);
        }
       // String portraiturl = Environment.getExternalStorageDirectory() + "/cc.png";
        OkHttpUtils.post(PathUtils.register).params("nikename", nikename).params("username", username)
                .params("password", password)
                .params("portrait", new File(picPath)).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                String result = null;
                try {
                    JSONObject jsonObject = new JSONObject(s.trim());
                    result = jsonObject.getString("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result.equals("1")) {
                    showDialog(DIARLOG);
                } else {
                    showDialog(DIARLOG2);
                }
            }

        });
    }
}
