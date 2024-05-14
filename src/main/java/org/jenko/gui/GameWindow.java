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



    public GameWindow(RobotModel robotModel)
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
        RobotsController robotsController = new RobotsController(robotModel);

        m_visualizer = new GameVisualizer(robotModel);


        robotModel.addListener(m_visualizer);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer);
        getContentPane().add(panel);

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                Point point = e.getPoint();
                point.setLocation(point.x - 6, point.y - 25); // Нормализация точки (вычитаю примерные размеры границ)
                robotsController.setTarget(point);

            }
        });

    }

    @Override
    public WindowData Save() {
        return UtilForComponent.getStateForComponent(this);
    }


}
