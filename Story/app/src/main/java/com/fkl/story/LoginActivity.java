package com.fkl.story;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fkl.story.entity.MyInfo;
import com.fkl.story.utils.PathUtils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton toolbarBack;
    private EditText edUser, edPwd;
    private TextView tvRegistGo;
    private Button btnLogin;
    public static List<MyInfo> myInfos = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MyApplication.getInstace().addActivity(this);
        findview();
        setClicListenr();
    }

    private void setClicListenr() {
        toolbarBack.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvRegistGo.setOnClickListener(this);
    }

    private void findview() {
        toolbarBack = (ImageButton) findViewById(R.id.toolbar_back);
        tvRegistGo = (TextView) findViewById(R.id.tvRegistGo);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edUser = (EditText) findViewById(R.id.edUser);
        edPwd = (EditText) findViewById(R.id.edPwd);
        if (null!=getSharedPreferences("loginData",MODE_PRIVATE)){
            SharedPreferences loginData = getSharedPreferences("loginData", MODE_PRIVATE);
            edUser.setText(loginData.getString("username",""));
            edPwd.setText(loginData.getString("password",""));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                LoginActivity.this.finish();
                break;
            case R.id.btnLogin:
                login();


                break;
            case R.id.tvRegistGo:
                Intent intent1 = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent1);
                break;

        }

    }

    private void login() {


        String username = edUser.getText().toString();
        String password = edPwd.getText().toString();
        OkHttpUtils.post(PathUtils.login).params("username", username).params("password", password).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                String result = null;
                String nickname = null;
                try {
                    JSONObject jsonObject = new JSONObject(s.trim());
                    result = jsonObject.getString("result");
                    JSONObject data = jsonObject.getJSONObject("data");
                    String id = data.optString("id");
                    String username1 = data.optString("username");
                    Log.e("username1", username1);
                    String userpass = data.optString("userpass");
                    Log.e("username1", userpass);
                    int usersex = data.optInt("usersex");
                    Log.e("username1", usersex + "");
                    String useremail = data.optString("useremail");
                    Log.e("useremail", useremail);
                    nickname = data.optString("nickname");
                    Log.e("nickname", nickname);
                    String birthday = data.optString("birthday");
                    Log.e("birthday", birthday);
                    String portrait = data.optString("portrait");
                    Log.e("portrait", portrait);
                    String signature = data.optString("signature");
                    Log.e("signature", signature);
                    MyInfo myinfo = MyInfo.getInstace();
                    myinfo.setId(id);
                    myinfo.setUsername(username1);
                    myinfo.setUserpass(userpass);
                    myinfo.setUsersex(usersex);
                    myinfo.setUseremail(useremail);
                    myinfo.setNickname(nickname);
                    myinfo.setBirthday(birthday);
                    myinfo.setPortrait(PathUtils.portrait + portrait);
                    myinfo.setSignature(signature);
                    myInfos.add(myinfo);
                    myinfo.addUser(myinfo);
                    Log.e("----", myinfo.toString());
                    //   MyNickName.getInstace().addUser(MyNickName.getInstace());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result.equals("1")) {
                    saveUser();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    showDialog(DIARLOG);
                }
            }
        });
    }
     public void saveUser(){
        SharedPreferences.Editor edit = getSharedPreferences("loginData", MODE_PRIVATE).edit();
        edit.putString("username",MyInfo.getInstace().getUsername());
        edit.putString("password",edPwd.getText().toString());
         edit.putString("id",MyInfo.getInstace().getId());
         edit.putString("birthday",MyInfo.getInstace().getBirthday());
         edit.putString("portrait",MyInfo.getInstace().getPortrait());
         edit.putString("nickname",MyInfo.getInstace().getNickname());
         edit.putInt("usersex",MyInfo.getInstace().getUsersex());
         edit.putString("useremail",MyInfo.getInstace().getUseremail());
         edit.putString("userpass",MyInfo.getInstace().getUserpass());
         edit.putBoolean("isFirst",false);
         edit.apply();
    }

    private static final int DIARLOG = 1;


    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIARLOG:
                return buildDialog(LoginActivity.this);


        }
        return null;
    }

    private Dialog buildDialog(LoginActivity updataEmailActivity) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(updataEmailActivity);
        builder.setTitle("用户名或密码错误，请确认！！！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }
}
