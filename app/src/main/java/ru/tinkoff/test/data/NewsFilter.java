package ru.tinkoff.test.data;


import java.util.Iterator;
import java.util.List;

public class NewsFilter {
    private static final String[] ENDINGS_TO_FILTER = {"-fin", "-eng", "-en", "-e", "-f", "-i"};

    public static void filter(List<NewsTitle> titles) {

        Iterator<NewsTitle> iterator = titles.iterator();
        while (iterator.hasNext()) {
            NewsTitle title = iterator.next();
            String name = title.getName();

            for (String ending : ENDINGS_TO_FILTER) {
                if (name.endsWith(ending)) {
                    iterator.remove();
                }
            }
        }
    }
}
