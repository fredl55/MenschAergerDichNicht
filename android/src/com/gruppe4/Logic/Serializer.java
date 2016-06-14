package com.gruppe4.Logic;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by manfrededer on 20.04.16.
 */
public class Serializer {
    private Serializer(){}

    public static byte[] serialize(Object obj){
        try{
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            ObjectOutputStream o = new ObjectOutputStream(b);
            o.writeObject(obj);
            return b.toByteArray();
        }catch (IOException e){
            Log.e("ERROR",e.getMessage());
        }catch(Exception e){
            Log.e("ERROR",e.getMessage());
        }
        return new byte[0];
    }

    public static Object deserialize(byte[] bytes){
        try{
            ByteArrayInputStream b = new ByteArrayInputStream(bytes);
            ObjectInputStream o = new ObjectInputStream(b);
            return o.readObject();
        }catch (IOException e){
            Log.e("MenschAergerDichNicht",e.getMessage());
        }catch (ClassNotFoundException e){
            Log.e("MenschAergerDichNicht", e.getMessage());
        }catch (Exception e){
            Log.e("MenschAergerDichNicht", e.getMessage());

        }
        return null;
    }
}
