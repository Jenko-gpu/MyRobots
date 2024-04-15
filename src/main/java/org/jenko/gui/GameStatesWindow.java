package org.jenko.gui;

import javax.swing.*;
import java.awt.*;

public class GameStatesWindow extends JInternalFrame implements GameStateObserver {
    private JLabel displayPos;
    private JLabel displayAngle;


    public GameStatesWindow()
    {
        super("Информация об игре", false, false, false, false);


        this.setSize(250, 70);
        this.setLocation(50, 50);

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
}
