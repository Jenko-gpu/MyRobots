package org.jenko.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public final class SingletonWindow {
    private static SingletonWindow uniqInstance;
    private Map<String,WindowData> data = new HashMap<String, WindowData>();

    private Map<String, SaveLoadWindow> frames = new HashMap<String,SaveLoadWindow>();

    private FileGetter fileGetter = new FileGetter();
    private SingletonWindow() {

    }
    public static SingletonWindow getInstance() {
        if (uniqInstance == null){
            uniqInstance = new SingletonWindow();

        }
        return uniqInstance;
    }

    public WindowData loadData(String window_name)
    {
        if (data.size() == 0){
            data = fileGetter.get();
        }
        if (data.containsKey(window_name)){
            return data.get(window_name);
        }
        return null;

    }

    public void ConnectToSingleton(SaveLoadWindow frame, String name){
        frames.put(name, frame);
        //Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    public void ClosingWindows(){
        for (var pair : frames.entrySet()){
            WindowData windowData = pair.getValue().Save();
            data.put(pair.getKey(), windowData);
        }
        saveData();
        System.exit(0);
    }
    public void saveData()
    {
        fileGetter.send(data);
    }

}
