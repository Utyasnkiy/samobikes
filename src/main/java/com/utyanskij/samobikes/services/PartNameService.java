package com.utyanskij.samobikes.services;

import com.utyanskij.samobikes.repositories.PartNameRepository;
import com.utyanskij.samobikes.entities.PartName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//сервисный класс, предназначенный для управления сущностями "Название детали".
// Он обеспечивает методы для выполнения операций с сущностями "Название детали",
// таких как получение всех названий деталей, получение названия детали по идентификатору,
// сохранение нового названия детали и удаление названия детали по идентификатору.
@Service
public class PartNameService {
    private PartNameRepository partNameRepository;

    @Autowired
    public void setPartNameRepository(PartNameRepository partNameRepository) {
        this.partNameRepository = partNameRepository;
    }
    //возвращает список всех названий деталей.
    public List<PartName> getAll(){
        return partNameRepository.findAll();
    }

    //возвращает название детали по заданному идентификатору.
    public PartName getById(Integer id){
        return partNameRepository.getReferenceById(id);
    }


    //сохраняет новое название детали.
    public void save(PartName partName){
        partNameRepository.save(partName);
    }


    // удаляет название детали по заданному идентификатору.
    public void deleteById(Integer id){
        partNameRepository.deleteById(id);
    }
}
