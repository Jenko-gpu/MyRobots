package org.jenko.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import javax.swing.*;

public class GameWindow extends JInternalFrame implements SaveLoadableWindow {
    private final GameVisualizer m_visualizer;
    private final String FrameName = "GameWindow";



    public GameWindow(PropertyChangeListener stateWindow)
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
                System.err.println("Ошибка при восстановлении hidden окна " + FrameName);
                e.printStackTrace();
                System.err.println("Ошибка установки состояния iconified для " + this.FrameName);
            }
        }
        RobotModel robotModel = new RobotModel();
        RobotsController robotsController = new RobotsController(robotModel);
        m_visualizer = new GameVisualizer();
        robotsController.addListener(m_visualizer);
        robotsController.addListener(stateWindow);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer);
        getContentPane().add(panel);

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                Point point = e.getPoint();
                point.setLocation(point.x - 5, point.y - 25); // Нормализация точки (вычитаю размеры границ)
                robotsController.setTarget(point);
                repaint();
            }
        });

    }

    @Override
    public WindowData Save() {
        return UtilForComponent.getStateForComponent(this);
    }


}
