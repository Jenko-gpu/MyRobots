package org.jenko.gui;

public interface GameStateObserver {
     <T,R,M extends Number> void gameStateHasChanged(T x, R y, M angle);

}
