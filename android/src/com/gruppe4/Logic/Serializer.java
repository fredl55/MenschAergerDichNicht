package com.gruppe4.Logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by manfrededer on 20.04.16.
 */
public class Serializer {
    public static byte[] serialize(Object obj){
        try{
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            ObjectOutputStream o = new ObjectOutputStream(b);
            o.writeObject(obj);
            return b.toByteArray();
        }catch (IOException e){
        }
        return null;
    }

    public static Object deserialize(byte[] bytes){
        try{
            ByteArrayInputStream b = new ByteArrayInputStream(bytes);
            ObjectInputStream o = new ObjectInputStream(b);
            return o.readObject();
        }catch (IOException e){

        }catch (ClassNotFoundException d){
        }
        return null;
    }
}
