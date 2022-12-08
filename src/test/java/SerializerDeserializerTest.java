import org.example.Deserializer;
import org.example.Inspector;
import org.example.Serializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.example.Object1;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

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
        JsonObject jsonObj = Serializer.serializeObject(obj);
        jsonStr = jsonObj.toString();
        String expected = "{\"objects\":[{\"class\":\"org.example.Object1\",\"id\":\"0\",\"type\":\"object\",\"fields\":[{\"name\":\"x\",\"declaring class\":\"org.example.Object1\",\"type\":\"int\",\"value\":\"1\"},{\"name\":\"y\",\"declaring class\":\"org.example.Object1\",\"type\":\"double\",\"value\":\"1.3\"},{\"name\":\"z\",\"declaring class\":\"org.example.Object1\",\"reference\":\"null\"}]}]}";
        assertEquals(expected, jsonStr);
    }
    @Test
    @Order(2)
    void testDeserializeObject() throws Exception
    {
        JsonReader reader = Json.createReader(new StringReader(jsonStr));
        JsonObject jObj = reader.readObject();
        reader.close();

        Object o = Deserializer.deserializeObject(jObj);
        Inspector ins = new Inspector();
        ins.inspect(o,true);
        String[] outArr = outputStream.toString().split("\n");

        assertEquals("Class: org.example.Object1", outArr[1].trim());
        assertEquals("Field: public int org.example.Object1.x", outArr[2].trim());
        assertEquals("Value: 1", outArr[3].trim());
        assertEquals("Field: public double org.example.Object1.y", outArr[4].trim());
        assertEquals("Value: 1.3", outArr[5].trim());
        assertEquals("Field: public org.example.Object1 org.example.Object1.z", outArr[6].trim());
        assertEquals("Value: null", outArr[7].trim());
    }
}
