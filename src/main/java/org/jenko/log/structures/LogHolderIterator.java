package org.jenko.log.structures;

import org.jenko.log.LogEntry;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LogHolderIterator implements Iterator<LogEntry> {



    private LogHolder logHolder;
    private Node curNode;

    final Object lock;

    @Override
    public boolean hasNext() {
        synchronized (lock) {
        if (curNode == null) {
            logHolder.iterHead = null;
            return false;
        }
        return true;
        }
    }

    @Override
    public LogEntry next() throws NoSuchElementException {
        if (curNode == null){
            throw new NoSuchElementException("Нет элементов больше в Holder");
        }

        synchronized (lock){
            LogEntry el = curNode.el;
            curNode = curNode.next;
            logHolder.iterHead = curNode;
            return el;
        }
    }



    LogHolderIterator(LogHolder holder) throws HasAlreadyOpennedIterator {

        lock = holder.lock;

        synchronized (lock){
            if (holder.iterHead != null){
                throw new HasAlreadyOpennedIterator("Запрещено создавать более 1 итератора для одного экземпляра Holder");
            }
            holder.iterHead = holder.head;
        }
        curNode = holder.head;
        logHolder = holder;
    }

}
