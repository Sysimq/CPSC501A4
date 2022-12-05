package org.example;

import java.lang.reflect.Field;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.*;
import javax.json.*;

/**
 * Some of the code are taken from Course Website written by Johnathan Hudson
 * https://pages.cpsc.ucalgary.ca/~jwhudson/CPSC501F22/code/Reflection4GeneralPurpose/JSONMain.java
 * **/

public class Serializer {
    public static JsonObject serializeObject(Object object) throws Exception {
        JsonArrayBuilder object_list = Json.createArrayBuilder();
        serializeHelper(object, object_list, new IdentityHashMap());

        JsonObjectBuilder json_base_object = Json.createObjectBuilder();
        json_base_object.add("objects", object_list);
        return json_base_object.build();
    }

    private static void serializeHelper(Object source, JsonArrayBuilder object_list, Map object_tracking_map)throws Exception{
        //Unique Object ID
        String object_id = Integer.toString(object_tracking_map.size());
        object_tracking_map.put(source, object_id);

        // Get the object class
        Class object_class = source.getClass();
        JsonObjectBuilder object_info = Json.createObjectBuilder();
        JsonObjectBuilder array_object = Json.createObjectBuilder();

        if(object_class == null){
            object_info.add("reference", "null");
        }
        if (object_class.isArray()){
            JsonObjectBuilder array_info = Json.createObjectBuilder();
            int length = Array.getLength(source);
            Class componentType = source.getClass().getComponentType();

            object_info.add("class", object_class.getName());
            object_info.add("id",object_id);
            JsonArrayBuilder arraylist = Json.createArrayBuilder();
            object_info.add("type","array");
            object_info.add("length",Integer.toString(Array.getLength(source)));
            for(int i = 0; i < length; i++){
                String fields = String.valueOf(Array.get(source,i));
                Object arrayObject = Array.get(source,i);
                if(arrayObject == null){
                    array_info.add("reference","null");
                }
                else if(!componentType.isPrimitive()){
                    if(object_tracking_map.containsKey(arrayObject)){
                        array_info.add("reference", object_tracking_map.get(arrayObject).toString());
                    }
                    else{
                        array_info.add("reference",Integer.toString(object_tracking_map.size()));
                        serializeHelper(arrayObject, object_list, object_tracking_map);
                    }
                }else{
                    array_info.add("value", arrayObject.toString());
                }
                arraylist.add(array_info);
            }
            object_info.add("entries",arraylist);

        }

        else if (source instanceof ArrayList<?>){
            object_info.add ("class", object_class.getName());
            object_info.add("id",object_id);
        }




    }

}
