package com.example.xq.soundofheart.bean;

/**
 * Created by Rocky on 2018/6/8 0008.
 */

public class PracticeResultBean {
    private String createTime;
    private float correctRate;
    private int id;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public float getCorrectRate() {
        return correctRate;
    }

    public void setCorrectRate(float correctRate) {
        this.correctRate = correctRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
