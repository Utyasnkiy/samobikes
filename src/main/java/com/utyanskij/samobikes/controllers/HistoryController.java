package com.utyanskij.samobikes.controllers;

import com.utyanskij.samobikes.entities.Bike;
import com.utyanskij.samobikes.entities.History;
import com.utyanskij.samobikes.services.BikeService;
import com.utyanskij.samobikes.services.HistoryService;
import com.utyanskij.samobikes.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.NoSuchElementException;

//отвечает за обработку запросов, связанных с историей ремонта.
@Controller
public class HistoryController {
    private HistoryService historyService;

    private UserServiceImpl userService;

    private BikeService bikeService;

    @Autowired
    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Autowired
    public void setBikeService(BikeService bikeService) {
        this.bikeService = bikeService;
    }


    //Метод showHistory отображает историю ремонта на первой странице.
    @GetMapping("/history")
    public String showHistory(Model model){
        return showHistoryByPage(model, 1);
    }

    //Метод showHistoryByPage отображает историю ремонта на указанной странице.
    @GetMapping("/history/page/{pageNum}")
    public String showHistoryByPage(Model model,
                                  @PathVariable(name = "pageNum") int pageNum){
        Page<History> page = historyService.getAllByPage(pageNum);
        List<History> historyList = page.getContent();
        prepareHistoryList(historyList);

        model.addAttribute("history", historyList);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalItems", page.getTotalElements());

        return "repair-history";
    }

    @GetMapping("/history/clean")
    public String cleanHistory(){
        historyService.clean();

        return "redirect:/history";
    }


    //Метод prepareHistoryList подготавливает список истории ремонта для отображения,
    // заменяя идентификаторы пользователя и велосипеда на их соответствующие имена и номера.
    private void prepareHistoryList(List<History> historyList){
        historyList.forEach(s -> {
            try{
                if (s.getUserId() == -1) s.setUsername("Нет данных");
                else s.setUsername(userService.getById(s.getUserId()).getUsername());
            }catch (NoSuchElementException ex){
                s.setUsername("Удалён");
            }
            try{
                if (s.getBikeId() == -1) {
                    s.setNumber("Нет данных");
                    s.setQrNumber("Нет данных");
                    s.setVIN("Нет данных");
                } else {
                    Bike bike = bikeService.getById(s.getBikeId());
                    s.setNumber(String.valueOf(bike.getNumber()));
                    s.setQrNumber(String.valueOf(bike.getQrNumber()));
                    s.setVIN(bike.getVIN());
                }
            }catch (NoSuchElementException ex){
                s.setNumber("Удалён");
                s.setQrNumber("Удалён");
                s.setVIN("Удалён");
            }
        });
    }
}
