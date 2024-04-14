package org.jenko.gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;


import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JOptionPane;

import org.jenko.log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame implements SaveLoadWindow
{
    private final JDesktopPane desktopPane = new JDesktopPane();

    public final String FrameName = "MainFrame";

    public MainApplicationFrame() {

        //Make the big window be indented 50 pixels from each edge
        //of the screen. {"LogWindow":null,"MainFrame":{"pos_x":603,"pos_y":76,"is_hidden":false,"width":779,"height":800}}
        SingletonWindow.getInstance().ConnectToSingleton(this, this.FrameName);
        WindowData windowData = SingletonWindow.getInstance().loadData(this.FrameName);
        int inset = 50;
        this.setVisible(true);
        this.setMinimumSize(new Dimension(500,400));
        if (windowData == null){

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setBounds(inset, inset,
                screenSize.width/ - inset*2,
                screenSize.height/ - inset*2);
            this.setExtendedState(Frame.MAXIMIZED_BOTH);
        } else {
            this.setLocation(windowData.pos_x, windowData.pos_y);
            this.setSize(windowData.width,windowData.height);
            if (windowData.is_hidden)
            this.setState(Frame.ICONIFIED);
            else this.setState(Frame.NORMAL);
        }
        setContentPane(desktopPane);
        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        InitListeners();
        InitSubWindows();

    }


    private void InitListeners(){
        addWindowListener(new java.awt.event.WindowAdapter() {

            final Object[]  YES_NO_OPTION_RUS = {
                    "Да", "Нет"
            };
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int result  = JOptionPane.showOptionDialog(getParent(),
                        "Вы точно хотите закрыть приложение?", "Закрыть приложение?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, YES_NO_OPTION_RUS, YES_NO_OPTION_RUS[1]);
                if (result == JOptionPane.YES_OPTION){
                    System.out.println("Program is closing");
                    SingletonWindow.getInstance().ClosingWindows();
                }
            }
        });
    }

    private void InitSubWindows(){
        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        addWindow(gameWindow);

        GameStatesWindow gameStatesWindow = new GameStatesWindow();
        addWindow(gameStatesWindow);
        gameWindow.setObserver(gameStatesWindow);
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());

        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
//
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
//
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
//
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
//
//        return menuBar;
//    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Новая строка");
            });
            testMenu.add(addLogMessageItem);
        }


        JMenuItem quitItem = new JMenuItem("Выйти из приложения", KeyEvent.VK_X);
            quitItem.addActionListener((event) -> {
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                    new WindowEvent(this, WindowEvent.WINDOW_CLOSING)
            );
        });

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(quitItem);
        return menuBar;
    }

    private void setLookAndFeel(String className) {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }

    @Override
    public WindowData Save() {
        WindowData windowData = new WindowData();
        windowData.is_hidden = this.getState() == 1;
        windowData.pos_x = Math.max(this.getX(), 0);
        windowData.pos_y = Math.max(this.getY(), 0);
        windowData.width = Math.max(this.getWidth(), 0);
        windowData.height = Math.max(this.getHeight(), 0);
        return windowData;
    }

}
