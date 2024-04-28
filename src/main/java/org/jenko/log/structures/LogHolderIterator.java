package org.jenko.log.structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LogHolderIterator<T> implements Iterator<T> {



    private LogHolder<T> logHolder;
    private Node<T> curNode;

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
    public T next() throws NoSuchElementException {
        if (curNode == null){
            throw new NoSuchElementException("Нет элементов больше в Holder");
        }

        synchronized (lock){
            T el = curNode.el;
            curNode = curNode.next;
            logHolder.iterHead = curNode;
            return el;
        }
    }



    LogHolderIterator(LogHolder<T> holder) throws HasAlreadyOpennedIterator {

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
