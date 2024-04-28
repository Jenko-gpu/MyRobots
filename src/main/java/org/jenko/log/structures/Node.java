package org.jenko.log.structures;

class Node<T> {
    T el;
    Node<T> next;
    Node(T t){
        el = t;
    }
}
