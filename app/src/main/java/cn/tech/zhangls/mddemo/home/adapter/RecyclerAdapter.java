package cn.tech.zhangls.mddemo.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.tech.zhangls.mddemo.R;

/**
 * Created by zhangls on 2016/10/11.
 *
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<String> list;
    private OnItemClickListener mOnItemClickListener = null;
    private RecyclerView mRecyclerView = null;

    public RecyclerAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.list = list;
    }

    public void changeData(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void clearData() {
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                list.remove(i);
            }
            notifyItemRangeRemoved(0, list.size());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.fragment_blank_recycler_item, parent, false);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.nameText.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null && mRecyclerView != null) {
            int position = mRecyclerView.getChildAdapterPosition(view);
            mOnItemClickListener.onItemClick(mRecyclerView, view, position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.mRecyclerView = null;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText;

        MyViewHolder(View itemView) {
            super(itemView);
            nameText = (TextView) itemView.findViewById(R.id.recycler_item_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView recyclerView, View view, int position);
    }
}