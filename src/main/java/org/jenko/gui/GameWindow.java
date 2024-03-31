package org.jenko.gui;

import java.awt.BorderLayout;
import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame implements SaveLoadWindow {
    private final GameVisualizer m_visualizer;
    private final String FrameName = "GameWindow";

    public GameWindow()
    {
        super("Игровое поле", true, true, true, true);
        SingletonWindow.getInstance().ConnectToSingleton(this, this.FrameName);
        WindowData windowData = SingletonWindow.getInstance().loadData(FrameName);
        if (windowData == null){
            this.setSize(300, 400);
            this.setLocation(15, 15);
            pack();
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

    @Override
    public WindowData Save() {
        WindowData windowData = new WindowData();
        windowData.is_hidden = this.isIcon();
        windowData.pos_x = this.getX() > 0? this.getX() : 0;
        windowData.pos_y =this.getY() > 0? this.getY() : 0;
        windowData.width = this.getWidth() > 0? this.getWidth() : 0;
        windowData.height = this.getHeight() > 0 ? this.getHeight(): 0;
        return windowData;
    }


}
