package com.utyanskij.samobikes.services;

import com.utyanskij.samobikes.repositories.HistoryRepository;
import com.utyanskij.samobikes.entities.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {
    public static final int ROWS_PER_PAGE = 10;

    private HistoryRepository historyRepository;

    @Autowired
    public void setHistoryRepository(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public Page<History> getAllByPage(int pageNum){
        Sort sort = Sort.by("datePoint");
        sort = sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, ROWS_PER_PAGE, sort);

        return historyRepository.findAll(pageable);
    }

    public void save(History history){
        historyRepository.save(history);
    }

    public void clean(){
        historyRepository.deleteAll();
    }

}
