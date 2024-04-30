package org.jenko.log.structures;

import org.jenko.log.LogEntry;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Итератор для LogHolder
 *
 */
public class LogHolderIterator implements Iterator<LogEntry> {



    private LogHolder logHolder;
    private Node curNode;

    @Override
    public boolean hasNext() {
        return curNode != null;
    }


    @Override
    public LogEntry next() throws NoSuchElementException {
        if (curNode == null){
            throw new NoSuchElementException("Нет элементов больше в Holder");
        }

        LogEntry el = curNode.el;
        curNode = curNode.next;
        return el;
    }



    LogHolderIterator(LogHolder holder){
        if (holder.size() == 0){
            curNode = null;
        }
        else {
            logHolder = holder.subList(0,holder.size());
            curNode = holder.getHead();
        }
    }

}
