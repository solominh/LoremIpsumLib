package com.lorem_ipsum.utils;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by hoangminh on 12/2/15.
 */
public class ObjectUtils {

    //-----------------------------------------------------------------------------
    // Serialize object to byte array - hoangminh - 2:24 PM - 2/3/16
    //-----------------------------------------------------------------------------

    public static byte[] serialize(Serializable obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;

        try {
            // Wrap
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

            // Write
            objectOutputStream.writeObject(obj);

            // Return byte array
            return byteArrayOutputStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close
            if (objectOutputStream != null)
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            // Close
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Object deserialize(byte[] data) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = null;
        Object object = null;
        try {
            // Wrap
            objectInputStream = new ObjectInputStream(byteArrayInputStream);

            // Read
            object = objectInputStream.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close
            if (objectInputStream != null)
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            // Close
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return object;
    }

    //-----------------------------------------------------------------------------
    // Serialize object to string - hoangminh - 2:24 PM - 2/3/16
    //-----------------------------------------------------------------------------

    public static String toJson(Serializable obj) {
        Gson gson = GsonUtils.getGson();
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        Gson gson = GsonUtils.getGson();
        return gson.fromJson(jsonString, clazz);
    }

}
