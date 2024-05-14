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

import org.jenko.gui.mylocale.Localer;
import org.jenko.log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame implements SaveLoadableWindow
{
    private final JDesktopPane desktopPane = new JDesktopPane();

    public final String FrameName = "MainFrame";

    /**
     *
     */
    public Localer localer;

    //final Object[] YES_NO_OPTION = null;//{ "Да", "Нет" };

    public MainApplicationFrame() {

        //Make the big window be indented 50 pixels from each edge
        //of the screen. {"LogWindow":null,"MainFrame":{"pos_x":603,"pos_y":76,"is_hidden":false,"width":779,"height":800}}
        WindowSaveLoader.getInstance().connect(this, this.FrameName);

        localer = Localer.getLocaler();

        WindowData windowData = WindowSaveLoader.getInstance().loadWindowState(this.FrameName);
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
            try {
                UtilForComponent.setStatesForComponent(this, windowData);
            }
            catch (Exception e){
                e.printStackTrace();
                System.err.println("Не предвиденная ошибка");
            }

        }
        setContentPane(desktopPane);
        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        InitListeners();
        InitSubWindows();

    }


    private void InitListeners(){
        addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Object[] YES_NO_OPTION = {localer.getVal("Yes"), localer.getVal("No")};

                int result  = JOptionPane.showOptionDialog(getParent(),
                        localer.getVal("AskCloseApplication"), localer.getVal("CloseApplication") + "?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, YES_NO_OPTION, YES_NO_OPTION[1]);
                if (result == JOptionPane.YES_OPTION){
                    System.out.println("Program is closing");
                    WindowSaveLoader.getInstance().saveAllWidows();
                    System.exit(0);
                }
            }
        });
    }


    /**
     * Открытие внутренних окон.
     */
    private void InitSubWindows(){
        LogWindow logWindow =  new LogWindow(Logger.getDefaultLogSource());
        Logger.debug(localer.getVal("Log.Ok"));
        addWindow(logWindow);

        RobotModel robotModel = new RobotModel();

        GameStatesWindow gameStatesWindow = new GameStatesWindow(robotModel);
        addWindow(gameStatesWindow);

        GameWindow gameWindow = new GameWindow(robotModel);

        addWindow(gameWindow);
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

        JMenu lookAndFeelMenu = new JMenu(localer.getVal("DisplayMode"));
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        {
            JMenuItem systemLookAndFeel = new JMenuItem(localer.getVal("DisplayMode.System"), KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem(localer.getVal("DisplayMode.Universal"), KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        JMenu testMenu = new JMenu(localer.getVal("Tests"));
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        {
            JMenuItem addLogMessageItem = new JMenuItem(localer.getVal("Tests.MessageLog"), KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug(localer.getVal("Log.NewLine"));
            });
            testMenu.add(addLogMessageItem);
        }

        {
            JMenuItem addLogMessageItem = new JMenuItem(localer.getVal("Tests.ErrorLog"), KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.error(localer.getVal("Log.Error"));
            });
            testMenu.add(addLogMessageItem);
        }

        JMenuItem quitItem = new JMenuItem(localer.getVal("CloseApplication"), KeyEvent.VK_X);
            quitItem.addActionListener((event) -> {
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                    new WindowEvent(this, WindowEvent.WINDOW_CLOSING)
            );
        });



        JMenu languageMenu = new JMenu(localer.getVal("Language"));
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Язык интерфейса");

        {
            JMenuItem addLogMessageItem = new JMenuItem(localer.getVal("Language.Rus"), KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Localer.setLocale(0);
                Localer.SaveLocale();
                Localer.reFresh();
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                        new WindowEvent(this, WindowEvent.WINDOW_CLOSING)
                );
            });
            languageMenu.add(addLogMessageItem);
        }

        {
            JMenuItem addLogMessageItem = new JMenuItem(localer.getVal("Language.Trans"), KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Localer.setLocale(1);
                Localer.SaveLocale();
                Localer.reFresh();
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                        new WindowEvent(this, WindowEvent.WINDOW_CLOSING)
                );
            });
            languageMenu.add(addLogMessageItem);
        }

        menuBar.add(languageMenu);
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
        return UtilForComponent.getStateForComponent(this);
    }

}
