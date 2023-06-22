package pl.javastart.di;

import org.springframework.stereotype.Component;
import pl.javastart.di.formatter.TextFormatter;

@Component
public class ConsoleOutputWriter {

    private final TextFormatter textFormatter;

    public ConsoleOutputWriter(TextFormatter textFormatter) {
        this.textFormatter = textFormatter;
    }

    public void println(String message ) {
        System.out.println( textFormatter.format( message ) );
    }

}
