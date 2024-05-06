package com.utyanskij.samobikes.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Определяем директорию для фотографий велосипедов
        String dirBikeName = "photos/bike-photos";
        Path bikePhotosDir = Paths.get(dirBikeName);

        // Определяем директорию для фотографий запчастей
        String dirPartName = "photos/part-photos";
        Path partPhotosDir = Paths.get(dirPartName);

        // Получаем абсолютный путь к директориям фотографий
        String bikePhotosPath = bikePhotosDir.toFile().getAbsolutePath();
        String partPhotosPath = partPhotosDir.toFile().getAbsolutePath();

        // Добавляем обработчики ресурсов для фотографий велосипедов
        registry.addResourceHandler("/" + dirBikeName + "/**")
                .addResourceLocations(
                        "file:/" + bikePhotosPath + "/",
                        "file:/home/samobikes/samobikes_app/samobikes/" + dirBikeName + "/"
                );
        // Добавляем обработчики ресурсов для фотографий запчастей
        registry.addResourceHandler("/" + dirPartName + "/**")
                .addResourceLocations(
                        "file:/" + partPhotosPath + "/",
                        "file:/home/samobikes/samobikes_app/samobikes/" + dirPartName + "/"
                );
    }
}
