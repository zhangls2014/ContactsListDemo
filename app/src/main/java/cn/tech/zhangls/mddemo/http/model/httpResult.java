package cn.tech.zhangls.mddemo.http.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhangls on 2016/10/21.
 * <p>
 * 网络请求结果类
 */

public class HttpResult<T> {
    @SerializedName("status")
    private boolean status;
    @SerializedName("total")
    private long total;
    @SerializedName("tngou")
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
