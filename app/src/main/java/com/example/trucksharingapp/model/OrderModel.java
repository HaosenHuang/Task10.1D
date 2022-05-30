package com.example.trucksharingapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * com.example.trucksharingapp.model
 * 2022/5/5
 *
 * @author Created on {DATE}
 * Major Function：<b></b>
 * @author mender，Modified Date Modify Content:
 */
@Entity
public class OrderModel implements Serializable {
  @PrimaryKey
  @NonNull
  public String id;
  public String senderName;
  public String userId;
  public String receiverName;
  public String pickDate;
  public String pickTime;
  public String location;
  public String dropOffLocation;
  public String goodType;
  public double weight;
  public double width;
  public double length;
  public double height;
  public String vehicleType;
  public String imageString;

  public int createTime;

  public String getDropOffLocation() {
    return dropOffLocation;
  }

  public OrderModel setDropOffLocation(String dropOffLocation) {
    this.dropOffLocation = dropOffLocation;
    return this;
  }

  public String getSenderName() {
    return senderName;
  }

  public OrderModel setSenderName(String senderName) {
    this.senderName = senderName;
    return this;
  }

  public String getUserId() {
    return userId;
  }

  public OrderModel setUserId(String userId) {
    this.userId = userId;
    return this;
  }

  public int getCreateTime() {
    return Integer.parseInt(id);
  }

  public OrderModel setCreateTime(int createTime) {
    this.createTime = createTime;
    return this;
  }

  public String getId() {
    return id;
  }

  public OrderModel setId(String id) {
    this.id = id;
    return this;
  }

  public String getReceiverName() {
    return receiverName;
  }

  public OrderModel setReceiverName(String receiverName) {
    this.receiverName = receiverName;
    return this;
  }

  public String getPickDate() {
    return pickDate;
  }

  public OrderModel setPickDate(String pickDate) {
    this.pickDate = pickDate;
    return this;
  }

  public String getPickTime() {
    return pickTime;
  }

  public OrderModel setPickTime(String pickTime) {
    this.pickTime = pickTime;
    return this;
  }

  public String getLocation() {
    return location;
  }

  public OrderModel setLocation(String location) {
    this.location = location;
    return this;
  }

  public String getGoodType() {
    return goodType;
  }

  public OrderModel setGoodType(String goodType) {
    this.goodType = goodType;
    return this;
  }

  public double getWeight() {
    return weight;
  }

  public OrderModel setWeight(double weight) {
    this.weight = weight;
    return this;
  }

  public double getWidth() {
    return width;
  }

  public OrderModel setWidth(double width) {
    this.width = width;
    return this;
  }

  public double getLength() {
    return length;
  }

  public OrderModel setLength(double length) {
    this.length = length;
    return this;
  }

  public double getHeight() {
    return height;
  }

  public OrderModel setHeight(double height) {
    this.height = height;
    return this;
  }

  public String getVehicleType() {
    return vehicleType;
  }

  public OrderModel setVehicleType(String vehicleType) {
    this.vehicleType = vehicleType;
    return this;
  }

  public String getImageString() {
    return imageString;
  }

  public OrderModel setImageString(String imageString) {
    this.imageString = imageString;
    return this;
  }
}
