package org.example;
import java.io.*;
import java.util.*;
import javax.json.*;
import javax.json.stream.JsonGenerator;

public class ObjectCreator {
    public static void printMenu(){
        System.out.println("Choose from the following options");
        System.out.println(" 1: Create Object 1 ");
        System.out.println(" 2: Create Object 2 ");
        System.out.println(" 3: Create Object 3 ");
        System.out.println(" 4: Create Object 4 ");
        System.out.println(" 5: Create Object 5 ");
        System.out.println(" 6: Exit ");

    }
    public static int getOption(){
        Scanner scan = new Scanner(System.in);
        while(true){
            printMenu();
            String input = scan.nextLine();
            try{
                int option = Integer.parseInt(input);
                switch(option){
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        return option;
                    default:
                        System.out.println("Enter a number from 1-6");
                }

            }catch (NumberFormatException e){
                System.out.println("Invalid input. Please enter a number from 1-6");
            }
        }
    }
    private static Object createObject(int input){
        Object object = null;
        switch (input){
            case 1:
                object = createObject1();
                break;
            case 2:
                object = createObject2();
                break;
            case 3:
                object = createObject3();
                break;
            case 4:
                object = createObject4();
                break;
            case 5:
                object = createObject5();
                break;
            case 6:
                System.out.println("Exiting program...");
                System.exit(0);
                break;
        }
        return object;
    }

    private static Object createObject1(){
        System.out.println("Creating ObjectA, Please enter an int value and a double value ");
        System.out.println("If you do not enter the values, They will be automatically set to 0");
        Object1 obj1 = new Object1();
        Scanner scan = new Scanner(System.in);
        int i = 0;
        double d = 0.0;
        while(true){
            System.out.println("Int value: ");
            String input = scan.nextLine();
            try{
                i = Integer.parseInt(input);
                break;
            }catch (NumberFormatException e){
                System.out.println("Invalid integer value.");
            }
        }
        while(true){
            System.out.println("Double value: ");
            String input = scan.nextLine();
            try{
                d = Double.parseDouble(input);
                break;
            }catch (NumberFormatException e){
                System.out.println("Invalid double value.");
            }
        }
        obj1.x = i;
        obj1.y = d;
        return obj1;
    }

    private static Object createObject2(){
        System.out.println("Demonstrating an object that contains references to other objects");
        Object2 obj2 = new Object2();
        obj2.obj = (Object1) createObject1();
        Object1 obj1 = new Object1();
        obj1.z = obj2.obj;
        return obj1;
    }

    private static Object createObject3(){

        Scanner scan = new Scanner(System.in);

        System.out.print("Define a length of the array: ");
        int length = Integer.parseInt(scan.nextLine());
        int [] arr = new int[length];

        System.out.println("Enter the values to put in array: \n ");
        for(int i = 0; i < length; i++){
            System.out.print("["+ i +"]: ");
            try{
                arr[i] = Integer.parseInt(scan.nextLine());
            }
            catch (NumberFormatException e){
                System.out.println("Invalid int value. Value will be automatically set to 0");
            }

        }
        Object3 obj3 = new Object3(arr);
        return obj3;
    }
    private static Object createObject4(){
        System.out.println("Creating a reference array. Please define a length of the array");
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        int length = Integer.parseInt(input);
        Object[] arr = new Object[length];
        for (int i = 0; i < length; i++){
            System.out.println("Enter either 'null' for no object or 'obj' for a reference object");
            String option = scan.nextLine();

            if(option.equalsIgnoreCase("obj")){
                System.out.println("Creating an object at index ["+i+"]");
                arr[i] = (Object1) createObject1();
            }else if (option.equalsIgnoreCase("null")){
                System.out.println("Inserting null at index ["+i+"]");
                arr[i] = null;
            }
            else{
                System.out.println("Invalid option. Inserting null value automatically ");
            }
        }
        Object4 obj4= new Object4(arr);
        return obj4;
    }
    private static Object createObject5(){
        System.out.println("Creating an ArrayList reference, please define a length of the array");
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        int length = Integer.parseInt(input);
        ArrayList<Object> arrayList =  new ArrayList<>(length);
        for(int i = 0; i < length; i++){
            System.out.println("Enter either 'null' or 'obj' for a reference object");
            String option = scan.nextLine();

            if(option.equalsIgnoreCase("obj")){
                System.out.println("Creating an object at index ["+i+"]");
                arrayList.add( (Object1) createObject1() );
            }
            else if (option.equalsIgnoreCase("null")){
                arrayList.add(null);
            }
            else{
                System.out.println("Invalid input. Inserting null value automatically");
            }
        }
        Object5 obj5 = new Object5(arrayList);
        return obj5;
    }

    public static Object sendObject(){
        Object obj = createObject(getOption());
        return obj;
    }

    public static String prettyPrintString(Object obj) throws Exception{
        JsonObject json_obj = Serializer.serializeObject(obj);
        StringWriter sw = new StringWriter();
        Map<String, Object> map = new HashMap<>();
        map.put(JsonGenerator.PRETTY_PRINTING,true);
        JsonWriterFactory wf = Json.createWriterFactory(map);
        JsonWriter jw = wf.createWriter(sw);
        jw.writeObject(json_obj);
        jw.close();
        String prettyPrint = sw.toString();
        return prettyPrint;
    }

}

