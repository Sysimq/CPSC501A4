import org.example.Serializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.example.Object1;

import javax.json.JsonObject;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializerDeserializerTest {
    public static Object1 obj;

    public static String jsonStr;
    private static final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private static final PrintStream printStream = System.out;

    @BeforeAll
    public static void setUp() throws Exception
    {
        obj = new Object1();
        obj.x = 1;
        obj.y = 1.3;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterAll
    public static void tearDown()
    {
        System.setOut(printStream);
    }

    @Test
    @Order(1)
    void testSerializeObject() throws Exception
    {
        //{"objects":[{"class":"org.example.ObjectA","id":"0","type":"object","fields":[{"name":"x","declaring class":"org.example.ObjectA","type":"int","value":"1"},{"name":"y","declaring class":"org.example.ObjectA","type":"double","value":"1.3"},{"name":"a_a","declaring class":"org.example.ObjectA","reference":"null"}]}]}
        JsonObject jsonObj = Serializer.serializeObject(obj);
        jsonStr = jsonObj.toString();
        String expected = "{\"objects\":[{\"class\":\"org.example.Object1\",\"id\":\"0\",\"type\":\"object\",\"fields\":[{\"name\":\"x\",\"declaring class\":\"org.example.Object1\",\"type\":\"int\",\"value\":\"1\"},{\"name\":\"y\",\"declaring class\":\"org.example.Object1\",\"type\":\"double\",\"value\":\"1.3\"},{\"name\":\"z\",\"declaring class\":\"org.example.Object1\",\"reference\":\"null\"}]}]}";
        assertEquals(expected, jsonStr);
    }
}
