package org.jenko.gui;

import java.awt.*;

/**
 * Получение WindowData для Frame
 */
public class GetStateForComponent {
    GetStateForComponent(){


    }

    /**
     * Сохранение положения и размера компонента в WindowData
     *
     */
    public static WindowData get(Component frame){
        WindowData windowData = new WindowData();
        windowData.pos_x = Math.max(frame.getX(), 0);
        windowData.pos_y = Math.max(frame.getY(), 0);
        windowData.width = Math.max(frame.getWidth(), 0);
        windowData.height = Math.max(frame.getHeight(), 0);
        return windowData;
    }
}
