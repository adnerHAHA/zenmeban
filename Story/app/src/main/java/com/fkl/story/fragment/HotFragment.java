package com.fkl.story.fragment;


import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fkl.story.R;
import com.fkl.story.StoryActivity;
import com.fkl.story.db.DbManage;
import com.fkl.story.entity.Story;
import com.fkl.story.entity.User;
import com.fkl.story.utils.PathUtils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.AbsCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class HotFragment extends Fragment implements AbsListView.OnScrollListener ,AdapterView.OnItemClickListener{
    private ListView lvHot;            //展示故事
    private ListViewAdapter adapter;//lvHot的适配器
    private List<Story> storyList =new ArrayList<>();//存储故事对象
    private List<User> users=new ArrayList<>();//存储用户对象
    private Story story=null;
    private User user=null;
    List<String> picslist=null;//存放图片地址
    List<String> portraits=new ArrayList<>();//头像地址
     int pageIndex=0;           //当前页数
    List<String> pics=new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ListViewAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_hot, null);
        lvHot = (ListView) inflate.findViewById(R.id.lvHot);
        return inflate;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lvHot.setAdapter(adapter);
        lvHot.setOnScrollListener(this);
        initData();
        lvHot.setOnItemClickListener(this);

    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 100:
                    adapter.notifyDataSetChanged();
                    break;

            }
        }
    };

    /**
     * 初始化数据
     */
    private void initData() {
        OkHttpUtils.post(PathUtils.getStorys).params("type","hot").execute(new MyCallBack() {
            @Override
            public void onSuccess(List<Story> stories, Call call, Response response) {
               handler.sendEmptyMessage(100);
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DbManage dbManage = new DbManage(getActivity());
        ContentValues values = new ContentValues();
        values.put("sid",storyList.get(position).getId());
        values.put("story_info",storyList.get(position).getStory_info());
        values.put("story_time",storyList.get(position).getStory_time());
        values.put("uid",storyList.get(position).getUid());
        values.put("city",storyList.get(position).getCity());
        dbManage.add(values);
        Intent intent = new Intent(getActivity(), StoryActivity.class);
        intent.putExtra("story",storyList.get(position));
        intent.putExtra("user",storyList.get(position).getList().get(position));
        startActivity(intent);

    }

    /**
     * 网络请求回调以及数据解析类
     */
    private abstract class MyCallBack extends AbsCallback<List<Story>> {
        @Override
        public List<Story> parseNetworkResponse(Response response) throws Exception {
            String result = response.body().string();
            JSONObject object = new JSONObject(result);
            JSONArray data = object.getJSONArray("data");
            for (int i=0;i<data.length();i++){
                JSONObject obj = data.getJSONObject(i);
                String id = obj.optString("id");
                Long story_time = obj.optLong("story_time");
                String story_info = obj.optString("story_info");
              //  String pic = obj.optString("pics");
                JSONArray pics = obj.optJSONArray("pics");
                 picslist=new ArrayList<>();
                if (null!=pics){
                for (int j=0;j<pics.length();j++) {
                   String s=pics.getString(j);
                    picslist.add(s);
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
                String useremail =muser.optString("useremail");
                String nickname = muser.optString("nickname");
                String birthday = muser.optString("birthday");
                String portrait = muser.optString("portrait");
                String signature = muser.optString("signature");
                portraits.add(portrait);
                user=new User(_id,username,usersex,useremail,nickname,birthday,portrait,signature);
                users.add(user);
                story=new Story(id,story_time,story_info,picslist,uid,lng,lat,city,readcount,comment,users);
                storyList.add(story);
            }

            return storyList;

        }
    }

    /**
     * 适配器
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
            return storyList==null?0:storyList.size();
        }

        @Override
        public Object getItem(int position) {
            return storyList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                vh = new ViewHoldle();
                convertView = inflater.inflate(R.layout.hot_layout, null);
                vh.imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
                vh.imaSex = (ImageView) convertView.findViewById(R.id.imaSex);
               //vh.imgContentStory = (ImageView) convertView.findViewById(R.id.imgContentStory);
                vh.mLinearLayout= (ImageView) convertView.findViewById(R.id.imgLinearLayOut);
                vh.tvLoveNum = (TextView) convertView.findViewById(R.id.tvLoveNum);
                vh.tvNickName = (TextView) convertView.findViewById(R.id.tvNickName);
                vh.tvStory = (TextView) convertView.findViewById(R.id.tvStory);
                vh.tvTalkNum = (TextView) convertView.findViewById(R.id.tvTalkNum);
                vh.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
                vh.btnLove = (ImageButton) convertView.findViewById(R.id.btnLove);
                vh.btnShare = (ImageButton) convertView.findViewById(R.id.btnShare);
                vh.btnTalk = (ImageButton) convertView.findViewById(R.id.btnTalk);
                vh.tvCity= (TextView) convertView.findViewById(R.id.tvAddress);
                vh.mLinearLayout.setScaleType(ImageView.ScaleType.FIT_XY);
                convertView.setTag(vh);

            } else {
                vh = (ViewHoldle) convertView.getTag();
            }
                vh.tvNickName.setText(storyList.get(position).getList().get(position).getNickname());
                vh.tvCity.setText(storyList.get(position).getCity());
                vh.tvStory.setText(storyList.get(position).getStory_info());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Long story_time = storyList.get(position).getStory_time();
                String s = sdf.format(new Date(story_time * 1000L));
                vh.tvTime.setText(s);

                Picasso.with(ctx).load(PathUtils.portrait + portraits.get(position)).placeholder(R.mipmap.mm_11).into(vh.imgPhoto);
                Log.e("portrait", PathUtils.portrait + portraits.get(position));
                int talkNum = storyList.get(position).getComment();
                int loveNum = storyList.get(position).getReadcount();
                if (talkNum > 100) {
                    vh.tvTalkNum.setText(100 + "+");
                } else {
                    vh.tvTalkNum.setText(talkNum + "");
                }
                    vh.tvLoveNum.setText(loveNum + "");

                Picasso.with(ctx).load(PathUtils.portrait + storyList.get(position).getList().get(position).getPortrait())
                        .fit().into(vh.imgPhoto);
                pics.addAll(storyList.get(position).getPics());
                if (0!= pics.size()) {
                    Picasso.with(ctx).load(PathUtils.imgpath + pics.get(0)).into(vh.mLinearLayout);
                }

            String usersex = storyList.get(position).getList().get(position).getUsersex();
            if(usersex.equals("0")){
                vh.imaSex.setImageResource(R.mipmap.icon_man);
            }else if (usersex.equals("1")) {
                vh.imaSex.setImageResource(R.mipmap.icon_woman);
            }
            return convertView;
        }
    }

    private class ViewHoldle {
        public ImageView imgPhoto, imaSex;
        public ImageButton btnTalk, btnLove, btnShare;
        public TextView tvNickName, tvTime, tvStory, tvTalkNum, tvLoveNum,tvCity;
        public ImageView mLinearLayout;
    }




    boolean isBottom=false;//是否滑动到底的监听
    /**
     * listview滑动时状态改变的监听
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(isBottom&&scrollState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
            pageIndex++;
            initData();
        }
      if (scrollState == SCROLL_STATE_IDLE || SCROLL_STATE_TOUCH_SCROLL == scrollState) {
            Picasso.with(getActivity()).pauseTag(getActivity());
      } else {
          Picasso.with(getActivity()).resumeTag(getActivity());
      }

    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        isBottom=firstVisibleItem+visibleItemCount==totalItemCount;

    }
}
