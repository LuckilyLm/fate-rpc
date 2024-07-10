package org.fate.example.common.model;

import java.io.Serializable;

/**
 * @Author: Fate
 * @Date: 2024/7/10 22:49
 **/
public class User implements Serializable {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
