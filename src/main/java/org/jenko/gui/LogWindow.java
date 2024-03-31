package org.jenko.gui;

import java.awt.*;
import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import org.jenko.log.LogChangeListener;
import org.jenko.log.LogEntry;
import org.jenko.log.LogWindowSource;

public class LogWindow extends JInternalFrame implements LogChangeListener, SaveLoadWindow
{
    private LogWindowSource m_logSource;
    private TextArea m_logContent;

    public final String FrameName = "LogWindow";

    public LogWindow(LogWindowSource logSource)
    {
        super("Протокол работы", true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");

        SingletonWindow.getInstance().ConnectToSingleton(this, this.FrameName);
        WindowData windowData = SingletonWindow.getInstance().loadData(FrameName);
        if (windowData == null) {
            this.setLocation(10,10);
            this.setSize(200, 500);
            this.pack();
            setMinimumSize(this.getSize());
            m_logContent.setSize(200, 500);
        }
        else{
            this.setLocation(windowData.pos_x,windowData.pos_y);
            this.setSize(windowData.width,windowData.height);
            try {
                this.setIcon(windowData.is_hidden);
            } catch (PropertyVetoException e) {
                throw new RuntimeException(e);
            }
        }
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        updateLogContent();
    }

    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }
    
    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
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
