/package com.fkl.story;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fkl.story.entity.MyInfo;
import com.fkl.story.utils.PathUtils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
/*
*编辑我的故事 
*/

public class EditStoryActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton toolBarBack;
    private EditText edStory;
    private ImageView imgSend;
    List<File> files;
private  Button btnCarea;


    public static final int TO_SELECT_PHOTO = 3;

    private static final int UPLOAD_IN_PROCESS = 5;
    private String picPath = null;
    private ProgressDialog progressDialog;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_story);
        MyApplication.getInstace().addActivity(this);
        findView();
        setListener();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO)
        {
            picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);

            Bitmap bm = BitmapFactory.decodeFile(picPath);
            imageView.setImageBitmap(bm);

            files=new ArrayList<>();
            files.add(new File(picPath));
            Log.e("***", picPath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void setListener() {
        toolBarBack.setOnClickListener(this);
        TextView textView = new TextView(this);
        imgSend.setOnClickListener(this);
        btnCarea.setOnClickListener(this);

    }

    private void findView() {

        toolBarBack = (ImageButton) findViewById(R.id.toolbar_back);
        edStory= (EditText) findViewById(R.id.edStory);
        imgSend= (ImageView) findViewById(R.id.imgSend);
        btnCarea= (Button) findViewById(R.id.btnCarea);
        imageView= (ImageView) findViewById(R.id.imageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                EditStoryActivity.this.finish();
                break;
            case R.id.btnCarea:
                Intent intent = new Intent(this,SelectPicActivity.class);
                startActivityForResult(intent, TO_SELECT_PHOTO);
                break;
            case R.id.imgSend:
                sendStory();
                break;



        }

    }


    private void sendStory() {

//        Log.e("****", files.toString() );
        String story_info=edStory.getText().toString();
        OkHttpUtils.post(PathUtils.sendStory).params("uid", MyInfo.getInstace().getId()).params("story_info",story_info).addFileParams("photo[]",files)
                .params("userpass",MyInfo.getInstace().getUserpass()).params("lng",23).params("lat",25).params("city","成都")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s.trim());
                            String result = jsonObject.optString("result");
                            if (result.equals("1")){
                                showDialog(DIARLOG);
                            }else {
                                Toast.makeText(EditStoryActivity.this, "故事发表失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
    private static final int DIARLOG=1;
    private static final int DIARLOG1=2;
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIARLOG:
                return buildDialog(EditStoryActivity.this);
            case DIARLOG1:
                return buildDialog1(EditStoryActivity.this);
        }
        return null;
    }


    private Dialog buildDialog(EditStoryActivity editStoryActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(editStoryActivity);
        builder.setTitle("故事发表成功！！！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDialog(DIARLOG1);

            }
        });

        return builder.create();
    }
    private Dialog buildDialog1(EditStoryActivity editStoryActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(editStoryActivity);
        builder.setTitle("再来一发？！！！");

        builder.setPositiveButton("再来一发", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                edStory.setText("");

            }
        });
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditStoryActivity.this.finish();
            }
        });
        return builder.create();
    }


}