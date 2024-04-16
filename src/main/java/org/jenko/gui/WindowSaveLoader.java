package org.jenko.gui;

import java.util.HashMap;
import java.util.Map;

/**
 * Синглтон для сохранения и загрузки данных окон.
 */
public final class WindowSaveLoader {
    private static WindowSaveLoader uniqInstance;

    /**
     * Загруженные данные окон
     */
    private Map<String,WindowData> data;


    /**
     * Окна, что нужно сохранить
     */
    private final Map<String, SaveLoadableWindow> frames = new HashMap<String, SaveLoadableWindow>();


    private final FileGetter fileGetter = new FileGetter();
    private WindowSaveLoader() {
        data = fileGetter.get();
    }
    public static WindowSaveLoader getInstance() {
        if (uniqInstance == null){
            uniqInstance = new WindowSaveLoader();

        }
        return uniqInstance;
    }

    /**
     * Загрузить состояния конкретного окна
     * @return WindowData, если есть, иначе null
     */
    public WindowData loadWindowStates(String window_name)
    {
        return data.get(window_name);

    }

    /**
     * Соединить окно для последующих сохранений
     */
    public void connect(SaveLoadableWindow frame, String name){
        frames.put(name, frame);
        //Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Сохранить состояния окон и закрыть приложение
     */
    public void closingWindows(){
        for (Map.Entry<String, SaveLoadableWindow> pair : frames.entrySet()){
            WindowData windowData = pair.getValue().Save();
            data.put(pair.getKey(), windowData);
        }
        fileGetter.send(data);
        System.exit(0);
    }

}
