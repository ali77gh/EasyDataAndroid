package com.example.ali.easyrepo;

import android.content.Context;

import com.example.easyrepolib.sqlite.GenericDAO;

public class UserDao extends GenericDAO<User> {

    public UserDao(Context context) {
        super(context, User.class, "UsersTable", true);
    }
}
