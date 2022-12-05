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

        object_info.add("class", object_class.getName());
        object_info.add("id", object_id);

        JsonArrayBuilder field_list = Json.createArrayBuilder();

        for(Field f: object_class.getDeclaredFields()){
            f.setAccessible(true);

            JsonObjectBuilder field_info = Json.createObjectBuilder();
            Class field_DeclaringClass = f.getDeclaringClass();
            Object fieldObj = f.get(source);

            field_info.add("name", f.getName());
            field_info.add("declaring class", field_DeclaringClass.getName());

            if(f.getType().isPrimitive()){
                field_info.add("value",fieldObj.toString());
            }
            // if field is an object
            else{
                if(fieldObj == null){
                    field_info.add("reference", "null");
                }
                else if(object_tracking_map.containsKey(fieldObj)){
                    field_info.add("reference",object_tracking_map.get(fieldObj).toString());
                }
                else{
                    field_info.add("reference", Integer.toString(object_tracking_map.size()));
                    serializeHelper(fieldObj,object_list,object_tracking_map);
                }
            }
        }




    }

}
