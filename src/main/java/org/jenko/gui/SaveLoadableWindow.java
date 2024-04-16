package org.jenko.gui;


/**
 * Окна, поддерживающие загрузку и выгрузку состояния
 */
public interface SaveLoadableWindow {
    /**
     * Обернуть данные окна в WindowData, для дальнейшего сохранения
     *
     */
    WindowData Save();


}
