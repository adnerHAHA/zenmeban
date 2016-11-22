package com.fkl.music;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fkl.music.bean.MusicInfo;
import com.fkl.music.fragment.FindFrag;
import com.fkl.music.fragment.SingerList;
import com.fkl.music.fragment.SongListFrag;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup radioGroup;
    private List<Fragment> fragments=null;
    private    int lastposition=-1;
    private List<MusicInfo> musicInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup= (RadioGroup) findViewById(R.id.rg);
        fragments=new ArrayList<Fragment>();
        addFrament();
        musicInfos=new ArrayList<>();
        radioGroup.setOnCheckedChangeListener(this);




    }

    private void addFrament() {
        SongListFrag songListFrag = new SongListFrag();
        Bundle bundle = new Bundle();
       // bundle.putBundle("infos",);
        fragments.add(songListFrag);
        SingerList singerList = new SingerList();
        fragments.add(singerList);
        FindFrag findFrag = new FindFrag();
        fragments.add(findFrag);
    }




    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
       for (int i=0;i<group.getChildCount();i++){
           RadioButton rb= (RadioButton) group.getChildAt(i);
           if(rb.isChecked()){
               showFragment(i);
               break;
           }
       }
    }

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
            ft.add(R.id.frag, f);
        }
        ft.commit();
        lastposition = position;
    }

    }

