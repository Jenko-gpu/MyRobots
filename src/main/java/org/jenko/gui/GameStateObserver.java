package org.jenko.gui;

/**
 * Наблюдатель за координатами робота
 */
public interface GameStateObserver {
     /**
      * Оповестить о новых данных робота
      */
     <T,R,M extends Number> void gameStateHasChanged(T x, R y, M angle);

}
