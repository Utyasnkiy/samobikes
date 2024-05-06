package com.utyanskij.samobikes.services;

import com.utyanskij.samobikes.repositories.PartRepository;
import com.utyanskij.samobikes.entities.Bike;
import com.utyanskij.samobikes.entities.Part;
import com.utyanskij.samobikes.entities.PartName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


//сервисный класс, предназначенный для управления сущностями "Деталь".
// Он обеспечивает методы для выполнения операций с деталями,
// таких как сохранение детали, получение детали по идентификатору,
// создание деталей для конкретного велосипеда и удаление детали по имени.
@Service
public class PartService {
    private PartRepository partRepository;

    private PartNameService partNameService;

    private BikeService bikeService;

    @Autowired
    public void setPartRepository(PartRepository partRepository){
        this.partRepository = partRepository;
    }

    @Autowired
    public void setPartNameService(PartNameService partNameService) {
        this.partNameService = partNameService;
    }

    @Autowired
    public void setBikeService(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    public void save (Part part){
        partRepository.save(part);
    }

    public Part getById(Integer id){
        return partRepository.getReferenceById(id);
    }

    public List<Part> createParts(Bike bike){
        List<PartName> partNames = partNameService.getAll();
        List<Part> parts = new ArrayList<>();
        for (PartName partName:
             partNames) {
            Part part = new Part(partName.getName(), partName.getImportance(), partName.getDescription(), true, bike);
            parts.add(part);
            partRepository.save(part);
        }

        return parts;
    }

    public void deleteByName(String name) {
        partRepository.deleteByName(name);
    }

    public void saveForAll(PartName partName){
        Iterable<Bike> bikes = bikeService.getAllBikes();
        bikes.forEach(bike -> {
            bike.getParts()
                    .add(new Part(partName.getName(),
                            partName.getImportance(),
                            partName.getDescription(),
                            true,
                            bike));
            bikeService.save(bike);
        });
    }
}
