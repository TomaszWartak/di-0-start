package pl.javastart.di;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:properties.properties")
class FileService {

    @Value("${app.data-config.data-filename}")
    private String fileName;

    List<Entry> readAllFile() throws IOException {
        return Files.readAllLines(Paths.get(fileName))
            .stream()
            .map(CsvEntryConverter::parse)
            .collect(Collectors.toList());
    }

    void saveEntries(List<Entry> entries) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
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
