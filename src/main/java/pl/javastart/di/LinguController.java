package pl.javastart.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

@Controller
class LinguController {
    private static final int UNDEFINED = -1;
    private static final int ADD_ENTRY = 0;
    private static final int TEST = 1;
    private static final int CLOSE_APP = 2;

    private final EntryRepository entryRepository;
    private final FileService fileService;
    private final Scanner scanner;
    private final ConsoleWriter consoleWriter;

    public LinguController(EntryRepository entryRepository, FileService fileService, Scanner scanner, ConsoleWriter consoleWriter) {
        this.entryRepository = entryRepository;
        this.fileService = fileService;
        this.scanner = scanner;
        this.consoleWriter = consoleWriter;
    }

    void mainLoop() {
        consoleWriter.write("Witaj w aplikacji LinguApp");
        int option = UNDEFINED;
        while(option != CLOSE_APP) {
            printMenu();
            option = chooseOption();
            executeOption(option);
        }
    }

    private void executeOption(int option) {
        switch (option) {
            case ADD_ENTRY -> addEntry();
            case TEST -> test();
            case CLOSE_APP -> close();
            default -> consoleWriter.write("Opcja niezdefiniowana");
        }
    }

    private void test() {
        if(entryRepository.isEmpty()) {
            consoleWriter.write("Dodaj przynajmniej jedną frazę do bazy.");
            return;
        }
        final int testSize = Math.min(entryRepository.size(), 10);
        Set<Entry> randomEntries = entryRepository.getRandomEntries(testSize);
        int score = 0;
        for (Entry entry : randomEntries) {
            System.out.printf("Podaj tłumaczenie dla :\"%s\"\n", entry.getOriginal());
            String translation = scanner.nextLine();
            if(entry.getTranslation().equalsIgnoreCase(translation)) {
                consoleWriter.write("Odpowiedź poprawna");
                score++;
            } else {
                consoleWriter.write("Odpowiedź niepoprawna - " + entry.getTranslation());
            }
        }
        System.out.printf("Twój wynik: %d/%d\n", score, testSize);
    }

    private void addEntry() {
        consoleWriter.write("Podaj oryginalną frazę");
        String original = scanner.nextLine();
        consoleWriter.write("Podaj tłumaczenie");
        String translation = scanner.nextLine();
        Entry entry = new Entry(original, translation);
        entryRepository.add(entry);
    }

    private void close() {
        try {
            fileService.saveEntries(entryRepository.getAll());
            consoleWriter.write("Zapisano stan aplikacji");
        } catch (IOException e) {
            consoleWriter.write("Nie udało się zapisać zmian");
        }
        consoleWriter.write("Bye Bye!");
    }

    private void printMenu() {
        consoleWriter.write("Wybierz opcję:");
        consoleWriter.write("0 - Dodaj frazę");
        consoleWriter.write("1 - Test");
        consoleWriter.write("2 - Koniec programu");
    }

    private int chooseOption() {
        int option;
        try {
            option = scanner.nextInt();
        } catch(InputMismatchException e) {
            option = UNDEFINED;
        } finally {
            scanner.nextLine();
        }
        if(option > UNDEFINED && option <= CLOSE_APP)
            return option;
        else
            return UNDEFINED;
    }
}
