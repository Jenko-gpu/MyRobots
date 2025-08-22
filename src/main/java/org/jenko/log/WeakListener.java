package org.jenko.log;

import java.lang.ref.WeakReference;

/**
 * Реализация слабой ссылки для слушателей изменений лога. <br/>
 * Предназначен для использования в механизме уведомлений для предотвращения утечек памяти.
 */
public class WeakListener extends WeakReference<LogChangeListener>{

    /**
     * Сохранённый хэш код на случай, если слушатель удалится.
     */
    private final int cacheHashCode;

    public WeakListener(LogChangeListener listener){
        super(listener);
        cacheHashCode = listener.hashCode();
    }


    @Override
    public int hashCode() {
        return cacheHashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        LogChangeListener cur = get();
        LogChangeListener other = ((WeakListener) obj).get();

        return cur != null && cur.equals(other);
    }
}
