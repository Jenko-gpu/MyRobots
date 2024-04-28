package org.jenko.gui;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;

/**
 * Окно показывающее координаты робота
 */
public class GameStatesWindow extends JInternalFrame implements PropertyChangeListener, SaveLoadableWindow {
    private final JLabel displayPos;
    private final JLabel displayAngle;

    public final String FrameName = "GameStates";
    public GameStatesWindow()
    {
        super("Информация об игре", false, false, false, false);

        WindowSaveLoader.getInstance().connect(this,FrameName);
        WindowData windowData = WindowSaveLoader.getInstance().loadWindowState(FrameName);
        this.setSize(250, 70);
        if (windowData == null) {
            this.setLocation(50, 50);
        } else {
            try {
                UtilForComponent.setStatesForComponent(this, windowData);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
                System.err.println("Ошибка при попытке восстановить iconified " + FrameName);
            }
        }
        setLayout(new BorderLayout());

        displayPos = new JLabel("Робот на начальной координате");
        displayAngle = new JLabel("");
        add(displayPos, BorderLayout.NORTH);
        add(displayAngle, BorderLayout.SOUTH);
        setVisible(true);



    }





    @Override
    public WindowData Save() {
        WindowData windowData = UtilForComponent.getStateForComponent(this);
        return windowData;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        RobotModel robot = (RobotModel) evt.getNewValue();
        displayPos.setText("Робот на координате "+ (int) robot.m_PositionX +":"+ (int) robot.m_PositionY);
        displayAngle.setText("Угол робота: "+ robot.m_Direction);
    }
}
