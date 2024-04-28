package org.jenko.log.structures;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *  Структура данных основанная на списке. <br>
 *  Имеет максимальный размер. Самоочищаемая (удаляются самые старые) <br>
 *  Поддерживает возможность добавлять элементы во время итерирования.
 *
 * @param <T>
 */
public class LogHolder<T> implements Iterable<T> {
    final Object lock = new Object(); // Для самого холдера, так и для его итератора

    Node<T> iterHead;

    Node<T> head; // конкретно для пакета
    private Node<T> tail;

    private final int maxLen;

    private int curLen;

    public LogHolder(int n){
        maxLen = n;
        curLen = 0;
    }

    public void add(T el){
        synchronized (lock) {
            if (iterHead != head || head == null) { // Если не догнали итератор или ...
                if (tail == null) {
                    tail = new Node<T>(el);
                    head = tail;
                } else {
                    tail.next = new Node<T>(el);
                    tail = tail.next;
                }
                curLen = Math.min(curLen + 1, maxLen + 1);
                if (curLen > maxLen) {
                    curLen = maxLen;
                    Node<T> next = head.next;
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
    public LogHolder<T> getSlice(int start, int finish) {
        if (start >= finish) {
            throw new IllegalArgumentException();
        }
        if (finish > curLen) {
            throw new IndexOutOfBoundsException();
        }
        synchronized (lock) {
            int n = 0;
            Node<T> cur = head;
            LogHolder<T> ans = new LogHolder<>(finish - start);
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


    public int getCurLen(){
        return curLen;
    }

    public int getMaxLen(){
        return maxLen;
    }

    @Override
    public Iterator<T> iterator() {
        try {
            return new LogHolderIterator<T>(this);
        } catch (HasAlreadyOpennedIterator e) {
           e.printStackTrace();
           System.err.println(e.getMessage());
        }
        return new ArrayList<T>().iterator();
    }
}
