package org.jenko.gui;


import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Timer;
import java.util.TimerTask;

public class RobotsController {
    public RobotModel robot;
    private RobotModel emptyRobot;

    private final PropertyChangeSupport support;
    private final Timer m_timer = initTimer();

    private static Timer initTimer()
    {
        return new Timer("events generator", true);
    }

    RobotsController(RobotModel robot){
        this.robot = robot;
        emptyRobot = new RobotModel();
        support = new PropertyChangeSupport(this);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                robot.UpdateRobot();
                notifyListeners();
            }
        }, 0, 10);

    }

    public void addListener(PropertyChangeListener pcl){
        support.addPropertyChangeListener(pcl);
    }

    /**
     *  Установить цель для модели робота
     *
     */
    public void setTarget(Point p){
        robot.setTargetPosition(p);
    }

    private void notifyListeners(){
        support.firePropertyChange("robot", emptyRobot, robot);
    }


}
