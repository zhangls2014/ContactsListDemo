package cn.tech.zhangls.mddemo.http;

import java.util.List;

import cn.tech.zhangls.mddemo.http.model.Cook;
import cn.tech.zhangls.mddemo.http.model.CookList;
import cn.tech.zhangls.mddemo.http.model.HttpResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by zhangls on 2016/10/20.
 * <p>
 * Cook 网络请求类
 */

public interface CookService {
    /**
     * 查询菜谱列表
     *
     * @param id 菜谱id
     * @param page 菜谱列表页数
     * @param rows 每页的菜谱数量
     * @return 菜谱列表
     */
//    @GET("/api/cook/list")
//    Call<CookList> getCookList(@Query("id") long id, @Query("page") int page, @Query("rows") int rows);

    /**
     * 查询菜谱列表
     *
     * @param id   菜谱id
     * @param page 菜谱列表页数
     * @param rows 每页的菜谱数量
     * @return 菜谱列表
     */
    @GET("/api/cook/list")
    Observable<HttpResult<List<Cook>>> getCookList(@Query("id") long id, @Query("page") int page, @Query("rows") int rows);
}
