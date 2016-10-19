package cn.tech.zhangls.mddemo.home.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cn.tech.zhangls.mddemo.home.fragment.BlankFragment;
import cn.tech.zhangls.mddemo.home.fragment.ListFragment;
import cn.tech.zhangls.mddemo.home.fragment.SettingFragment;

/**
 * Created by zhangls on 2016/6/10.
 *
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<String> list;

    public FragmentAdapter(FragmentManager fm, Context context, List<String> list) {
        super(fm);
        this.context = context;
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = BlankFragment.newInstance();
                break;
            case 1:
                fragment = ListFragment.newInstance();
                break;
            case 2:
                fragment = SettingFragment.newInstance();
                break;
            default:
                fragment = BlankFragment.newInstance();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }
}
