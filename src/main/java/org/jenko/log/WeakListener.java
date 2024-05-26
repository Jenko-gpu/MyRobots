package org.jenko.log;

import java.lang.ref.WeakReference;

public class WeakListener {
    private WeakReference<LogChangeListener> listener;

    public WeakListener(LogChangeListener listener){
        this.listener = new WeakReference<LogChangeListener>(listener);
    }


    public LogChangeListener get(){
        return listener.get();
    }

    @Override
    public int hashCode() {
        LogChangeListener val = listener.get();
        if (val != null){
            return val.hashCode();
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        LogChangeListener cur = listener.get();
        LogChangeListener other = ((WeakListener) obj).get();

        return cur != null && other != null && cur.equals(other);
    }
}
