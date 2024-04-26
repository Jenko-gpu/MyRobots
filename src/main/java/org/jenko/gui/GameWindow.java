package org.jenko.gui;

import java.awt.BorderLayout;
import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame implements SaveLoadableWindow {
    private final GameVisualizer m_visualizer;
    private final String FrameName = "GameWindow";

    public GameWindow()
    {
        super("Игровое поле", true, true, true, true);
        WindowSaveLoader.getInstance().connect(this, this.FrameName);
        WindowData windowData = WindowSaveLoader.getInstance().loadWindowState(FrameName);
        if (windowData == null){
            this.setSize(400, 500);
            this.setLocation(15, 15);
            setMinimumSize(this.getSize());
        } else{
            try {
                UtilForComponent.setStatesForComponent(this, windowData);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
                System.err.println("Ошибка установки состояния iconified для " + this.FrameName);
            }

        }

        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer);
        getContentPane().add(panel);

    }

    @Override
    public WindowData Save() {
        return UtilForComponent.getStateForComponent(this);
    }


}
