package com.utyanskij.samobikes.services;

import com.utyanskij.samobikes.repositories.HistoryRepository;
import com.utyanskij.samobikes.entities.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


//это класс сервиса, предназначенный для управления историей технического обслуживания велосипедов.
// Этот сервис содержит методы для получения истории обслуживания постранично, сохранения новых записей и очистки истории.
@Service
public class HistoryService {
    public static final int ROWS_PER_PAGE = 10;

    private HistoryRepository historyRepository;

    @Autowired
    public void setHistoryRepository(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }


    //getAllByPage: получает историю технического обслуживания постранично.
    // История сортируется по дате точки (дате обслуживания) в убывающем порядке.
    public Page<History> getAllByPage(int pageNum){
        Sort sort = Sort.by("datePoint");
        sort = sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, ROWS_PER_PAGE, sort);

        return historyRepository.findAll(pageable);
    }

    //сохраняет новую запись о техническом обслуживании в репозитории.
    public void save(History history){
        historyRepository.save(history);
    }


    // очищает всю историю технического обслуживания, удаляя все записи из репозитория.
    public void clean(){
        historyRepository.deleteAll();
    }

}
