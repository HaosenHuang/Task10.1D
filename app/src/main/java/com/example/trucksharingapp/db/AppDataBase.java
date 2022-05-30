package com.example.trucksharingapp.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.trucksharingapp.MyApplication;
import com.example.trucksharingapp.model.OrderModel;
import com.example.trucksharingapp.model.UserModel;

/**
 * com.example.trucksharingapp.db
 * 2022/5/4
 *
 * @author Created on {DATE}
 * Major Function：<b></b>
 * @author mender，Modified Date Modify Content:
 */
@Database(entities =
        {UserModel.class, OrderModel.class}
        , exportSchema = false, version = 1)
public abstract class AppDataBase  extends RoomDatabase {
  private static AppDataBase appDataBase;

  public static synchronized AppDataBase getInstance() {
    if (appDataBase == null) {
      appDataBase = Room.databaseBuilder(
              MyApplication.getInstance().getApplicationContext()
              , AppDataBase.class
              , "truck_sharing_app.db")
              .allowMainThreadQueries() //可同步操作
              .build();
    }
    return appDataBase;
  }


  public abstract UserDao userDao();

  public abstract OrderDao orderDao();
}
