package com.example.ali.easyrepo;

import com.example.easyrepolib.sqlite.Model;

/**
 * Created by ali on 8/23/18.
 */

public class User implements Model {

    private String id;
    public String name;
    public String lName;
    public int age;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
