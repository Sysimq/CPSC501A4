# CPSC501A4

## Description
Using reflection to serialize an object to JSON and then de-serialize from JSON back to an object

## Getting started

All source code can be found in `cpsc501a4/src/main/java/org/example` and `SerializedObject.json` file in `cpsc501a4`.\
Junit test can be found in `cpsc501a4/src/test/java/SerializerDeserializerTest.java`.

To run the program you can open the project on IntelliJ:
1. Load the project with IntelliJ and be sure to include pom.xml file. 
2. You have to run `Server.java` first followed by `Client.java` file. 
3. When these two are up and running, the user will be prompted on the Client terminal interface to choose from which objects you would like to create. Please input value from 1-6
4. Please follow given instructions and enter in only valid input to avoid Exceptions thrown.


For VM flag, add following commands to run configuration of both `Server.java` and `Client.java` files:\
```--add-opens java.base/java.util=ALL-UNNAMED```

## Refactorings

### Move Method
I moved getOption() method from ObjectCreator.java because it can be combined into the createObject() method.\
See commit: 5e0add6b0341bfe3ad31bc2987ddf00e103ba19b

### Rename Method
The next refactoring I performed dealth with the fact that some of the method names are not informative.I renamed sendObject() method to createSerializeObject().\
See commit: 5e0add6b0341bfe3ad31bc2987ddf00e103ba19b


