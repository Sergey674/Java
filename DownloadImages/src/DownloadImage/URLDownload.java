package DownloadImage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLDownload {

    // Сохраняет картинку, размером больше min КБ, по указанному url, в файл SavePath
    public static Runnable downloadFile(String url, String SavePath, int min) {

        try
        {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getContentLength() <= 1024 * min) {
                System.out.println("Картинка с адресом:  " + '\n' + url + '\n' + "не сохранена. Размер меньше " + min + " KБ" + '\n');
                return null;
            }

            InputStream in = new DataInputStream(conn.getInputStream());
            OutputStream out = new FileOutputStream(SavePath);

            byte buff[] = new byte[1];
            //int  num = in.read(buff);

            while (in.read(buff) > 0) {
                out.write(buff, 0, 1);
            }


            System.out.println("Картинка с адресом:  " + '\n' + url + '\n' +  " сохранена в " + SavePath + '\n');
            Main.numImage.getAndIncrement();

            in.close();
            out.flush();
            out.close();
        } catch (FileNotFoundException e){

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Runnable download(String url, String PathImages, String SavePath, int min) {
        String nameImage = "";

        if(!PathImages.matches(".*.(?:jpg|svg|gif|png)$")){
            nameImage = String.valueOf((long) (Math.random() * 100000000000L)) + ".png";
        }

        if (PathImages.matches("\\/*")) {
            URLDownload.downloadFile(url + PathImages, SavePath + nameImage, min);
        } else if (PathImages.startsWith("//")) {
            URLDownload.downloadFile("https:" + PathImages, SavePath + nameImage, min);
        } else if (PathImages.startsWith("http")) {
            URLDownload.downloadFile(PathImages, SavePath + nameImage, min);
        }
        return null;
    }
}
