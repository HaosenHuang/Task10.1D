package com.example.trucksharingapp.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.trucksharingapp.model.UserModel;

import java.util.List;

/**
 * com.example.trucksharingapp.db
 * 2022/5/4
 *
 * @author Created on {DATE}
 * Major Function：<b></b>
 * @author mender，Modified Date Modify Content:
 */
@Dao
public interface UserDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(UserModel data);


  @Query("SELECT * FROM usermodel WHERE account = :account")
  UserModel getByAccount(String account);

  @Query("SELECT * FROM usermodel WHERE userId = :userId")
  UserModel getByUserId(String userId);
}
