package com.rjp.cnvteachers.beans;

import java.io.Serializable;

/**
 * Created by Shraddha on 4/26/2017.
 */

public class TopicBean implements Serializable{

    private String topic_name,topic_id;

    @Override
    public String toString() {
        return topic_name;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }
}
