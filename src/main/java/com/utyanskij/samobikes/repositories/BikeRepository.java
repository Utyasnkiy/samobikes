package com.utyanskij.samobikes.repositories;

import com.utyanskij.samobikes.entities.Bike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


//BikeRepository - это интерфейс репозитория,
// который обеспечивает доступ к данным о велосипедах в базе данных.
// Он расширяет интерфейс PagingAndSortingRepository,
// что позволяет осуществлять постраничное и сортированное получение данных.
@Repository
public interface BikeRepository extends PagingAndSortingRepository<Bike, Integer> {
    int countByStatus (boolean status);
    int countByworkstatus (boolean workstastus);
    Bike findByNumber(Integer number);
    Bike findByqrNumber(Integer qrNumber);
    Bike findByVIN(String VIN);

    //осуществляет поиск велосипедов по ключевому слову, которое может встречаться в номере, QR-коде или VIN-коде.
    //Метод возвращает страницу результатов, с учетом параметров постраничного вывода и сортировки.
    @Query("SELECT u FROM Bike u WHERE CONCAT(u.number, ' ', u.qrNumber, ' ', u.VIN) LIKE %?1%")
    public Page<Bike> findAll(String keyword, Pageable pageable);

}
