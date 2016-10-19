package cn.tech.zhangls.mddemo.home.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.tech.zhangls.mddemo.R;
import cn.tech.zhangls.mddemo.home.adapter.DividerItemDecoration;
import cn.tech.zhangls.mddemo.home.adapter.RecyclerAdapter;
import cn.tech.zhangls.mddemo.home.adapter.TitleItemDecoration;
import cn.tech.zhangls.mddemo.home.adapter.bean.NameBean;
import cn.tech.zhangls.mddemo.home.widget.IndexBar.IndexBarView;

public class BlankFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    //联系人列表
    List<String> mNameList;
    //中英文转换List
    private List<NameBean> nameBeanList;
    //RecyclerAdapter
    private RecyclerAdapter mRecycleAdapter;
    //字符串分割标志
    private final String SPLIT_FLAG = "@_@";
    //TitleItemDecoration
    private TitleItemDecoration mTitleList;
    private IndexBarView mIndexBarView;

    public BlankFragment() {
        // Required empty public constructor
    }

    public static BlankFragment newInstance() {
//        Bundle args = new Bundle();
//        args.putString("text", text);
        BlankFragment fragment = new BlankFragment();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
/*
      显示指示器DialogText
     */
        TextView mTvSideBarHint = (TextView) view.findViewById(R.id.tvSideBarHint);
        /*
      右侧边栏导航区域
     */
        mIndexBarView = (IndexBarView) view.findViewById(R.id.indexBar);
        mIndexBarView.setPressedShowTextView(mTvSideBarHint);//设置HintTextView
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mIndexBarView.setLayoutManager(mLayoutManager);

        mNameList = new ArrayList<>();
        nameBeanList = new ArrayList<>();
        mRecycleAdapter = new RecyclerAdapter(getContext(), mNameList);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setAdapter(mRecycleAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        mTitleList = new TitleItemDecoration(getContext(), nameBeanList);
        mRecyclerView.addItemDecoration(mTitleList);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getLoaderManager().initLoader(0, null, BlankFragment.this);
                }
            });
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecycleAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View view, int position) {
                Toast.makeText(getContext(), "---" + mNameList.get(position) + "---", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * 联系人查询条件
     */
    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
        };
        int DISPLAY_NAME_PRIMARY = 0;
//        int NUMBER = 1;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                ProfileQuery.PROJECTION,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        mNameList = new ArrayList<>();
        while (!data.isAfterLast()) {
            mNameList.add(data.getString(ProfileQuery.DISPLAY_NAME_PRIMARY));
            data.moveToNext();
        }
        //对数据进行排序
        if (mNameList.size() > 0){
            nameBeanList = initData(mNameList);
            mNameList = new ArrayList<>();
            for (NameBean nameBean : nameBeanList) {
                mNameList.add(nameBean.getName());
            }
            mRecycleAdapter.changeData(mNameList);
            mTitleList.setData(nameBeanList);
            mIndexBarView.setSourceData(nameBeanList);//设置数据源
        }
    }

    /**
     * 组织数据源， 对数据排序
     * @param list 数据列表
     */
    private List<NameBean> initData(List<String> list) {
        nameBeanList = new ArrayList<>();
        NameBean nameToPinyin;
        StringBuilder builder;

        //将数据源转换成拼音
        for (String string : list) {
            nameToPinyin = new NameBean();
            builder = new StringBuilder();
            for (int i = 0; i < string.length(); i++) {
                    builder.append(Pinyin.toPinyin(string.charAt(i))).append(SPLIT_FLAG);
            }
            nameToPinyin.setName(string);
            nameToPinyin.setPinyin(builder.toString());
            String substring = builder.toString().substring(0, 1);
            if (substring.matches("[A-Z]")) {
                nameToPinyin.setTag(substring);
            } else {
                nameToPinyin.setTag("#");
            }
            nameBeanList.add(nameToPinyin);
            //对数据源排序
//            nameBeanList.sort(new Comparator<NameBean>() {
//                @Override
//                public int compare(NameBean o1, NameBean o2) {
//                    return 0;
//                }
//            });
        }
        /**
         * 1：对拼音转换成的字符串按字比较，提高准确度，防止同字不相邻的情况出现
         * 2：分离标志不会出现在字符串的头部，尾部的分离标志不会增加数组的长度
         */
        Collections.sort(nameBeanList, new Comparator<NameBean>() {
            @Override
            public int compare(NameBean o1, NameBean o2) {
                String[] str1, str2;
                str1 = o1.getPinyin().split(SPLIT_FLAG);
                str2 = o2.getPinyin().split(SPLIT_FLAG);
                //以短的字符串的长度作为循环次数
                if (str1.length < str2.length) {
                    for (int i = 0; i < str1.length; i++) {
                        int result = str1[i].compareTo(str2[i]);
                        if (result != 0){
                            return result;
                        }
                    }
                    return -1;
                } else if (str1.length == str2.length){
                    for (int i = 0; i < str1.length; i++) {
                        int result = str1[i].compareTo(str2[i]);
                        if (result != 0){
                            return result;
                        }
                    }
                    return 0;
                } else {
                    for (int i = 0; i < str2.length; i++) {
                        int result = str1[i].compareTo(str2[i]);
                        if (result != 0){
                            return result;
                        }
                    }
                    return 1;
                }
            }
        });
        return nameBeanList;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}