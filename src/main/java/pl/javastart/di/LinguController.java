package pl.javastart.di;

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
    private final ConsoleOutputWriter consoleOutputWriter;

    public LinguController(EntryRepository entryRepository, FileService fileService, Scanner scanner, ConsoleOutputWriter consoleOutputWriter ) {
        this.entryRepository = entryRepository;
        this.fileService = fileService;
        this.scanner = scanner;
        this.consoleOutputWriter = consoleOutputWriter;
    }

    void mainLoop() {
        consoleOutputWriter.println("Witaj w aplikacji LinguApp");
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
            default -> consoleOutputWriter.println("Opcja niezdefiniowana");
        }
    }

    private void test() {
        if(entryRepository.isEmpty()) {
            consoleOutputWriter.println("Dodaj przynajmniej jedną frazę do bazy.");
            return;
        }
        final int testSize = Math.min(entryRepository.size(), 10);
        Set<Entry> randomEntries = entryRepository.getRandomEntries(testSize);
        int score = 0;
        for (Entry entry : randomEntries) {
            consoleOutputWriter.println( String.format("Podaj tłumaczenie dla :\"%s\"", entry.getOriginal() ) );
            String translation = scanner.nextLine();
            if(entry.getTranslation().equalsIgnoreCase(translation)) {
                consoleOutputWriter.println("Odpowiedź poprawna\n");
                score++;
            } else {
                consoleOutputWriter.println("Odpowiedź niepoprawna - " + entry.getTranslation()+"\n");
            }
        }
        consoleOutputWriter.println( String.format( "Twój wynik: %d/%d%n", score, testSize) );
    }

    private void addEntry() {
        consoleOutputWriter.println("Podaj oryginalną frazę");
        String original = scanner.nextLine();
        consoleOutputWriter.println("Podaj tłumaczenie");
        String translation = scanner.nextLine();
        Entry entry = new Entry(original, translation);
        entryRepository.add(entry);
    }

    private void close() {
        try {
            fileService.saveEntries(entryRepository.getAll());
            consoleOutputWriter.println("Zapisano stan aplikacji");
        } catch (IOException e) {
            consoleOutputWriter.println("Nie udało się zapisać zmian");
        }
        consoleOutputWriter.println("Bye Bye!");
    }

    private void printMenu() {
        consoleOutputWriter.println("Wybierz opcję:");
        consoleOutputWriter.println("0 - Dodaj frazę");
        consoleOutputWriter.println("1 - Test");
        consoleOutputWriter.println("2 - Koniec programu");
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
