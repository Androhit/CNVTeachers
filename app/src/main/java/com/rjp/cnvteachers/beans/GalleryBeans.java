package com.rjp.cnvteachers.beans;

import java.io.Serializable;

/**
 * Created by Shraddha on 3/29/2017.
 */
public class GalleryBeans implements Serializable{

    private String path,description;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
