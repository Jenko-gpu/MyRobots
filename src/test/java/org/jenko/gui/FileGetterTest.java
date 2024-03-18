package org.jenko.gui;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FileGetterTest {

    @Test
    void get() {
        FileGetter fileGetter = new FileGetter();
        Map<String, WindowData> map = fileGetter.get();
        for (WindowData el: map.values()){
            System.out.println(el.height);
        }
    }

    @Test
    void send() {
        WindowData data1 = new WindowData();
        WindowData data2 = new WindowData();

        data1.height = 1;
        data1.width = 1;
        data1.pos_x = 1;
        data1.pos_y = 1;
        data1.is_hidden = false;

        data2.height = 1;
        data2.width = 1;
        data2.pos_x = 1;
        data2.pos_y = 1;
        data2.is_hidden = false;

        HashMap<String,WindowData> map = new HashMap<String, WindowData>();
        map.put("data1",data1);
        map.put("data2",data2);

        FileGetter fileGetter = new FileGetter();

        fileGetter.send(map);
        assertEquals(0, 0);

    }
}