package org.jenko.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;


import org.codehaus.jackson.type.TypeReference;


import javax.imageio.IIOException;

public class FileGetter implements DataGetter {
    @Override
    public Map<String, WindowData> get() {
        try {

            BufferedReader reader = new BufferedReader(new FileReader("C:\\RobotsConfig\\config.json"));
            String raw_data = reader.readLine();
            reader.close();

            ObjectMapper mapper = new ObjectMapper();

            TypeReference<Map<String,WindowData>> typeRef = new TypeReference<>() {
            };

            return mapper.readValue(raw_data, typeRef);
            /*
            Map<String, MyData> map = new HashMap<String, MyData>();
            for (MyData el: mydata){
                map.put(el.name,el);
            }
            return map;
            */

        }catch (Exception e){
            e.printStackTrace();
        }
        return new HashMap<String, WindowData>();
    }

    @Override
    public void send(Map<String,WindowData> data) {
        ObjectMapper mapper = new ObjectMapper().configure( SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
        try {
            mapper.writeValue(new File("C:\\RobotsConfig\\config.json"), data);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}