package org.jenko.log;

import org.jenko.log.structures.LogHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
    
    private final LogHolder m_messages;
    private final ArrayList<WeakListener> m_listeners;
    
    public LogWindowSource(int iQueueLength) 
    {
        m_iQueueLength = iQueueLength;
        m_messages = new LogHolder(iQueueLength);
        m_listeners = new ArrayList<WeakListener>();
    }
    
    public void registerListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.add(new WeakListener(listener));
        }
    }
    
    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.remove(new WeakListener(listener));
        }
    }
    
    public void append(LogLevel logLevel, String strMessage)
    {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        m_messages.add(entry);
        List<WeakListener> activeListeners = new ArrayList<WeakListener>();

        synchronized (m_listeners)
        {
            for (WeakListener reference: m_listeners) {
                if (reference.get() != null) {
                    activeListeners.add(reference);
                } else {
                    m_listeners.remove(reference);
                }
            }
        }
        for (WeakListener reference : activeListeners)
        {
            LogChangeListener listener = reference.get();
            if (listener != null ) {
                listener.onLogChanged();
            }
        }
    }
    
    public int size(){
        return m_messages.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count)
    {
        if (startFrom < 0 || startFrom >= m_messages.size())
        {
            return new LogHolder(m_iQueueLength);
        }
        int indexTo = Math.min(startFrom + count, m_messages.size());
        return m_messages.subList(startFrom, indexTo);
    }

    public Iterable<LogEntry> all()
    {
        return m_messages;
    }


}
