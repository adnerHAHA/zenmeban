package com.fkl.story;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fkl.story.utils.PathUtils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

public class MyInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int TO_SELECT_PHOTO = 1;
    private TextView tvInfoUser, tvInfoNickName, tvInfoaSex, tvInfoEmail, tvInfoBirthday;
    private Button btnUpdataPwdGo,btnUp;
    private ImageView imaSexIcon, imgPhoto;
    private ImageButton btnBack;
    String sex = "男";
    int isManOrWoman = 0;//标记性别
    boolean isClick = false;
    SharedPreferences myinfo;
    String picpath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        MyApplication.getInstace().addActivity(this);
        findView();
        setListner();
        imaSexIcon.setImageResource(R.mipmap.icon_man);
        tvInfoaSex.setText("男");
        handler.sendEmptyMessage(100);
        initData();
    }

    private void initData() {
        myinfo = getSharedPreferences("loginData", MODE_PRIVATE);
        tvInfoUser.setText(myinfo.getString("username", ""));
        tvInfoNickName.setText(myinfo.getString("nickname", ""));
        tvInfoEmail.setText(myinfo.getString("useremail", ""));
        tvInfoBirthday.setText(myinfo.getString("birthday", ""));
        isManOrWoman = myinfo.getInt("usersex", 0);
        if (isManOrWoman == 0) {
            sex = "男";
            imaSexIcon.setImageResource(R.mipmap.icon_man);
            tvInfoaSex.setText(sex);

        } else if (isManOrWoman == 1) {
            sex = "女";
            imaSexIcon.setImageResource(R.mipmap.icon_woman);
            tvInfoaSex.setText(sex);
        } else {
            imaSexIcon.setImageResource(R.mipmap.icon_man);
            tvInfoaSex.setText(sex);
        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    Picasso.with(MyInfoActivity.this).load(myinfo.getString("portrait", ""))
                            .placeholder(R.mipmap.icon_portrait).into(imgPhoto, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Log.e("---", "onSuccess: ");
                        }

                        @Override
                        public void onError() {
                            Log.e("---", "onError: ");
                        }
                    });
                    break;
            }
        }
    };

    private void findView() {
        tvInfoUser = (TextView) findViewById(R.id.tvInfoUser);
        tvInfoNickName = (TextView) findViewById(R.id.tvInfoNickName);
        tvInfoaSex = (TextView) findViewById(R.id.tvInfoaSex);
        tvInfoEmail = (TextView) findViewById(R.id.tvInfoEmail);
        tvInfoBirthday = (TextView) findViewById(R.id.tvInfoBirthday);
        btnUpdataPwdGo = (Button) findViewById(R.id.btnUpdataPwdGo);
        imaSexIcon = (ImageView) findViewById(R.id.imaSexIcon);
        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        btnBack = (ImageButton) findViewById(R.id.toolbar_back);
        btnUp= (Button) findViewById(R.id.btnUp);
    }

    private void setListner() {
        tvInfoUser.setOnClickListener(this);
        tvInfoNickName.setOnClickListener(this);
        tvInfoaSex.setOnClickListener(this);
        tvInfoEmail.setOnClickListener(this);
        tvInfoBirthday.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnUpdataPwdGo.setOnClickListener(this);
        imgPhoto.setOnClickListener(this);
        btnUp.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvInfoNickName:
                startActivity(new Intent(MyInfoActivity.this, NickNameActivity.class));
                break;
            case R.id.tvInfoaSex:
                startActivity(new Intent(MyInfoActivity.this, UpdataSexActivity.class));
                isClick = true;

                break;
            case R.id.tvInfoEmail:
                startActivity(new Intent(MyInfoActivity.this, UpdataEmailActivity.class));
                break;
            case R.id.tvInfoBirthday:
                startActivity(new Intent(MyInfoActivity.this, UpdataBirthdayActivity.class));
                break;
            case R.id.btnUpdataPwdGo:
                startActivity(new Intent(MyInfoActivity.this, UpdataPWDActivity.class));
                break;
            case R.id.toolbar_back:
                MyInfoActivity.this.finish();
                break;
            case R.id.imgPhoto:
                Intent intent = new Intent(this, SelectPicActivity.class);
                startActivityForResult(intent, TO_SELECT_PHOTO);
                break;
            case R.id.btnUp:
                if (picpath!=null){
                    updataPortrait();
                }else {
                    Toast.makeText(MyInfoActivity.this, "你还没有选择头像", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    private void updataPortrait() {
        OkHttpUtils.post(PathUtils.changePortrait).params("uid", myinfo.getString("id", ""))
                .params("userpass", myinfo.getString("userpass","")).params("portrait", new File(picpath)).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(s.trim());
                    String result = jsonObject.optString("result");
                    String data = jsonObject.optString("data");
                    if (result.equals("1")) {
                        myinfo.edit().putString("portrait", PathUtils.portrait + data).apply();
                        Toast.makeText(MyInfoActivity.this, "修改头像成功", Toast.LENGTH_LONG).show();
                        Log.e("xi", "onSuccess: ");
                        handler.sendEmptyMessage(100);
                    }else {
                        Toast.makeText(MyInfoActivity.this, "修改头像失败", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO) {
            picpath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);

            Bitmap bm = BitmapFactory.decodeFile(picpath);
            imgPhoto.setImageBitmap(bm);

            Log.e("***", picpath);
        }
    }

}
