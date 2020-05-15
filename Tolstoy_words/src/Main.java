import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

public class Main {

    public static void main(String args[]) {

        try (Stream<String> rows = Files.lines(Paths.get("src//Tolstoy.txt")))
        {
            HashMap<String, Integer> Frequency = GetCountWords(3, 15, rows);
            printHashMap(Frequency);
            
        }
        catch (IOException e) {
            System.out.println("Не найден указанный файл. " + e.getLocalizedMessage());
        }
    }

    public static HashMap<String, Integer>  GetCountWords(int min, int max, Stream<String> rows)
    {
        Stream<String> stream = rows.flatMap(line -> Stream.of(line.split("[^A-Za-zА-Яа-я]+")));

        stream = stream.filter(str -> {
            return str.length() > min && str.length() < max;
        });

        HashMap<String, Integer> NumberOfwords = stream.collect(HashMap::new, (map, value) -> {
            map.merge(value, 1, Integer::sum);
        }, HashMap::putAll);

        return NumberOfwords;


    }

    public static void printHashMap( HashMap<String, Integer> hp)
    {
        hp.entrySet().stream().sorted(HashMap.Entry.<String, Integer>comparingByValue()).forEach(System.out::println);
    }
}
