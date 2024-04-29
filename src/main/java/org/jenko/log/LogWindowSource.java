package org.jenko.log;

import org.jenko.log.structures.LogHolder;

import java.util.ArrayList;


/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 * 2. Этот класс хранит активные сообщения лога, но в такой реализации он 
 * их лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено 
 * величиной m_iQueueLength (т.е. реально нужна очередь сообщений 
 * ограниченного размера) 
 */
public class LogWindowSource
{
    private final int m_iQueueLength;
    
    private final LogHolder<LogEntry> m_messages;
    private final ArrayList<LogChangeListener> m_listeners;
    private volatile LogChangeListener[] m_activeListeners;
    
    public LogWindowSource(int iQueueLength) 
    {
        m_iQueueLength = iQueueLength;
        m_messages = new LogHolder<>(iQueueLength);
        m_listeners = new ArrayList<LogChangeListener>();
    }
    
    public void registerListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.add(listener);
            m_activeListeners = null;
        }
    }
    
    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {

            m_listeners.remove(listener);
            m_activeListeners = null;
        }
    }
    
    public void append(LogLevel logLevel, String strMessage)
    {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        m_messages.add(entry);
        LogChangeListener [] activeListeners;

        synchronized (m_listeners)
        {
            if (m_activeListeners == null)
            {
                activeListeners = m_listeners.toArray(new LogChangeListener [0]);
                m_activeListeners = activeListeners;
            } else{
                activeListeners = m_activeListeners;
            }
        }
        for (LogChangeListener listener : activeListeners)
        {
            listener.onLogChanged();
        }
    }
    
    public int size()
    {
        return m_messages.getCurLen();
    }

    public Iterable<LogEntry> range(int startFrom, int count)
    {
        if (startFrom < 0 || startFrom >= m_messages.getCurLen())
        {
            return new LogHolder<LogEntry>(m_iQueueLength);
        }
        int indexTo = Math.min(startFrom + count, m_messages.getCurLen());
        return m_messages.getSlice(startFrom, indexTo);
    }

    public Iterable<LogEntry> all()
    {
        return m_messages;
    }


}
