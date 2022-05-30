package com.example.trucksharingapp.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.trucksharingapp.model.OrderModel;
import com.example.trucksharingapp.model.UserModel;

import java.util.List;

/**
 * com.example.trucksharingapp.db
 * 2022/5/5
 *
 * @author Created on {DATE}
 * Major Function：<b></b>
 * @author mender，Modified Date Modify Content:
 */
@Dao
public interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OrderModel data);


    @Query("SELECT * FROM OrderModel")
    List<OrderModel> getAll();

    @Query("SELECT * FROM OrderModel WHERE userId = :userId")
    List<OrderModel> getByUserId(String userId);
}
