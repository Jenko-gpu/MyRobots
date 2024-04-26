package org.jenko.gui;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для сохранения и загрузки данных окон
 */
public final class WindowSaveLoader {
    private static final WindowSaveLoader instance = new WindowSaveLoader();

    /**
     * Загруженные данные окон
     */
    private final Map<String,WindowData> data;


    /**
     * Окна, что нужно сохранить
     */
    private final Map<String, SaveLoadableWindow> frames = new HashMap<String, SaveLoadableWindow>();


    private final FileGetter fileGetter = new FileGetter();
    private WindowSaveLoader() {
        data = fileGetter.get();
    }
    public static WindowSaveLoader getInstance() {
        return instance;
    }

    /**
     * Загрузить состояния конкретного окна
     * @return WindowData, если есть, иначе null
     */
    public WindowData loadWindowState(String window_name)
    {
        return data.get(window_name);

    }

    /**
     * Зарегистрировать окно в классе
     */
    public void connect(SaveLoadableWindow frame, String name){
        frames.put(name, frame);
    }

    /**
     * Сохранить состояния окон
     */
    public void saveAllWidows(){
        for (Map.Entry<String, SaveLoadableWindow> pair : frames.entrySet()){
            WindowData windowData = pair.getValue().Save();
            data.put(pair.getKey(), windowData);
        }
        fileGetter.send(data);
    }

}
