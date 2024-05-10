package com.utyanskij.samobikes.services;

import com.utyanskij.samobikes.repositories.BikeRepository;
import com.utyanskij.samobikes.entities.Bike;
import com.utyanskij.samobikes.entities.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


//это класс сервиса, который предоставляет методы для управления данными о велосипедах.
// Он содержит логику для выполнения операций с объектами Bike, таких как получение всех велосипедов, постраничный вывод,
// подсчет числа велосипедов, сохранение, удаление и проверка уникальности номера, QR-кода и VIN.
@Service
public class BikeService {
    public static final int BIKES_PER_PAGE = 20;
    private BikeRepository bikeRepository;
    private PartService partService;


    @Autowired
    public void setBikeRepository(BikeRepository bikeRepository){
        this.bikeRepository = bikeRepository;
    }

    @Autowired
    public void setPartService(PartService partService){
        this.partService = partService;
    }

    //получает все велосипеды из репозитория.
    public Iterable<Bike> getAllBikes(){
        return bikeRepository.findAll();
    }


    //получает страницу велосипедов с заданным номером страницы, сортировкой и ключевым словом.
    public Page<Bike> getAllByPage(int pageNum, String sortField, String sortDir, String keyword){
        Sort sort = Sort.by(sortField, "number");
        if ("asc".equals(sortDir)){
            sort = sort.ascending();
        } else{
            sort = sort.descending();
        }
        Pageable pageable = PageRequest.of(pageNum - 1, BIKES_PER_PAGE, sort);

        if (keyword != null){
            return bikeRepository.findAll(keyword, pageable);
        }
        return bikeRepository.findAll(pageable);
    }


    //подсчитывает общее количество велосипедов.
    public int countAll(){
        return (int)bikeRepository.count();
    }


    //подсчитывает количество рабочих велосипедов.
    public int countWorkingBikes(){
        return bikeRepository.countByStatus(true);
    }

    //подсчитывает количество сломанных велосипедов.
    public int countBrokenBikes(){
        return bikeRepository.countByStatus(false);
    }

    public int countworkbikes(){
        return bikeRepository.countByworkstatus(true);
    }
    public int countnotworkbikes(){
        return bikeRepository.countByworkstatus(false);
    }

    //получает велосипед по его идентификатору.
    public Bike getById(Integer id){
        return bikeRepository.findById(id).get();
    }


    //сохраняет велосипед в репозитории, также создает детали (части) велосипеда при необходимости.
    public void save(Bike bike){
        bikeRepository.save(bike);
        List<Part> parts = bike.getParts();
        if (parts.size() == 0){
            parts = partService.createParts(bike);
            bike.setParts(parts);
        }
    }

    // удаляет велосипед по его идентификатору.
    public void deleteById(Integer id){
        bikeRepository.deleteById(id);
    }


    //проверяет уникальность номера велосипеда.
    public boolean isNumberUnique(Integer id, Integer number) {
        Bike bike = bikeRepository.findByNumber(number);
        if (bike == null) return true;

        return checkUniqueness(id, bike);
    }


    // проверяет уникальность QR-кода велосипеда.
    public boolean isQRNumberUnique(Integer id, Integer qrNumber) {
        Bike bike = bikeRepository.findByqrNumber(qrNumber);
        if (bike == null) return true;

        return checkUniqueness(id, bike);
    }


    //проверяет уникальность VIN велосипеда.
    public boolean isVINUnique(Integer id, String VIN) {
        Bike bike = bikeRepository.findByVIN(VIN);
        if (bike == null) return true;


        return checkUniqueness(id, bike);
    }

    private boolean checkUniqueness(Integer id, Bike bike){
        if(id == null){
            return bike == null;
        }else {
            return Objects.equals(bike.getId(), id);
        }
    }
}
