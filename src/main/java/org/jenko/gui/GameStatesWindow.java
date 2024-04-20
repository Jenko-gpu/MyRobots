package org.jenko.gui;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

/**
 * Окно показывающее координаты робота
 */
public class GameStatesWindow extends JInternalFrame implements GameStateObserver, SaveLoadableWindow {
    private JLabel displayPos;
    private JLabel displayAngle;

    public final String FrameName = "GameStates";
    public GameStatesWindow()
    {
        super("Информация об игре", false, false, false, false);

        WindowSaveLoader.getInstance().connect(this,FrameName);
        WindowData windowData = WindowSaveLoader.getInstance().loadWindowStates(FrameName);
        this.setSize(250, 70);
        if (windowData == null) {
            this.setLocation(50, 50);
        } else {
            this.setLocation(windowData.pos_x,windowData.pos_y);
        }
        setLayout(new BorderLayout());

        displayPos = new JLabel("Робот на начальной координате");
        displayAngle = new JLabel("");
        add(displayPos, BorderLayout.NORTH);
        add(displayAngle, BorderLayout.SOUTH);
        setVisible(true);



    }



    @Override
    public <R,T,M extends Number> void gameStateHasChanged(R x, T y, M angle) {

        displayPos.setText("Робот на координате "+ x +":"+ y);
        displayAngle.setText("Угол робота: "+ angle);
    }

    @Override
    public WindowData Save() {
        WindowData windowData = GetStateForComponent.get(this);
        return windowData;
    }
}
