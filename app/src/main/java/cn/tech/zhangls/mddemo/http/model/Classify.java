package cn.tech.zhangls.mddemo.http.model;

/**
 * Created by zhangls on 2016/8/28.
 * <p>
 * 持久化对象
 * 菜谱分类
 */
public class Classify {
    private int id;
    private int cookclass; //菜谱分类 0 位顶级
    private String name;
    private String title;
    private String keywords;
    private String description;
    private int seq;//排序 从0。。。。10开始

    public int getCookclass() {
        return cookclass;
    }

    public void setCookclass(int cookclass) {
        this.cookclass = cookclass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
