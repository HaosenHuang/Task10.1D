package com.example.trucksharingapp.utli;

import android.graphics.Movie;
import android.os.Build;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import androidx.collection.SimpleArrayMap;


import com.example.trucksharingapp.db.AppDataBase;
import com.example.trucksharingapp.model.UserModel;
import com.tencent.mmkv.MMKV;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class KVUtils {


    /**
     * 删除字符串最后两个字符
     * @param str
     * @return
     */
    public static String deleteLast2(String str) {
        if (str.equals("")) {
            return str;
        } else {
            return str.substring(0, str.length() - 2);
        }
    }
    /**
     * 获取当前登录用户
     * @return
     */
    public static UserModel getLoginUser(){
        String accountString = getString("accountString","");
        UserModel user = AppDataBase.getInstance().userDao().getByUserId(accountString);


        return user;
    }



    public static boolean putString(String key, String value) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.encode(key, value);
    }

    //储存String 带id的
    public static boolean putString(String id, String key, String value) {
        MMKV mmkv = MMKV.mmkvWithID(id);
        return mmkv.encode(key, value);
    }

    //拿出String  没有默认值的
    public static String getString(String key) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.decodeString(key);
    }

    public static String getString(String key, String defaultValue) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.decodeString(key, defaultValue);
    }

    //拿出带id的String   defaultValue=默认值
    public static String getString(String id, String key, String defaultValue) {
        MMKV mmkv = MMKV.mmkvWithID(id);
        return mmkv.decodeString(key, defaultValue);
    }

    //储存int
    public static boolean putint(String key, int value) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.encode(key, value);
    }

    //储存int 带id的
    public static boolean putint(String id, String key, int value) {
        MMKV mmkv = MMKV.mmkvWithID(id);
        return mmkv.encode(key, value);
    }

    //拿出int  没有默认值的
    public static int getint(String key) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.decodeInt(key);
    }

    //拿出int   defaultValue=默认值
    public static int getint(String key, int defaultValue) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.decodeInt(key, defaultValue);
    }

    //拿出带id的int   defaultValue=默认值
    public static int getint(String id, String key, int defaultValue) {
        MMKV mmkv = MMKV.mmkvWithID(id);
        return mmkv.decodeInt(key, defaultValue);
    }


    //Boolean
//储存Boolean
    public static boolean putBoolean(String key, boolean value) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.encode(key, value);
    }

    //储存Boolean 带id的
    public static boolean putBoolean(String id, String key, boolean value) {
        MMKV mmkv = MMKV.mmkvWithID(id);
        return mmkv.encode(key, value);
    }

    //拿出Boolean  没有默认值的
    public static Boolean getBoolean(String key) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.decodeBool(key);
    }

    //拿出Boolean   defaultValue=默认值
    public static Boolean getBoolean(String key, boolean defaultValue) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.decodeBool(key, defaultValue);
    }

    //拿出带id的Boolean   defaultValue=默认值
    public static Boolean getBoolean(String id, String key, boolean defaultValue) {
        MMKV mmkv = MMKV.mmkvWithID(id);
        return mmkv.decodeBool(key, defaultValue);
    }


    //Byte
//储存Byte
    public static boolean putByte(String key, byte[] value) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.encode(key, value);
    }

    //储存Byte 带id的
    public static boolean putByte(String id, String key, byte[] value) {
        MMKV mmkv = MMKV.mmkvWithID(id);
        return mmkv.encode(key, value);
    }

    //拿出Byte  没有默认值的
    public static byte[] getByte(String key) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.decodeBytes(key);
    }

    //拿出Byte   defaultValue=默认值
    public static byte[] getByte(String key, byte[] defaultValue) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.decodeBytes(key, defaultValue);
    }

    //拿出带id的Byte   defaultValue=默认值
    public static byte[] getByte(String id, String key, byte[] defaultValue) {
        MMKV mmkv = MMKV.mmkvWithID(id);
        return mmkv.decodeBytes(key, defaultValue);
    }


    //Double
//储存Double
    public static boolean putDouble(String key, double value) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.encode(key, value);
    }

    //储存Double 带id的
    public static boolean putDouble(String id, String key, double value) {
        MMKV mmkv = MMKV.mmkvWithID(id);
        return mmkv.encode(key, value);
    }

    //拿出Double   没有默认值的
    public static double getDouble(String key) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.decodeDouble(key);
    }

    //拿出Double    defaultValue=默认值
    public static double getDouble(String key, double defaultValue) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.decodeDouble(key, defaultValue);
    }

    //拿出带id的Double    defaultValue=默认值
    public static double getDouble(String id, String key, double defaultValue) {
        MMKV mmkv = MMKV.mmkvWithID(id);
        return mmkv.decodeDouble(key, defaultValue);
    }


    //Float
//储存Float
    public static boolean putFloat(String key, float value) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.encode(key, value);
    }

    //储存Float 带id的
    public static boolean putFloat(String id, String key, float value) {
        MMKV mmkv = MMKV.mmkvWithID(id);
        return mmkv.encode(key, value);
    }

    //拿出Float   没有默认值的
    public static float getFloat(String key) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.decodeFloat(key);
    }

    //Float    defaultValue=默认值
    public static float getFloat(String key, float defaultValue) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.decodeFloat(key, defaultValue);
    }

    //拿出带id的Float    defaultValue=默认值
    public static float getFloat(String id, String key, float defaultValue) {
        MMKV mmkv = MMKV.mmkvWithID(id);
        return mmkv.decodeFloat(key, defaultValue);
    }


    //Long
//储存Long
    public static boolean putLong(String key, long value) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.encode(key, value);
    }

    //储存Long 带id的
    public static boolean putLong(String id, String key, long value) {
        MMKV mmkv = MMKV.mmkvWithID(id);
        return mmkv.encode(key, value);
    }

    //拿出Long   没有默认值的
    public static float getLong(String key) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.decodeLong(key);
    }

    //Long    defaultValue=默认值
    public static float getLong(String key, long defaultValue) {
        MMKV mmkv = MMKV.defaultMMKV();
        return mmkv.decodeLong(key, defaultValue);
    }

    //拿出带id的Long    defaultValue=默认值
    public static float getLong(String id, String key, long defaultValue) {
        MMKV mmkv = MMKV.mmkvWithID(id);
        return mmkv.decodeLong(key, defaultValue);
    }

    public static boolean isEmpty(final Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj.toString().isEmpty()) {
            return true;
        }
        if (obj instanceof CharSequence && obj.toString().length() == 0) {
            return true;
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return true;
        }
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SimpleArrayMap && ((SimpleArrayMap) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() == 0) {
                return true;
            }
        }
        if (obj instanceof LongSparseArray && ((LongSparseArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (obj instanceof LongSparseArray
                    && ((LongSparseArray) obj).size() == 0) {
                return true;
            }
        }
        return false;
    }
}
