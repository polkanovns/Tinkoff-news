package ru.tinkoff.test.data;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsFilter {
    private static final String[] ENDINGS_TO_FILTER = {"-fin", "-eng", "-en", "-e", "-f", "-i"};

    private static final double ENGLISH_LETTERS_THRESHOLD = 0.6;
    private static final String ENGLISH_LETTERS_PATTERN = "[a-zA-Z0-9]";

    public static void filter(List<NewsTitle> titles) {
        Iterator<NewsTitle> iterator = titles.iterator();
        while (iterator.hasNext()) {
            NewsTitle title = iterator.next();
            String name = title.getName();

            //Remove all english news
            Matcher matcher = Pattern.compile(ENGLISH_LETTERS_PATTERN).matcher(title.getText());
            int count = 0;

            while (matcher.find()) {
                count++;
            }

            //Threshold value is calculated empirically
            if (count / (double)title.getText().length() > ENGLISH_LETTERS_THRESHOLD) {
                iterator.remove();
                continue;
            }

            //Check endings
            for (String ending : ENDINGS_TO_FILTER) {
                if (name.endsWith(ending)) {
                    iterator.remove();
                    break;
                }
            }
        }
    }
}
