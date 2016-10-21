package cn.tech.zhangls.mddemo.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.tech.zhangls.mddemo.R;
import cn.tech.zhangls.mddemo.home.adapter.ListAdapter;
import cn.tech.zhangls.mddemo.http.HttpMethods;
import cn.tech.zhangls.mddemo.http.model.Cook;
import cn.tech.zhangls.mddemo.http.model.HttpResult;
import rx.Subscriber;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
    /**
     * RecyclerView
     */
    private RecyclerView mRecyclerView;
    /**
     * ListAdapter
     */
    private ListAdapter mListAdapter;
    /**
     * 数据源
     */
    private List<String> mNameList;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance() {
        Bundle args = new Bundle();
        ListFragment fragment = new ListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findView(view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNameList = new ArrayList<>();
        mListAdapter = new ListAdapter(getContext(), mNameList);
        mRecyclerView.setAdapter(mListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mListAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(RecyclerView recyclerView, View view, int position) {
                Toast.makeText(getContext(), "===" + mNameList.get(position) + "===", Toast.LENGTH_SHORT).show();
            }
        });

        //获取菜谱列表
        getCookList(0, 1, 20);
    }

    /**
     * 通过findViewById对组件实例化
     *
     * @param view 父容器
     */
    private void findView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fg_list_recycler);
    }

    /**
     * 获取菜谱列表
     *
     * @param id   查询id
     * @param page 页数
     * @param rows 每页的数据量
     */
    private void getCookList(int id, int page, int rows) {
        Subscriber<HttpResult<List<Cook>>> subscriber = new Subscriber<HttpResult<List<Cook>>>() {
            @Override
            public void onCompleted() {
                Toast.makeText(getContext(), "菜谱加载完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), "菜谱获取失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(HttpResult<List<Cook>> cookList) {
                List<Cook> cooks = cookList.getData();
                for (int i = 0; i < cooks.size(); i++) {
                    mNameList.add(cooks.get(i).getName());
                }
                mListAdapter.changeData(mNameList);
            }
        };
        //取消http请求
//        subscriber.unsubscribe();
        HttpMethods.getInstance().getCookList(subscriber, id, page, rows);
    }
}