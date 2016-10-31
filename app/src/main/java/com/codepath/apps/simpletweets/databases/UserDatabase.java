package com.codepath.apps.simpletweets.databases;

import com.codepath.apps.simpletweets.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by mkhade on 10/30/2016.
 */

@Database(name = UserDatabase.NAME, version = MyDatabase.VERSION)
public class UserDatabase {
    public static final String NAME = "UserDataBase";

    public static final int VERSION = 1;
}