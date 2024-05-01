package com.utyanskij.samobikes.repositories;

import com.utyanskij.samobikes.entities.History;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends PagingAndSortingRepository<History, Integer> {
}
