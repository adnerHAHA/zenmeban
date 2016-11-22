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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fkl.story.entity.Comments;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class StoryActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView imgSendMassge, imgPhoto, imgSex;
    private EditText edComm;
    private TextView tvReadCount, tvTalkNum, tvNickName, tvTime, tvAddress, tvTalkCount;
    private ListView lvStory;
    private MyAdapter adpter;
    private TextView tvStory;
    private Gallery gridView;
    private User user;
    private ImageButton btnShare,btnback;
    List<Story> storys = new ArrayList<>();
    Story story = null;
    GirdViewAdapter girdViewAdapter;
    private List<Comments> commentsList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        MyApplication.getInstace().addActivity(this);
        findView();
        inidata();
        readStory();
        readConmun();
    }

    private void readConmun() {
        commentsList = new ArrayList<>();
        final List<User> users = new ArrayList<>();
        OkHttpUtils.post(PathUtils.getComments).params("sid", story.getId()).params("page", 1).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(s.trim());
                    int result = jsonObject.optInt("result");
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.getJSONObject(i);
                        String id = obj.optString("id");
                        String s1 = obj.optString("comments");
                        long time = obj.optLong("time");
                        String sid1 = obj.optString("sid");
                        String uid = obj.optString("uid");
                        String cid = obj.optString("cid");
                        JSONObject user = obj.getJSONObject("user");
                        String nickname = user.optString("nickname");
                        String portrait = user.optString("portrait");
                        User mUser = new User();
                        mUser.setNickname(nickname);
                        mUser.setPortrait(portrait);
                        users.add(mUser);
                        Comments comments = new Comments(id, users, uid, cid, sid1, time, s1);
                        commentsList.add(comments);
                        if (result == 1) {
                            hd.sendEmptyMessage(200);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void inidata() {
        Intent intent = this.getIntent();
        story = new Story();
        story = (Story) intent.getSerializableExtra("story");
        user = new User();
        user = (User) intent.getSerializableExtra("user");
        adpter = new MyAdapter(this);
        girdViewAdapter = new GirdViewAdapter(StoryActivity.this);
        Log.e("//", story.getId());
        gridView.setAdapter(girdViewAdapter);
        lvStory.setAdapter(adpter);
        btnShare.setOnClickListener(this);
        btnback.setOnClickListener(this);
        imgSendMassge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgSendMassge();

            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        commentsList.clear();

    }

    Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    tvStory.setText(story.getStory_info());
                    tvReadCount.setText(story.getReadcount() + "");
                    tvTalkNum.setText(story.getComment() + "");
                    tvNickName.setText(user.getNickname() + "");
                    tvAddress.setText(story.getCity());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Long story_time = story.getStory_time();
                    String s = sdf.format(new Date(story_time * 1000L));
                    tvTime.setText(s + "");
                    String usersex = user.getUsersex();
                    Log.e("sex", usersex );
                    if (usersex.equals("0")) {
                        imgSex.setImageResource(R.mipmap.icon_man);
                    } else if (usersex.equals("1")) {
                        imgSex.setImageResource(R.mipmap.icon_woman);
                    }
                    tvTalkCount.setText("一共包含" + story.getComment() + "条评论");
                    Picasso.with(StoryActivity.this).load(PathUtils.portrait + user.getPortrait().toString()).into(imgPhoto);
                    Log.e("TAG", PathUtils.portrait + user.getPortrait());
                    girdViewAdapter.notifyDataSetChanged();

                    break;
                case 200:
                    adpter.notifyDataSetChanged();
                    edComm.setText("");
                    break;


            }

        }
    };

    private void readStory() {
        OkHttpUtils.post(PathUtils.readStorys).params("sid", story.getId()).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String result = jsonObject.getString("result");
                    if (result.equals("1")) {
                        //  hd.sendEmptyMessage(100);
                        hd.sendEmptyMessage(100);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    private void findView() {
        lvStory = (ListView) findViewById(R.id.lvStory);
        View view = View.inflate(this, R.layout.story_look, null);
        tvStory = (TextView) view.findViewById(R.id.tvStory);
        gridView = (Gallery) view.findViewById(R.id.gvImg);
        imgPhoto = (ImageView) view.findViewById(R.id.imgPhoto);
        imgSex = (ImageView) view.findViewById(R.id.imgSex);
        tvNickName = (TextView) view.findViewById(R.id.tvNickName);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        tvTalkNum = (TextView) view.findViewById(R.id.tvTalkNum);
        tvReadCount = (TextView) view.findViewById(R.id.tvReadCount);
        tvTime = (TextView) view.findViewById(R.id.tvda);
        tvTalkCount = (TextView) view.findViewById(R.id.tvTalkCount);
        edComm = (EditText) findViewById(R.id.edComm);
        imgSendMassge = (ImageView) findViewById(R.id.imgSendMassge);
        lvStory.addHeaderView(view);
        btnShare= (ImageButton) findViewById(R.id.btnShare);
        btnback= (ImageButton) findViewById(R.id.btnback);
    }

    private void imgSendMassge() {
        SharedPreferences myinfo = getSharedPreferences("loginData", MODE_PRIVATE);
        String comments = edComm.getText().toString();
        OkHttpUtils.post(PathUtils.sendComment).params("uid",myinfo.getString("id",""))
                .params("userpass",myinfo.getString("userpass",""))
                .params("sid", story.getId()).params("comments", comments)
                .params("cid", 55555555)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s.trim());
                            int result = jsonObject.optInt("result");
                            if (result == 1) {
                                showDialog(DIARLOG);
                                Log.e("---", "评论成功");
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
                return buildDialog(StoryActivity.this);

        }
        return null;
    }


    private Dialog buildDialog(StoryActivity storyActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(storyActivity);
        builder.setTitle("评论成功！！！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                hd.sendEmptyMessage(200);
                //  edComm.setText("");
            }
        });

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (v==btnback){
            StoryActivity.this.finish();

        }else if (v==btnShare){
            sendStory();
        }

    }

    private class MyAdapter extends BaseAdapter {
        private Context ctx;
        private LayoutInflater inflater;

        public MyAdapter(Context ctx) {
            this.ctx = ctx;
            inflater = (LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return commentsList == null ? 0 : commentsList.size();
        }

        @Override
        public Object getItem(int position) {
            return commentsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHodler vh = null;
            if (null == convertView) {
                vh = new ViewHodler();
                convertView = inflater.inflate(R.layout.story_list, null);
                vh.imgAnswer = (ImageView) convertView.findViewById(R.id.imgAnswer);
                vh.imgOtherPhoto = (ImageView) convertView.findViewById(R.id.imgOtherPhoto);
                vh.tvOtherNickName = (TextView) convertView.findViewById(R.id.tvOtherNickName);
                vh.tvTalk = (TextView) convertView.findViewById(R.id.tvTalk);
                vh.txDate = (TextView) convertView.findViewById(R.id.txDate);
                convertView.setTag(vh);
            } else {
                vh = (ViewHodler) convertView.getTag();
            }
            Picasso.with(ctx).load(PathUtils.portrait + commentsList.get(position).getUsers().get(position).getPortrait())
                    .into(vh.imgOtherPhoto);
            vh.tvOtherNickName.setText(commentsList.get(position).getUsers().get(position).getNickname());
            vh.tvTalk.setText(commentsList.get(position).getComments());
            Log.e("TT", commentsList.get(position).getComments());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日   HH:mm");
            Long time = commentsList.get(position).getTime();
            String s = sdf.format(new Date(time * 1000L));
            vh.txDate.setText(s);
            return convertView;
        }
    }
    private void sendStory() {
      List<File>  files=new ArrayList<>();
     files.add(new File(PathUtils.imgpath+story.getPics().toString()));
        OkHttpUtils.post(PathUtils.sendStory).params("uid", MyInfo.getInstace().getId()).params("story_info",story.getStory_info()).addFileParams("photo[]",files)
                .params("userpass",MyInfo.getInstace().getUserpass()).params("lng",story.getLng()).params("lat",story.getLat()).params("city",story.getCity())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s.trim());
                            String result = jsonObject.optString("result");
                            if (result.equals("1")){
                                Toast.makeText(StoryActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(StoryActivity.this, "故事发表失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
    private class ViewHodler {
        public ImageView imgOtherPhoto, imgAnswer;
        public TextView tvOtherNickName, tvTalk, txDate;
    }

    private class GirdViewAdapter extends BaseAdapter {
        private Context ctx;
        private LayoutInflater inflater;

        public GirdViewAdapter(Context ctx) {
            this.ctx = ctx;
            inflater = (LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return story.getPics() == null ? 0 : story.getPics().size();
        }

        @Override
        public Object getItem(int position) {
            return story.getPics().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HolderView vh = null;
            if (convertView == null) {
                vh = new HolderView();
                convertView = inflater.inflate(R.layout.gird_itme, null);
                vh.imageView = (ImageView) convertView.findViewById(R.id.imgPic);
                convertView.setTag(vh);
                vh.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                vh.imageView.setPadding(5, 0, 5, 0);

            } else {
                vh = (HolderView) convertView.getTag();
            }
            if (null != story.getPics()) {
                for (int i = 0; i < story.getPics().size(); i++) {
                    Picasso.with(ctx).load(PathUtils.imgpath + story.getPics().get(position)).into(vh.imageView);
                    Log.e("11", PathUtils.imgpath + story.getPics().get(i));
                }
            }
            return convertView;
        }
    }

    private class HolderView {
        public ImageView imageView;
    }
}
