package org.jenko.gui;

import java.util.Map;

public interface DataGetter{
    public Map<String, WindowData> get();
/*
*
*
 */
    public void send(Map<String, WindowData> data);
}
