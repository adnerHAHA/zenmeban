package com.fkl.story;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fkl.story.entity.MyInfo;
import com.fkl.story.entity.Story;
import com.fkl.story.entity.User;
import com.fkl.story.utils.PathUtils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MyStoryActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener{
    private ListView lvMystory;
    private TextView tvMassage, tvYear, tvNickName,tvSignature;
    private ImageView imaMine, imgSex,imgchangeSignaGo;
    private ListViewAdapter adapter;
    private List<Story> myStory=new ArrayList<>();
    private List<String> picslist=new ArrayList<>();
    private List<User> users=new ArrayList<>();
    private static  final int DIALOG=1;
  private String signature=null;
    private EditText edSingna;
   // String data=null;
    SharedPreferences myinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_story);
        MyApplication.getInstace().addActivity(this);
        findView();
        myinfo=getSharedPreferences("loginData",MODE_PRIVATE);
        imgchangeSignaGo.setOnClickListener(this);
        adapter = new ListViewAdapter(MyStoryActivity.this);
        lvMystory.setAdapter(adapter);
        showMineInfo();
        getMyStory();
        lvMystory.setOnItemClickListener(this);


    }

    /**
     * 修改签名
     */
    private void changeSignature() {

        String id = myinfo.getString("id","");
        String userpass =myinfo.getString("userpass","");

        OkHttpUtils.post(PathUtils.changeSignature).params("uid",id).params("userpass",userpass)
                .params("signature",signature).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(s.trim());
                    int result = jsonObject.optInt("result");
                signature = jsonObject.optString("data");
                    if (result==1){
                        myinfo.edit().putString("signature",signature).apply();
                        hd.sendEmptyMessage(300);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    Handler hd=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 100:
                        adapter.notifyDataSetChanged();
                        break;
                    case 200:
                        String portrait = MyInfo.getInstace().getPortrait();
                        Picasso.with(MyStoryActivity.this).load(myinfo.getString("portrait","")).placeholder(R.mipmap.icon_care).into(imaMine);
                        break;
                    case 300:
                        tvSignature.setText(signature);
                        break;
                }
            }
        };
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG:
                return buildDialog(MyStoryActivity.this);
        }
        return null;
    }
    private Dialog buildDialog(MyStoryActivity myStoryActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(myStoryActivity);
        View inflate = LayoutInflater.from(myStoryActivity).inflate(R.layout.editsingnatuer, null);
        edSingna = (EditText) inflate.findViewById(R.id.edSingna);
        builder.setView(inflate);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                signature=edSingna.getText().toString();

                changeSignature();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    /**
     * 获取我的故事
     */
    private void getMyStory() {
        OkHttpUtils.post(PathUtils.myStorys).params("uid",myinfo.getString("id","")).params("page",1).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(s.trim());
                    String result = jsonObject.optString("result");
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i=0;i<data.length();i++){
                        JSONObject obj = data.getJSONObject(i);
                        String id = obj.optString("id");
                        Long story_time = obj.optLong("story_time");
                        String story_info = obj.optString("story_info");
                        JSONArray pics = obj.optJSONArray("pics");
                        if (null!=pics){
                            for (int j=0;j<pics.length();j++) {
                                String s1 = pics.optString(j);
                                picslist.add(s1);
                            }
                        }
                        String uid = obj.optString("uid");
                        String lng = obj.optString("lng");
                        String lat = obj.optString("lat");
                        String city = obj.optString("city");
                        int readcount = obj.optInt("readcount");
                        int comment = obj.optInt("comment");
                        Story story = new Story(id, story_time, story_info, picslist, uid, lng, lat, city, readcount, comment, users);
                        myStory .add(story);
                        Log.e("my", myStory.toString());
                    }
                    if (result.equals("1")){
                           hd.sendEmptyMessage(100);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 展示用户信息
     */
    private void showMineInfo() {

        int usersex = myinfo.getInt("usersex",0);
        String nickname =myinfo.getString("nickname","");
        String birthday = myinfo.getString("birthday","");
        hd.sendEmptyMessage(200);
        if (usersex == 0) {
            imgSex.setImageResource(R.mipmap.icon_man);
        } else if (usersex == 1) {
            imgSex.setImageResource(R.mipmap.icon_woman);
        }
        tvNickName.setText(nickname);
        tvSignature.setText(myinfo.getString("signature","暂无签名"));
    }
    private void findView() {
        lvMystory = (ListView) findViewById(R.id.lvMystory);
        View view = View.inflate(this, R.layout.head_listview, null);
        tvYear = (TextView) view.findViewById(R.id.tvYear);
        tvNickName = (TextView)view. findViewById(R.id.tvNickName);
        tvSignature= (TextView) view.findViewById(R.id.tvSignature);
        imgchangeSignaGo= (ImageView) view.findViewById(R.id.imgchangeSignaGo);
        imaMine = (ImageView) view.findViewById(R.id.imaMine);
        imgSex= (ImageView) view.findViewById(R.id.imgSex);
        lvMystory.addHeaderView(view);
    }

    /**
     * 事件监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v==imgchangeSignaGo){
            showDialog(DIALOG);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MyStoryActivity.this, StoryActivity.class);
        intent.putExtra("story",myStory.get(position));

        startActivity(intent);

    }

    /**
     * listView控件
     */

    private class ListViewAdapter extends BaseAdapter {
        public Context ctx;
        LayoutInflater inflater;
        public ViewHoldle vh;
        public ListViewAdapter(Context ctx) {
            this.ctx = ctx;
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return myStory==null?0:myStory.size();
        }

        @Override
        public Object getItem(int position) {
            return myStory.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                vh = new ViewHoldle();
                convertView = inflater.inflate(R.layout.mystory_layout, null);
                vh.tvLoveNum = (TextView) convertView.findViewById(R.id.tvLoveNum);
                vh.tvStory = (TextView) convertView.findViewById(R.id.tvStory);
                vh.tvTalkNum = (TextView) convertView.findViewById(R.id.tvTalkNum);
                vh.tvTime = (TextView) convertView.findViewById(R.id.tvData);
                vh.btnLove = (ImageButton) convertView.findViewById(R.id.btnLove);
                vh.btnShare = (ImageButton) convertView.findViewById(R.id.btnShare);
                vh.btnTalk = (ImageButton) convertView.findViewById(R.id.btnTalk);
                vh.imgStory = (ImageView) convertView.findViewById(R.id.imgStoryPics);
                vh.imgStory.setScaleType(ImageView.ScaleType.FIT_XY);

                convertView.setTag(vh);

            } else {
                vh = (ViewHoldle) convertView.getTag();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日   HH:mm");
            Long story_time = myStory.get(position).getStory_time();
            String s = sdf.format(new Date(story_time * 1000L));
            vh.tvTime.setText(s);
            vh.tvStory.setText(myStory.get(position).getStory_info());
            Log.e("mm",myStory.get(position).getStory_info() );
            List<String> pics = myStory.get(position).getPics();
            if (0!=pics.size()){
                Picasso.with(ctx).load(PathUtils.imgpath+pics.get(0)).placeholder(R.mipmap.mm_11).into(vh.imgStory);
                Log.e("mmm", PathUtils.imgpath+pics.get(0));
            }
            vh.tvLoveNum.setText(myStory.get(position).getReadcount()+"");
            vh.tvTalkNum.setText(myStory.get(position).getComment()+"");
            return convertView;
        }
    }

    private class ViewHoldle {
        public ImageButton btnTalk, btnLove, btnShare;
        public ImageView imgStory;
        public TextView tvTime, tvStory, tvTalkNum, tvLoveNum;

    }
}
