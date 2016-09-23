package com.carousel.bean;

/**
 * Created by seekingL on 2016/7/26.
 *
 */
public class MatchListBean {

    public int id;

    public String title;

    public String imgurl;

    public int eventtype;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", eventtype=" + eventtype +
                '}';
    }
}
