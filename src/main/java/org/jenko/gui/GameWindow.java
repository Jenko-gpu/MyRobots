package org.jenko.gui;

import java.awt.*;
import java.beans.PropertyVetoException;
import javax.swing.*;

public class GameWindow extends JInternalFrame implements SaveLoadableWindow {
    private final GameVisualizer m_visualizer;
    private final String FrameName = "GameWindow";



    public GameWindow(GameStateObserver observer)
    {
        super("Игровое поле", true, true, true, true);
        WindowSaveLoader.getInstance().connect(this, this.FrameName);
        WindowData windowData = WindowSaveLoader.getInstance().loadWindowStates(FrameName);
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
                e.printStackTrace();
            }
        }

        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer);
        getContentPane().add(panel);

    }

    @Override
    public WindowData Save() {
        WindowData data = GetStateForComponent.get(this);
        data.is_hidden = this.isIcon;
        return data;
    }


}
