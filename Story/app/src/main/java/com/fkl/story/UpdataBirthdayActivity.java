package com.fkl.story;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
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

public class UpdataBirthdayActivity extends AppCompatActivity implements View.OnClickListener{
private EditText edUpDataBirthday;
    private ImageButton back,btnSend;
    SharedPreferences myinfo;
    private  int mYear;
    private int mMonth;
    private int mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstace().addActivity(this);
        setContentView(R.layout.activity_updata_birthday);
        edUpDataBirthday= (EditText) findViewById(R.id.edUpDataBirthday);
        back= (ImageButton) findViewById(R.id.back);
        btnSend= (ImageButton) findViewById(R.id.btnSend);
        back.setOnClickListener(this);
        btnSend.setOnClickListener(this);
       edUpDataBirthday.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                   new DatePickerDialog(UpdataBirthdayActivity.this, new DatePickerDialog.OnDateSetListener() {
                       @Override
                       public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                           edUpDataBirthday.setText(String.format("%d-%d-%d", year, month + 1, dayOfMonth));
                       }
                   }, 1980, 1, 1).show();
           }

       });
    }



    @Override
    public void onClick(View v) {
        if (v==back){
            UpdataBirthdayActivity.this.finish();
        }else if (v==btnSend){
            upDataBirthday();

        }
    }

    private void upDataBirthday() {
        myinfo=getSharedPreferences("loginData",MODE_PRIVATE);
        String birthday=edUpDataBirthday.getText().toString();
        OkHttpUtils.post(PathUtils.changeBirthday).params("uid", myinfo.getString("id",""))
                .params("userpass",myinfo.getString("userpass",""))
                .params("birthday",birthday).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int result = jsonObject.optInt("result");
                    if (result==1) {
                        String data = jsonObject.optString("data");
                        MyInfo.getInstace().setBirthday(data);
                        Log.e("---",MyInfo.getInstace().getBirthday());
                        myinfo.edit().putString("birthday",data).apply();
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
                return buildDialog(UpdataBirthdayActivity.this);

        }
        return null;
    }


    private Dialog buildDialog(UpdataBirthdayActivity updataBirthdayActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(updataBirthdayActivity);
        builder.setTitle("修改成功！！！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
              //  startActivity(new Intent(UpdataBirthdayActivity.this,MyInfoActivity.class));
                UpdataBirthdayActivity.this.finish();
            }
        });
        return builder.create();
    }
}
