package org.jenko.gui;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

/**
 * Различные функции для получения/установки данных окон
 */
public class UtilForComponent {
    private UtilForComponent(){


    }

    /**
     *  Установить для компоненты данные из windowData
     *
     *
     */
    public static void setStatesForComponent(Component frame, WindowData windowData) throws PropertyVetoException {
        frame.setLocation(windowData.pos_x, windowData.pos_y);
        frame.setSize(windowData.width,windowData.height);
        if (frame instanceof JFrame){
            ((JFrame) frame).setState(windowData.is_hidden ? JFrame.ICONIFIED : JFrame.NORMAL);
        }
        if (frame instanceof JInternalFrame){
            ((JInternalFrame) frame).setIcon(windowData.is_hidden);
        }
    }

    /**
     * Получить WindowData из компоненты
     *
     */
    public static WindowData getStateForComponent(Component frame){
        WindowData windowData = new WindowData();
        windowData.setPos_x(Math.max(frame.getX(), 0));
        windowData.setPos_y(Math.max(frame.getY(), 0));
        windowData.setWidth(Math.max(frame.getWidth(), 0));
        windowData.setHeight(Math.max(frame.getHeight(), 0));
        if (frame instanceof JFrame){
            windowData.setIs_hidden(((JFrame) frame).getState() == JFrame.ICONIFIED);
        }
        if (frame instanceof JInternalFrame){
            windowData.setIs_hidden(((JInternalFrame) frame).isIcon());
        }
        return windowData;
    }
}
