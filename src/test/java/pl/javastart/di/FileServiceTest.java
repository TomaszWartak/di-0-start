package pl.javastart.di;

import org.assertj.core.api.Assertions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class FileServiceTest {

    private static final Entry dogEntry = new Entry( "dog", "pies");
    private static final Entry carEntry = new Entry( "car", "samochód");
    private static final Entry windowEntry = new Entry( "window", "okno");

    @org.junit.jupiter.api.Test
    void saveEntries__should_save_dog_and_car_entries_in_text_file() {
        FileService fileService = new FileService();
        List<Entry> entries = prepareDogAndCarEntries();

        try {
            fileService.saveEntries(entries);
        } catch (IOException ioException) {
            // TODO i co?
        }

        try {
            Assertions.assertThat( fileService.readAllFile().stream().map( entry -> entry.toString()) )
                    .contains( dogEntry.toString() )
                    .contains( carEntry.toString() )
                    .doesNotContain( windowEntry.toString() );
        } catch (IOException ioException) {
            // TODO i co?
        }
    }

    private static List<Entry> prepareDogAndCarEntries() {
        List<Entry> entries = new ArrayList<>();
        entries.add( dogEntry );
        entries.add( carEntry );
        return entries;
    }


}