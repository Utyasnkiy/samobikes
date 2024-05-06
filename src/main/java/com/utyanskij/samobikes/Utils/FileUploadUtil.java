package com.utyanskij.samobikes.Utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


//предоставляет утилитарные методы для загрузки, удаления и очистки файлов в приложении.
public class FileUploadUtil {

    //сохраняет файл, полученный в виде объекта
    public static  void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        // в указанной директории. Если директория не существует, она будет создана.
        // Этот метод обрабатывает возможные исключения, связанные с сохранением файла.
        if (System.getProperty("os.name").contains("Linux")){
            uploadPath = Paths.get(System.getProperty("user.home"), "/samobikes_app/samobikes/", uploadDir);
        }

        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e){
            throw new IOException("Невозможно сохранить файл: " + fileName, e);
        }
    }

    // удаляет все файлы в указанной директории.
    public static void cleanDir(String dir){
        Path dirPath = Paths.get(dir);
        if (System.getProperty("os.name").contains("Linux")){
            dirPath = Paths.get(System.getProperty("user.home"), "/samobikes_app/samobikes/", dir);
        }

        try{
            Files.list(dirPath).forEach(file -> {
                if (!Files.isDirectory(file)){
                    try{
                        Files.delete(file);
                    }catch (IOException e){
                        System.out.println("Невозможно удалить файл " + file);
                    }
                }
            });
        }catch (IOException e){
            System.out.println("Невозможно найти местоположение файла " + dirPath);
        }
    }


    //удаляет указанный файл из директории.
    public static void deleteFile(String dir, String fileName){
        Path dirPath = Paths.get(dir, fileName);
        if (System.getProperty("os.name").contains("Linux")){
            dirPath = Paths.get(System.getProperty("user.home"), "/samobikes_app/samobikes/", dir, fileName);
        }

        try{
            Files.delete(dirPath);
        }catch (IOException e){
            System.out.println("Невозможно найти местоположение файла " + dirPath);
        }
    }
}
