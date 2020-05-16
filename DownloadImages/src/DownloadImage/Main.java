package DownloadImage;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.jsoup.Jsoup.*;

public class Main {

    private static String SavePath;
    private static int min;
    private static HashSet<String> SetURL = new HashSet<>();
    static AtomicInteger numImage = new AtomicInteger(0);
    private static Executor executor = Executors.newCachedThreadPool();
    private static String PathImages;
    private static Document document;

    public static void main(String[] args) {
        long start_time = System.currentTimeMillis();

        try {
            String URL = loadParameters();

            System.out.println("Загружаем картинки с сайта: " + URL);
            getPathImages(URL);

            getImgOnHyperlinks(URL);

            System.out.println("Всего скачано: " + numImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Суммарное время: " + (System.currentTimeMillis() - start_time) + " (мс)");
    }

    //Функция загрузки параметров
    private static String loadParameters() {
        try {
            FileInputStream file = new FileInputStream("src\\DownloadImage\\load_parametres.properties");
            Properties properties = new Properties();

            properties.load(file);

            SavePath = properties.getProperty("SavePath");
            min = Integer.parseInt(properties.getProperty("minKB"));

            file.close();

            return properties.getProperty("URL");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Функция добавления метода загруки картинки с заданным url в поток
    static void getPathImages(String URL) {
        try {
            String PathImages;
            Elements elements = connect(URL).userAgent("Mozilla").get().getElementsByTag("img");
            for (Element elem : elements) {
                PathImages = elem.attr("src");

                if (SetURL.add(PathImages)) {
                    String ItogPath = PathImages;
                    //System.out.println('\n' + URL + " " + ItogPath + '\n');
                    executor.execute(() -> {
                        URLDownload.download(URL, ItogPath, SavePath, min);
                    });
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Загрузка всех картинок по гиперссылкам
    static void getImgOnHyperlinks(String URL) throws IOException {
        document = connect(URL).userAgent("Mozilla").get();
        Elements linksImage = document.getElementsByTag("a");
        for (Element elem : linksImage) {

            PathImages = elem.attr("href");

            if (PathImages.startsWith("//")) {
                getPathImages("https://" + PathImages.substring(2));

            } else {
                if (PathImages.startsWith("/")) {
                    getPathImages("https://" + new URL(URL).getHost() + PathImages);
                } else {
                    getPathImages(PathImages);
                }
            }
        }
    }

}
