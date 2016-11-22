package com.fkl.story;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fkl.story.fragment.HotFragment;
import com.fkl.story.fragment.NewFragment;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnNew, btnHot, exit;
    private SlidingPaneLayout mSlidingPanelLayout;
    private ImageButton openDrawerlayout, btnEdtditGo;
    private ImageView imgMystory, imgHome, imgLook, imgMyInfo, imgSetting;
    private TextView tvHome, tvMystory, tvLook, tvMyInfo, tvSeting;
    private boolean openorclose = false;
    private List<Fragment> fragments = new ArrayList<>();
    private static final int DIARLOG = 1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        SharedPreferences loginData = getSharedPreferences("loginData", MODE_PRIVATE);
//        boolean isFirst = loginData.getBoolean("isFirst", true);
//        if (isFirst){
//
//            startActivity(new Intent(MainActivity.this,LoginActivity.class));
//        }
        setContentView(R.layout.activity_main);
        MyApplication.getInstace().addActivity(this);
        findView();
        NewFragment newFragment = new NewFragment();
        HotFragment hotFragment = new HotFragment();
        fragments.add(newFragment);
        fragments.add(hotFragment);
        btnNew.setBackgroundColor(Color.WHITE);
        btnHot.setBackgroundResource(R.mipmap.title_bg);
        showFragment(0);
        setListener();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setListener() {
        btnNew.setOnClickListener(this);
        btnHot.setOnClickListener(this);
        btnEdtditGo.setOnClickListener(this);
        openDrawerlayout.setOnClickListener(this);
        imgMyInfo.setOnClickListener(this);
        imgMystory.setOnClickListener(this);
        imgHome.setOnClickListener(this);
        imgLook.setOnClickListener(this);
        imgSetting.setOnClickListener(this);
        tvHome.setOnClickListener(this);
        tvMystory.setOnClickListener(this);
        tvLook.setOnClickListener(this);
        tvMyInfo.setOnClickListener(this);
        tvSeting.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    private void findView() {
        btnNew = (Button) findViewById(R.id.btnNew);
        btnHot = (Button) findViewById(R.id.btnHot);
        mSlidingPanelLayout = (SlidingPaneLayout) findViewById(R.id.mSlidingPanelLayout);
        openDrawerlayout = (ImageButton) findViewById(R.id.startDrawerlayout);
        btnEdtditGo = (ImageButton) findViewById(R.id.btnEdtditGo);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        imgMystory = (ImageView) findViewById(R.id.imgMystory);
        imgLook = (ImageView) findViewById(R.id.imgLook);
        imgSetting = (ImageView) findViewById(R.id.imgSetting);
        imgMyInfo = (ImageView) findViewById(R.id.imgMyInfo);
        tvHome = (TextView) findViewById(R.id.tvHome);
        tvLook = (TextView) findViewById(R.id.tvLook);
        tvMyInfo = (TextView) findViewById(R.id.tvMyInfo);
        tvMystory = (TextView) findViewById(R.id.tvMystory);
        tvSeting = (TextView) findViewById(R.id.tvSeting);
        exit = (Button) findViewById(R.id.exit);
        mSlidingPanelLayout.isOpen();
        mSlidingPanelLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelOpened(View panel) {
                openorclose = true;
            }

            @Override
            public void onPanelClosed(View panel) {
                openorclose = false;

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNew:
                btnNew.setBackgroundColor(Color.WHITE);
                btnHot.setBackgroundResource(R.mipmap.title_bg);
                showFragment(0);
                break;
            case R.id.btnHot:
                btnHot.setBackgroundColor(Color.WHITE);
                btnNew.setBackgroundResource(R.mipmap.title_bg);
                showFragment(1);
                break;
            case R.id.btnEdtditGo:
                startActivity(new Intent(MainActivity.this, EditStoryActivity.class));
                break;
            case R.id.startDrawerlayout:
                if (!openorclose) {
                    mSlidingPanelLayout.openPane();
                } else {
                    mSlidingPanelLayout.closePane();
                }
                openorclose = !openorclose;

                break;
            case (R.id.imgHome):
                mSlidingPanelLayout.closePane();
                openorclose = !openorclose;
                break;
             case (R.id.tvHome):
                mSlidingPanelLayout.closePane();
                 openorclose = !openorclose;
                break;

            case R.id.imgMystory:
                startActivity(new Intent(MainActivity.this, MyStoryActivity.class));
                ;
                break;
            case R.id.tvMystory:
                startActivity(new Intent(MainActivity.this, MyStoryActivity.class));
                ;
                break;
            case R.id.imgLook:
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                break;
            case R.id.tvLook:
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                break;
            case R.id.imgMyInfo:
                startActivity(new Intent(MainActivity.this, MyInfoActivity.class));
                break;
            case R.id.tvMyInfo:
                startActivity(new Intent(MainActivity.this, MyInfoActivity.class));
                break;
            case R.id.imgSetting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            case R.id.tvSeting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            case R.id.exit:

                showDialog(DIARLOG);
                break;

        }


    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIARLOG:
                return buildDialog(MainActivity.this);
        }
        return null;
    }

    private Dialog buildDialog(MainActivity mainActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("你确定退出我的故事？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyApplication.getInstace().exit();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    private int lastposition = -1;

    private void showFragment(int position) {
        Fragment f = fragments.get(position);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (lastposition != -1) {
            Fragment lastF = fragments.get(lastposition);
            ft.hide(lastF);
        }
        if (f.isAdded()) {
            ft.show(f);

        } else {
            ft.add(R.id.fr, f);
        }
        ft.commit();
        lastposition = position;
    }


}
