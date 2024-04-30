package org.jenko.log.structures;

import org.jenko.log.LogEntry;


/**
 *  Узел в списке LogHolder
 */
class Node {
    LogEntry el;
    Node next;
    Node(LogEntry t){
        el = new LogEntry(t);
    }
}
