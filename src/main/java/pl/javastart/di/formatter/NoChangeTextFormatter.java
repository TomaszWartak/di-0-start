package pl.javastart.di.formatter;

import org.springframework.stereotype.Component;

@Component
public class NoChangeTextFormatter implements TextFormatter{
    @Override
    public String format(String text) {
        return text;
    }

}
