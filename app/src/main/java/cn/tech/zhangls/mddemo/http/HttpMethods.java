package cn.tech.zhangls.mddemo.http;

import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.tech.zhangls.mddemo.http.model.Cook;
import cn.tech.zhangls.mddemo.http.model.CookList;
import cn.tech.zhangls.mddemo.http.model.HttpResult;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangls on 2016/10/21.
 * <p>
 * 对网络请求进行封装
 */

public class HttpMethods {
    /**
     * Retrofit
     */
    private Retrofit mRetrofit;
    /**
     * CookService
     */
    private CookService mCookService;
    /**
     * 默认请求超时时间
     */
    private static final int DEFAULT_TIMEOUT = 5;

    /**
     * 私有构造方法
     */
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        mRetrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Contanst.BASE_URL)
                .build();

        mCookService = mRetrofit.create(CookService.class);
    }

    /**
     * 在访问HttpMethods时创建单例
     */
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> tHttpResult) {
            if (!tHttpResult.isStatus()) {
//                throw new ApiException(tHttpResult.isStatus());

            }
            return tHttpResult.getData();
        }
    }

    /**
     * 获取单例
     */
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取菜谱列表
     *
     * @param id   查询id
     * @param page 页数
     * @param rows 每页的数据量
     */
    public void getCookList(Subscriber<HttpResult<List<Cook>>> subscriber, int id, int page, int rows) {
        mCookService.getCookList(id, page, rows)
//                .map(new HttpResultFunc<List<Cook>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);//订阅
    }
}
