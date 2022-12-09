package org.example;

import javax.json.*;
import java.lang.reflect.*;
import java.util.*;

/*
 Some of the code are taken from Course Website written by Johnathan Hudson
 https://pages.cpsc.ucalgary.ca/~jwhudson/CPSC501F22/code/Reflection4GeneralPurpose/JSON/Deserializer.java
 */
public class Deserializer {
    public static Object deserializeObject(JsonObject json_object) throws Exception {
        JsonArray object_list = json_object.getJsonArray("objects");
        Map object_tracking_map = new HashMap();
        createInstances(object_tracking_map, object_list);
        assignFieldValues(object_tracking_map, object_list);
        return object_tracking_map.get("0");
    }
//

    private static void createInstances(Map object_tracking_map, JsonArray object_list) throws Exception {
        for (int i = 0; i < object_list.size(); i++) {
            JsonObject object_info = object_list.getJsonObject(i);
            Class object_class = Class.forName(object_info.getString("class"));
            if(object_class.isArray()){
                Class array_class = object_class.getComponentType();
                int length = Integer.valueOf(object_info.getString("length"));
                Object array_instance = Array.newInstance(array_class, length);
                object_tracking_map.put(object_info.getString("id"),array_instance);
            }
            else{
                Constructor constructor = object_class.getDeclaredConstructor();

                if(!Modifier.isPublic(constructor.getModifiers())){
                    constructor.setAccessible(true);
                }
                Object object_instance = constructor.newInstance();

                object_tracking_map.put(object_info.getString("id"),object_instance);
            }
        }
    }

    private static void assignFieldValues(Map object_tracking_map, JsonArray object_list) throws Exception {
        for(int i=0; i < object_list.size(); i++){
            JsonObject object_info = object_list.getJsonObject(i);
            Class object_class = Class.forName(object_info.getString("class"));
            Object object_instance = object_tracking_map.get(object_info.getString("id"));
            if(object_class.isArray()){
                JsonArray object_fields = object_info.getJsonArray("entries");
                int length = object_fields.size();
                Class cType = object_instance.getClass().getComponentType();

                for ( int j =0; j < length; j++){
                    JsonObject field_info = object_fields.getJsonObject(j);
                    if(cType.isPrimitive()){
                        if(cType.equals(int.class)){
                            Array.set(object_instance, j, Integer.valueOf(field_info.getString("value")));

                        }else{
                            Array.set(object_instance, j, Double.valueOf(field_info.getString("value")));

                        }
                    }
                    else{
                        if(field_info.get("reference").equals("null")){
                            Array.set(object_instance, j, null);
                        }
                        else{
                            Array.set(object_instance, j, object_tracking_map.get(field_info.getString("reference")));
                        }
                    }
                }

            }else{
                JsonArray object_fields = object_info.getJsonArray("fields");

                for(int j = 0; j < object_fields.size(); j++){
                    JsonObject field_info = object_fields.getJsonObject(j);
                    String field_declaring_name = field_info.getString("declaring class");
                    Class field_declaring_class = Class.forName(field_declaring_name);
                    String field_name = field_info.getString("name");
                    Field f = field_declaring_class.getDeclaredField(field_name);

                    if(!Modifier.isStatic(f.getModifiers())){
                        f.setAccessible(true);
                    }

                    if(f.getType().equals(int.class)){
                        f.set(object_instance, Integer.valueOf(field_info.getString("value")));

                    }
                    else if (f.getType().equals(double.class)){
                        f.set(object_instance, Double.valueOf(field_info.getString("value")));
                    }
                    else{
                        f.set(object_instance,object_tracking_map.get(field_info.getString("reference")));
                    }
                }
            }
        }
    }
}
