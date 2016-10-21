package cn.tech.zhangls.mddemo.home.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.tech.zhangls.mddemo.R;

/**
 * Created by zhangls on 2016/10/20.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> implements View.OnClickListener {
    /**
     * 上下文对象
     */
    private Context mContext;
    /**
     * 数据源
     */
    private List<String> mList;
    /**
     * RecyclerView Item 点击事件接口实例
     */
    private OnItemClickListener mOnItemClickListener = null;

    private RecyclerView mRecyclerView = null;

    public ListAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.fragment_list_recyler_item, parent, false);
//        view.setOnClickListener(this);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        myViewHolder.cardView.setOnClickListener(this);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.nameText.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        if (mRecyclerView != null && mOnItemClickListener != null) {
            int position = mRecyclerView.getChildAdapterPosition((View) v.getParent());
            mOnItemClickListener.OnItemClick(mRecyclerView, v, position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecyclerView = null;
    }

    /**
     * ViewHolder
     */
    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText;
        private CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            nameText = (TextView) itemView.findViewById(R.id.fg_list_card_name);
            cardView = (CardView) itemView.findViewById(R.id.fg_list_card_view);
        }
    }

    /**
     * RecyclerView Item 点击事件接口
     */
    public interface OnItemClickListener {
        void OnItemClick(RecyclerView recyclerView, View view, int position);
    }

    /**
     * 设置RecyclerView Item 点击事件监听
     *
     * @param mOnItemClickListener 监听接口
     */
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    /**
     * 刷新所有的数据
     *
     * @param list 数据列表
     */
    public void changeData(List<String> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    /**
     * 刷新Item数据
     *
     * @param str      新的数据
     * @param position 刷新位置
     */
    public void changeItemData(String str, int position) {
        mList.set(position, str);
        notifyItemChanged(position);
    }

    /**
     * 删除所有数据
     */
    public void deleteData() {
        if (mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                mList.remove(i);
            }
            notifyItemRangeChanged(0, mList.size());
        }
    }

    /**
     * 删除某个数据
     *
     * @param position 删除位置
     */
    public void deleteItemData(int position) {
        if (mList.size() >= position) {
            mList.remove(position);
            notifyItemRemoved(position);
        }
    }
}
