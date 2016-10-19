package cn.tech.zhangls.mddemo.home.adapter.bean;

import cn.tech.zhangls.mddemo.home.widget.IndexBar.bean.BaseIndexPinyinBean;

/**
 * Created by zhangls on 2016/10/17.
 *
 */

public class NameBean extends BaseIndexPinyinBean {
    /**
     * 原始的字符串
     */
    private String name;
    /**
     * 转换成拼音的字符串
     */
    private String pinyin;
    /**
     * 汉字第一字的首字母
     */
    private String Tag;

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public String getTarget() {
        return name;
    }
}
