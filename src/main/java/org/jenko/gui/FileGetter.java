package org.jenko.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;


import org.codehaus.jackson.type.TypeReference;
/**
 * Получение и сохранение данных окн в файл
 *
 */
public class FileGetter{

    private String curpath;
    private String filename;

    private File file;

    FileGetter(){
        curpath = System.getProperty("user.home") + "\\MyRobotsConf";
        filename = curpath + "\\robots.config";
        File dir = new File(curpath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        try {
            file = new File(filename);
            if (!file.exists()){
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Получить данные окон
     * @return
     */
    public Map<String, WindowData> get() {

        try (
                FileReader fileReader = new FileReader(filename);
                BufferedReader reader = new BufferedReader(fileReader)
        ) {
            String raw_data = reader.readLine();
            reader.close();
            if (raw_data == null){
                return new HashMap<String, WindowData>();
            }
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

    /**
     * Сохранить данные окон
     * @param data
     */
    public void send(Map<String,WindowData> data) {
        ObjectMapper mapper = new ObjectMapper().configure( SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
        try {
            mapper.writeValue(file, data);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}