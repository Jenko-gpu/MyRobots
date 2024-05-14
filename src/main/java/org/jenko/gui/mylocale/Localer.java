package org.jenko.gui.mylocale;

import java.text.MessageFormat;
import java.util.*;


/**
 * Класс для локализации приложения
 *
 */
public class Localer {

    private final Map<Integer, String> cache;

    public static int getLocale_id() {
        return locale_id;
    }

    private static int locale_id = 0;
    private static Locale locale; //= new Locale("ru", "RU");
    private static ResourceBundle rb;

    private static Localer instance = new Localer();

    /**
     * Получить объект для удобного обращения
     *
     */
    public static Localer getLocaler(){
        return instance;
    }

    private Localer(){
        cache = new TreeMap<Integer, String>();
        locale_id = LocaleSaver.readLocale();
        setLocale(locale_id);
        ResourceBundle.clearCache();
        rb = ResourceBundle.getBundle("text", locale);

    }



    /**
     * Установить локаль: 0 - Русский язык, 1 - Русский транслитом
     * @param loc
     */
    public static void setLocale(int loc){
        if (loc >= 2 || loc < 0){
            loc = 0;
        }
        if (locale_id != loc){
            locale_id = loc;

        }
        switch (locale_id){
            case 0: // ru
                locale = new Locale("ru", "RU");
                break;
            case 1: // translit
                locale = new Locale("ru","LATIN");
                break;
            default:
                locale = new Locale("ru");
                break;
        }
    }


    public static void reFresh(){
        ResourceBundle.clearCache();
        rb = ResourceBundle.getBundle("text", locale);
    }

    /**
     * Получить локализированную строку по ключу.
     * @param key
     * @return
     */
    public String getVal(String key){
        return rb.getString(key);
    }

    /**
     * Форматировать строку
     * @param pattern
     * @param args
     * @return
     */
    public String format(String pattern, Object... args){
        int key = Objects.hash(pattern, args);

        String formattedString = cache.computeIfAbsent(key, k -> MessageFormat.format(pattern, args));

        return formattedString;
    }
    public static void SaveLocale(){
        LocaleSaver.writeLocale(locale_id);
    }

}

