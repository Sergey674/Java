package DownloadImage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLDownload {



    public static Runnable download(String url, String PathImages, String SavePath, int min) {
        String nameImage = "";

        if (!PathImages.matches(".*.(?:jpg|svg|gif|png)$")) {
            nameImage = String.valueOf ((int) (Math.random() * 100000000)) + String.valueOf((int) (Math.random() * 100000000)) + ".png";
            if (PathImages.startsWith("/")) {
                URLDownload.downloadFile(url + PathImages, SavePath + nameImage, min);
                if (PathImages.startsWith("//")) {
                    URLDownload.downloadFile("https:" + PathImages, SavePath + nameImage, min);
                    if (PathImages.startsWith("http")) {
                        URLDownload.downloadFile(PathImages, SavePath + nameImage, min);
                    }
                }
            }
        }
        return null;
    }

    // Сохраняет картинку, размером больше min КБ, по указанному url, в файл SavePath
    public static Runnable downloadFile(String url, String SavePath, int min) {

        try {

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getContentLength() <= 1024 * min) {
                System.out.println("Картинка с адресом:  " + '\n' + url + '\n' + "не сохранена. Размер меньше " + min + " KБ" + '\n');
                return null;
            }

            //System.out.println('\n' + SavePath + "  !!!  " + url);
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(SavePath));

            byte buff[] = new byte[4096];
            while (in.read(buff) > 0) {
                out.write(buff, 0, 4096);
            }


            System.out.println("Картинка с адресом:  " + '\n' + url + '\n' + " сохранена в " + SavePath + '\n');
            Main.numImage.getAndIncrement();

            in.close();
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
