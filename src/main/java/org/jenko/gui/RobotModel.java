package org.jenko.gui;

import java.awt.*;

/**
 * Модель робота, отвечающая за его движение.
 *
 */
public class RobotModel {

    volatile public double m_PositionX = 100;
    volatile public double m_PositionY = 100;
    volatile public double m_Direction = 0;

    volatile public int m_targetPositionX = 150;
    volatile public int m_targetPositionY = 100;

    static public final double maxVelocity = 0.1;
    static public final double maxAngularVelocity = 0.001;


    RobotModel() {
    }




    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }


    /**
     * Выполнить задачу робота
     */
    public void UpdateRobot()
    {
        double distance = distance( m_targetPositionX,  m_targetPositionY,
                 m_PositionX,  m_PositionY);
        if (distance < 0.5)
        {
            return;
        }
        double velocity =  maxVelocity;
        double angleToTarget = angleTo( m_PositionX,  m_PositionY,  m_targetPositionX,  m_targetPositionY);
        double angularVelocity = 0;
        double angleDiff = Math.abs(angleToTarget -  m_Direction);
        if (angleDiff < Math.PI){
            if (angleToTarget >  m_Direction)
            {
                angularVelocity =  maxAngularVelocity;
            }
            if (angleToTarget <  m_Direction)
            {
                angularVelocity = -maxAngularVelocity;
            }
        } else{
            if (angleToTarget >  m_Direction)
            {
                angularVelocity = -maxAngularVelocity;
            }
            if (angleToTarget <  m_Direction)
            {
                angularVelocity =  maxAngularVelocity;
            }
        }

        moveRobot(velocity, angularVelocity, 10);
    }

    /**
     * Передвинуть робота
     */
    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        velocity = applyLimits(velocity, 0,  maxVelocity);
        angularVelocity = applyLimits(angularVelocity, - maxAngularVelocity,  maxAngularVelocity);
        double newX =  m_PositionX + velocity / angularVelocity *
                (Math.sin( m_Direction + angularVelocity * duration) -
                        Math.sin( m_Direction));
        if (!Double.isFinite(newX))
        {
            newX =  m_PositionX + velocity * duration * Math.cos( m_Direction);
        }
        double newY =  m_PositionY - velocity / angularVelocity *
                (Math.cos( m_Direction + angularVelocity * duration) -
                        Math.cos( m_Direction));
        if (!Double.isFinite(newY))
        {
            newY =  m_PositionY + velocity * duration * Math.sin(m_Direction);
        }
        m_PositionX = newX;
        m_PositionY = newY;
        double newDirection = asNormalizedRadians(m_Direction + angularVelocity * duration);
        m_Direction = newDirection;
    }

    private static double asNormalizedRadians(double angle)
    {
        while (angle < 0)
        {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI)
        {
            angle -= 2*Math.PI;
        }
        return angle;
    }

    protected void setTargetPosition(Point p)
    {

        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }

}