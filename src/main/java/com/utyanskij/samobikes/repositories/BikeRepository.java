package com.utyanskij.samobikes.repositories;

import com.utyanskij.samobikes.entities.Bike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BikeRepository extends PagingAndSortingRepository<Bike, Integer> {
    int countByStatus (boolean status);
    Bike findByNumber(Integer number);
    Bike findByqrNumber(Integer qrNumber);
    Bike findByVIN(String VIN);

    @Query("SELECT u FROM Bike u WHERE CONCAT(u.number, ' ', u.qrNumber, ' ', u.VIN) LIKE %?1%")
    public Page<Bike> findAll(String keyword, Pageable pageable);

}
