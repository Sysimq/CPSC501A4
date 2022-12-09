package org.example;

import java.lang.reflect.*;
import java.util.*;

public class Inspector {

    private HashMap<String,String> print;

    public void inspect(Object obj, boolean recursive){
        print = new HashMap<>();
        String ref = Integer.toHexString(obj.hashCode());
        print.put(ref,ref);
        inspect(obj, recursive, 0);
    }
    public void inspect(Object obj, boolean recursive, int depth){
        Class c = obj.getClass();
        inspectClass(c,obj,recursive,depth);
    }
    public void printTab ( int depth){
        String tab = new String(new char[depth]).replace('\0','\t');
        System.out.print(tab);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth){
        printTab(depth);
        System.out.println(String.format("Class: %s", c.getName()));

        inspectFields(c,obj,recursive,depth);
    }
    private void inspectFields(Class c, Object obj, boolean recursive, int depth){
        for(Field f: c.getDeclaredFields()){
            if(!Modifier.isStatic(f.getModifiers())){
                f.setAccessible(true);
                printFieldInfo(f, obj, recursive, depth);
            }
        }
    }
    private void printFieldInfo(Field f, Object obj, boolean recursive, int depth){
        printTab(depth);
        System.out.println(String.format("  Field: %s", f));

        Class fieldType = f.getType();
        f.setAccessible(true);

        Object fieldObj = null;

        try{
            fieldObj = f.get(obj);
        }catch(IllegalArgumentException | IllegalAccessException e){
            e.printStackTrace();
        }
        if(fieldObj == null){
            printTab(depth);
            System.out.println(String.format("  Value: null"));
        }
        else if (fieldType.isArray()){
            inspectArray(fieldType, fieldObj, recursive, depth);
        }
        else if(fieldType.isPrimitive()){
            printTab(depth);
            System.out.println(String.format("  Value: %s", fieldObj.toString()));
        }
        else{
            printTab(depth);
            System.out.println(String.format("  Value(reference): %s@%s", fieldObj.getClass().getName(),
                    Integer.toHexString(System.identityHashCode(fieldObj))));
            printTab(depth);
            System.out.println(("  Recursively Inspecting..."));
            inspectClass(fieldObj.getClass(), fieldObj, recursive, depth+1);

        }
    }
    private void inspectArray(Class c, Object obj, boolean recursive, int depth){
        Class cType = c.getComponentType();
        int array_length = Array.getLength(obj);
        printTab(depth);
        System.out.println(String.format("  Type: array"));
        printTab(depth);
        System.out.println(String.format("  Length: %s", Integer.toString(array_length)));

        if(array_length >  0){
            printTab(depth);
            System.out.println(String.format("  Entries:"));
        }

        for(int i = 0; i < array_length; i++){
            Object arrObj = Array.get(obj, i);

            if(arrObj == null){
                System.out.println(String.format("  Value:null"));
            }
            else if (cType.isArray()){
                printTab(depth);
                System.out.println(String.format("   Value(reference): %s", arrObj.toString()));
                inspectArray(arrObj.getClass(), arrObj, recursive, depth);
            }
            else if (cType.isPrimitive()){
                printTab(depth);
                System.out.println(String.format("   Value: %s",arrObj.toString()));
            }
            else {
                printTab(depth);
                System.out.println(String.format("  Value(reference): %s@%s", arrObj.getClass().getName(),
                        Integer.toHexString(System.identityHashCode(arrObj))));
                printTab(depth);
                System.out.println("  Recursively inspecting...");
                inspectClass(arrObj.getClass(), arrObj, recursive, depth +1 );
            }
        }



    }
}
