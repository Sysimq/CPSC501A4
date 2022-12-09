package org.example;

import javax.json.JsonObject;
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String args[]) throws Exception{
        boolean result = true;
        int port = 8080;
        Socket socket = new Socket("localhost", port);
        String jsonFile = "SerializedObject.json";
        System.out.println("Welcome to client interface: ");
        while(result){
            System.out.println("Create objects to serialize and send them to the server");
            System.out.println("--------------------------------");
            Object obj = ObjectCreator.createSerializeObject();
            System.out.println("--------------------------------");
            FileWriter fw = new FileWriter(jsonFile);
            fw.write(ObjectCreator.prettyPrintString(obj));
            fw.close();
            JsonObject json_obj = Serializer.serializeObject(obj);

            OutputStream outputStream = socket.getOutputStream();

            ObjectOutputStream obj_outputStream = new ObjectOutputStream(outputStream);
            obj_outputStream.writeObject(((Object) json_obj).toString());
            obj_outputStream.flush();
            System.out.println("Serializing the object and sending it over to the server");
            System.out.println("--------------------------------");
        }
        socket.close();


    }
}
