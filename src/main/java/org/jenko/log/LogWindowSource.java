package org.jenko.log;

import org.jenko.log.structures.LogHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Источник сообщений лога для оконного интерфейса. <br/>
 * Обеспечивает хранение лог-сообщений ограниченной емкости и уведомление подписчиков об изменениях. <br/>
 * Использует слабые ссылки на слушателей для предотвращения утечек памяти.
 */
public class LogWindowSource
{
    private final int m_iQueueLength;
    
    private final LogHolder m_messages;
    private final ArrayList<WeakListener> m_listeners;
    private volatile List<WeakListener> m_ActiveListeners;

    /**
     * Создает новый источник лог-сообщений с указанной максимальной емкостью.
     * @param iQueueLength максимальное количество хранимых сообщений
     */
    public LogWindowSource(int iQueueLength) 
    {
        m_iQueueLength = iQueueLength;
        m_messages = new LogHolder(iQueueLength);
        m_listeners = new ArrayList<WeakListener>();
    }

    /**
     * Регистрирует слушателя изменений лога. <br/>
     * @param listener слушатель изменений лога
     */
    public void registerListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.add(new WeakListener(listener));
            m_ActiveListeners = null;
        }
    }

    /**
     * Удаляет слушателя из списка подписчиков.
     * @param listener слушатель для удаления
     */
    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.remove(new WeakListener(listener));
            m_ActiveListeners = null;
        }
    }

    /**
     * Добавляет новое сообщение в лог. <br/>
     * Уведомляет всех активных слушателей об изменении.
     * @param logLevel уровень логирования
     * @param strMessage текст сообщения
     */
    public void append(LogLevel logLevel, String strMessage)
    {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        m_messages.add(entry);
        List<WeakListener> activeListeners = m_ActiveListeners;

        // Double-checked
        if (m_ActiveListeners == null) {

            synchronized (m_listeners) {

                if (m_ActiveListeners == null) {
                    activeListeners = new ArrayList<>();

                    // Собираем "живых" слушателей, а "мёртвых" удаляем из списка слушателей
                    for (WeakListener reference : m_listeners) {
                        if (reference.get() != null) {
                            activeListeners.add(reference);
                        } else {
                            m_listeners.remove(reference);
                        }
                    }
                    m_ActiveListeners = activeListeners;
                }
            }
        }
        if (activeListeners != null) notifyLogChanged(activeListeners);
    }

    /**
     * Уведомляет всех слушателей об изменении лога.
     * @param listeners список слушателей для уведомления
     */
    private void notifyLogChanged(List<WeakListener> listeners){
        for (WeakListener reference : listeners)
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

    /**
     * Возвращает все сообщения лога.
     * @return итерируемая коллекция всех сообщений
     */
    public Iterable<LogEntry> all()
    {
        return m_messages;
    }


}
