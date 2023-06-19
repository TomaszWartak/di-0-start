package pl.javastart.di;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class FileService {
    private static final String FILE_NAME = "data.csv";

    List<Entry> readAllFile() throws IOException {
        return Files.readAllLines(Paths.get(FILE_NAME))
            .stream()
            .map(CsvEntryConverter::parse)
            .toList();
    }

    void saveEntries(List<Entry> entries) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Entry entry : entries) {
                writer.write(entry.toString());
                writer.newLine();
            }
        }

    }

    private static class CsvEntryConverter {
        static Entry parse(String text) {
            String[] split = text.split(";");
            return new Entry(split[0], split[1]);
        }
    }
}
