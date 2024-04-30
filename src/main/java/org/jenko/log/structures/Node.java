package org.jenko.log.structures;

import org.jenko.log.LogEntry;

class Node {
    LogEntry el;
    Node next;
    Node(LogEntry t){
        el = new LogEntry(t);
    }
}
