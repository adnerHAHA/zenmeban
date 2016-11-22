package com.fkl.story;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fkl.story.db.DbManage;
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

public class HistoryActivity extends AppCompatActivity {
    private ListView lvHistory;
    private ListViewAdapter adapter;
    private List<Story> stories = new ArrayList<>();
    private List<String> picslist = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private ImageButton imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        MyApplication.getInstace().addActivity(this);
        findView();
        adapter = new ListViewAdapter(HistoryActivity.this);
        lvHistory.setAdapter(adapter);
        initData();
    }

    private void initData() {
        DbManage dbManage = new DbManage(HistoryActivity.this);
        Cursor query = dbManage.query(null, null);
        while (query.moveToNext()) {
            String sid = query.getString(query.getColumnIndex("sid"));
            String story_info = query.getString(query.getColumnIndex("story_info"));
            Long story_time = query.getLong(query.getColumnIndex("story_time"));
            String uid = query.getString(query.getColumnIndex("uid"));
            String city = query.getString(query.getColumnIndex("city"));
            Story story = new Story();
            story.setId(sid);
            story.setStory_info(story_info);
            story.setStory_time(story_time);
            story.setUid(uid);
            story.setCity(city);
            OkHttpUtils.post(PathUtils.getStorys).params("sid",sid).params("page", 1).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    try {
                        JSONObject jsonObject = new JSONObject(s.trim());
                        String result = jsonObject.optString("result");
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            String id = obj.optString("id");
                            Long story_time = obj.optLong("story_time");
                            String story_info = obj.optString("story_info");
                            //  String pic = obj.optString("pics");
                            JSONArray pics = obj.optJSONArray("pics");

                            if (null != pics) {
                                for (int j = 0; j < pics.length(); j++) {
                                    String str = pics.getString(j);
                                    picslist.add(str);
                                }
                            }
                            String uid = obj.optString("uid");
                            String lng = obj.optString("lng");
                            String lat = obj.optString("lat");
                            String city = obj.optString("city");
                            int readcount = obj.optInt("readcount");
                            int comment = obj.optInt("comment");
                            JSONObject muser = obj.getJSONObject("user");
                            String _id = muser.optString("id");
                            String username = muser.optString("username");
                            String usersex = muser.optString("usersex");
                            String useremail = muser.optString("useremail");
                            String nickname = muser.optString("nickname");
                            String birthday = muser.optString("birthday");
                            String portrait = muser.optString("portrait");
                            String signature = muser.optString("signature");
                            User user = new User(_id, username, usersex, useremail, nickname, birthday, portrait, signature);
                            users.add(user);
                            Story story = new Story(id, story_time, story_info, picslist, uid, lng, lat, city, readcount, comment, users);
                            stories.add(story);
                            if (result.equals("1")) {
                                hd.sendEmptyMessage(100);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }

    }

    Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private void findView() {
        lvHistory = (ListView) findViewById(R.id.lvHistory);
        imgBack= (ImageButton) findViewById(R.id.toolbar_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryActivity.this.finish();

            }
        });
    }

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
            return stories == null ? 0 : stories.size();
        }

        @Override
        public Object getItem(int position) {
            return stories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                vh = new ViewHoldle();
                convertView = inflater.inflate(R.layout.history_layout, null);
                vh.imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
                vh.imaSex = (ImageView) convertView.findViewById(R.id.imaSex);
                vh.tvAddress= (TextView) convertView.findViewById(R.id.tvAddress);
                vh.imgStory = (ImageView) convertView.findViewById(R.id.imgStory);
                vh.tvLoveNum = (TextView) convertView.findViewById(R.id.tvLoveNum);
                vh.tvNickName = (TextView) convertView.findViewById(R.id.tvNickName);
                vh.tvStory = (TextView) convertView.findViewById(R.id.tvStory);
                vh.tvTalkNum = (TextView) convertView.findViewById(R.id.tvTalkNum);
                vh.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
                vh.btnLove = (ImageButton) convertView.findViewById(R.id.btnLove);
                vh.btnShare = (ImageButton) convertView.findViewById(R.id.btnShare);
                vh.btnTalk = (ImageButton) convertView.findViewById(R.id.btnTalk);
                convertView.setTag(vh);

            } else {
                vh = (ViewHoldle) convertView.getTag();
            }
            vh.tvStory.setText(stories.get(position).getStory_info());
            Log.e("his", stories.get(position).getStory_info());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Long story_time = stories.get(position).getStory_time();
            String s = sdf.format(new Date(story_time * 1000L));
            List<String> pics = new ArrayList<>();
            pics.addAll(stories.get(position).getPics());
            if (0 != pics.size()) {
                Picasso.with(ctx).load(PathUtils.imgpath + pics.get(0)).into(vh.imgStory);
                Log.e("hhh", PathUtils.imgpath + pics.get(0));
            }

            vh.tvTime.setText(s);
            vh.tvLoveNum.setText(stories.get(position).getReadcount() + "");
            vh.tvLoveNum.setText(stories.get(position).getComment() + "");
            vh.tvNickName.setText(stories.get(position).getList().get(position).getNickname());
            Picasso.with(ctx).load(PathUtils.portrait + stories.get(position).getList().get(position).getPortrait()).placeholder(R.mipmap.icon_mine)
                    .into(vh.imgPhoto);
            vh.tvAddress.setText(stories.get(position).getCity());
            return convertView;
        }
    }

    private class ViewHoldle {
        public ImageView imgPhoto, imgStory, imaSex;
        public ImageButton btnTalk, btnLove, btnShare;
        public TextView tvNickName, tvTime, tvStory, tvTalkNum, tvLoveNum,tvAddress;

    }
}
