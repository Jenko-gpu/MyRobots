package org.jenko.gui;

import java.awt.*;
import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import org.jenko.log.LogChangeListener;
import org.jenko.log.LogEntry;
import org.jenko.log.LogWindowSource;

public class LogWindow extends JInternalFrame implements LogChangeListener, SaveLoadableWindow
{
    private LogWindowSource m_logSource;
    private TextArea m_logContent;

    public final String FrameName = "LogWindow";

    public LogWindow(LogWindowSource logSource)
    {
        super("Протокол работы", true, true, true, true);

        WindowSaveLoader.getInstance().connect(this, this.FrameName);
        WindowData windowData = WindowSaveLoader.getInstance().loadWindowStates(FrameName);

        Dimension dimension = new Dimension(200,400);

        if (windowData == null) {

            this.setLocation(10,10);
            this.setSize(dimension);
            setMinimumSize(dimension);
        }
        else{
            this.setLocation(windowData.pos_x,windowData.pos_y);
            this.setSize(windowData.width,windowData.height);
            try {
                this.setIcon(windowData.is_hidden);
            } catch (PropertyVetoException e) {
                System.err.println("Ошибка при восстановлении hidden окна " + FrameName);
                e.printStackTrace();
            }
        }
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);
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
        WindowData data = GetStateForComponent.get(this);
        data.is_hidden = this.isIcon;
        return data;
    }


}
