package org.jenko.gui;

import java.awt.*;
import java.beans.PropertyVetoException;
import javax.swing.*;

public class GameWindow extends JInternalFrame implements SaveLoadWindow {
    private final GameVisualizer m_visualizer;
    private final String FrameName = "GameWindow";



    public GameWindow()
    {
        super("Игровое поле", true, true, true, true);
        SingletonWindow.getInstance().ConnectToSingleton(this, this.FrameName);
        WindowData windowData = SingletonWindow.getInstance().loadData(FrameName);
        if (windowData == null){
            this.setSize(400, 500);
            this.setLocation(15, 15);
            setMinimumSize(this.getSize());
        } else{
            this.setLocation(windowData.pos_x, windowData.pos_y);
            this.setSize(windowData.width, windowData.height);
            try {
                this.setIcon(windowData.is_hidden);
            } catch (PropertyVetoException e) {
                throw new RuntimeException(e);
            }
        }

        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer);
        getContentPane().add(panel);


    }

    public void setObserver(GameStateObserver observer){
        m_visualizer.observer = observer;
    }
    @Override
    public WindowData Save() {
        WindowData windowData = new WindowData();
        windowData.is_hidden = this.isIcon();
        windowData.pos_x = Math.max(this.getX(), 0);
        windowData.pos_y = Math.max(this.getY(), 0);
        windowData.width = Math.max(this.getWidth(), 0);
        windowData.height = Math.max(this.getHeight(), 0);
        return windowData;
    }


}
