package pl.javastart.di.formatter;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class AllUpperTextFormatter implements TextFormatter{
    @Override
    public String format(String text) {
        return text.toUpperCase();
    }

}
