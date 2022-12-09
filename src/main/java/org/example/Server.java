package org.example;

import javax.json.Json;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.*;
import java.net.*;
import javax.json.*;

public class Server {

    public static void main(String args[]) throws Exception{
        System.out.println("Welcome to Server Interface: ");
        System.out.println("------------------------------");
        boolean result = true;

        try{
            ServerSocket serverSocket = new ServerSocket(8080);
            Socket socket = serverSocket.accept();
            while(result){
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream obj_inputStream = new ObjectInputStream(inputStream);
                System.out.println("--------------------------------");
                System.out.println("Object Received. Deserializing the object...");
                System.out.println("--------------------------------");
                Inspector ins = new Inspector();

                String jsonString = (String) obj_inputStream.readObject();
                JsonReader j_reader = Json.createReader(new StringReader(jsonString));
                JsonObject j_obj = j_reader.readObject();

                System.out.println("Printing the deserialized object...");
                System.out.println("------------------------------");
                Object obj = Deserializer.deserializeObject(j_obj);
                ins.inspect(obj,true);
            }
        }catch(IOException e){
            System.out.println("Cannot connect to the client. Exiting");
        }

    }
}
