package org.jenko.gui;


import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Timer;
import java.util.TimerTask;

public class RobotsController {
    public RobotModel robot;



    private final Timer m_timer = new Timer("events generator", true);


    RobotsController(RobotModel robot){
        this.robot = robot;
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                robot.UpdateRobot();
            }
        }, 0, 10);

    }




    /**
     *  Установить цель для модели робота
     *
     */
    public void setTarget(Point p){
        robot.setTargetPosition(p);
    }



}
