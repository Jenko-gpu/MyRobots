package org.jenko.gui;

import org.jenko.gui.mylocale.Localer;

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

    Localer localer;
    public final String FrameName = "GameStates";
    public GameStatesWindow(RobotModel robotModel)
    {

        super("Информация об игре", false, false, false, false);
        localer = Localer.getLocaler();

        this.setTitle(localer.getVal("Stat.Title"));

        robotModel.addListener(this);
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

        displayPos = new JLabel(localer.getVal("Stat.StartPos"));
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
        // Комментарий к задаче: Вообще не вижу смысла сохранять для каждого состояния строки.
        // Ибо количество строк будет равно квадрату от размера поля.
        RobotModel robot = (RobotModel) evt.getNewValue();
        StringBuilder posBuilder = new StringBuilder();
        posBuilder.append(localer.getVal("Stat.PosMessage"))
                .append((int) robot.getPositionX())
                .append(":")
                .append((int) robot.getPositionY());
        displayPos.setText(posBuilder.toString());

        StringBuilder angleBuilder = new StringBuilder();
        angleBuilder.append(localer.getVal("Stat.AngleMessage"))
                .append(robot.getDirection());
        displayAngle.setText(angleBuilder.toString());
    }
}
