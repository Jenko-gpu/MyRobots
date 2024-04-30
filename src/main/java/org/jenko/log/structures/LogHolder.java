package org.jenko.log.structures;

import org.jenko.log.LogEntry;

import java.util.Iterator;
import java.util.List;

/**
 *  Структура данных основанная на списке. <br>
 *  Имеет максимальный размер. Самоочищаемая (удаляются самые старые) <br>
 *
 */
public class LogHolder implements Iterable<LogEntry> {
    final Object lock = new Object(); // Для самого холдера, так и для его итератора

    Node iterHead;

    Node head; // конкретно для пакета
    private Node tail;

    private final int maxLen;

    private int curLen;

    public LogHolder(int n){
        maxLen = n;
        curLen = 0;
    }

    /**
     * Добавить элемент в коллекцию
     *
     */
    public void add(LogEntry el){
        synchronized (lock) {
            if (iterHead != head || head == null) { // Если не догнали итератор или ...
                if (tail == null) {
                    tail = new Node(el);
                    head = tail;
                } else {
                    tail.next = new Node(el);
                    tail = tail.next;
                }
                curLen = Math.min(curLen + 1, maxLen + 1);
                if (curLen > maxLen) {
                    curLen = maxLen;
                    Node next = head.next;
                    head.next = null;
                    head = next;
                }
                return;
            }

        }
        add(el); // Попытаться позже добавить элемент
    }

    /**
     *  Получить смежные данные от start включительно до finish невключительно
     *
     *
     */
    public LogHolder getSlice(int start, int finish) {
        if (start >= finish) {
            throw new IllegalArgumentException();
        }
        if (finish > curLen) {
            throw new IndexOutOfBoundsException();
        }
        synchronized (lock) {
            int n = 0;
            Node cur = head;
            LogHolder ans = new LogHolder(finish - start);
            while (n < start) {
                cur = cur.next;
                n++;
            }

            while (n < finish) {
                ans.add(cur.el);
                cur = cur.next;
                n++;
            }
            return ans;
        }
    }

    /**
     * Получить текущий размер
     *
     */
    public int size(){
        return curLen;
    }

    /**
     * Получить максимальный размер
     *
     */
    public int getMaxLen(){
        return maxLen;
    }

    @Override
    public Iterator<LogEntry> iterator() {
        try {
            return new LogHolderIterator(this);
        } catch (HasAlreadyOpennedIterator e) {
           e.printStackTrace();
           System.err.println(e.getMessage());
        }
        return new ArrayList<LogEntry>().iterator();
    }
}
