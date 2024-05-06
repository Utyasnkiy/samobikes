package com.utyanskij.samobikes.controllers;

import com.utyanskij.samobikes.DTO.PartListDTO;
import com.utyanskij.samobikes.Utils.StringUtil;
import com.utyanskij.samobikes.entities.*;
import com.utyanskij.samobikes.entities.*;
import com.utyanskij.samobikes.services.BikeService;
import com.utyanskij.samobikes.services.PartService;
import com.utyanskij.samobikes.services.HistoryService;
import com.utyanskij.samobikes.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


//Этот контроллер управляет операциями с запчастями
@Controller
@RequestMapping("/parts")
public class PartController {
    private PartService partService;

    private BikeService bikeService;

    private UserService userService;

    private HistoryService historyService;

    @Autowired
    public void setPartService (PartService partService){
        this.partService = partService;
    }

    @Autowired
    public void setBikeService (BikeService bikeService){
        this.bikeService = bikeService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }


    //Этот метод showEditPartForm предназначен для отображения формы редактирования запчастей велосипеда.
    @GetMapping("/edit/{id_bike}")
    public String showEditPartForm(Model model, @PathVariable(value = "id_bike") Integer id,
                                   @Param("currentPage") String currentPage,
                                   @Param("sortField") String sortField,
                                   @Param("sortDir") String sortDir,
                                   @Param("commentSortField") String commentSortField,
                                   @Param("commentSortDir") String commentSortDir,
                                   @Param("keyword") String keyword){
        Bike bike = bikeService.getById(id);
        List<Part> parts = bike.getParts();
        PartListDTO partListDTO = new PartListDTO();
        partListDTO.setBikeId(bike.getId());
        partListDTO.setParams(id + "?currentPage=" + currentPage + "&sortField=" + sortField + "&sortDir=" + sortDir + "&commentSortField=commentedAt&commentSortDir=" + commentSortDir + (keyword != null ? "&keyword=" + keyword : ""));

        int temp = 0;
        if (parts.size() % 2 != 0) temp = 1;

        List<Part> partsLeft = parts.subList(0, parts.size() / 2 + temp);
        List<Part> partsRight = parts.subList(parts.size() / 2 + temp, parts.size());
        partListDTO.setPartsLeft(partsLeft);
        partListDTO.setPartsRight(partsRight);

        model.addAttribute("partList", partListDTO);
        model.addAttribute("parts", parts);
        model.addAttribute("bike", bike);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", StringUtil.reverseSortDir(sortDir));
        model.addAttribute("commentSortField", commentSortField);
        model.addAttribute("commentSortDir", commentSortDir);
        model.addAttribute("commentReverseSortDir", StringUtil.reverseSortDir(sortDir));
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("keyword", keyword);

        return "part-edit";
    }

    @PostMapping("/edit")
    public String editPart (Model model,
                            @ModelAttribute(value = "partList") PartListDTO partListDTO,
                            @AuthenticationPrincipal SamUserDetails loggedUser){
        Bike bike = bikeService.getById(partListDTO.getBikeId());
        List<Part> parts = bike.getParts();

        List<List<Part>> doubleParts = new ArrayList<>();
        doubleParts.add(parts);
        doubleParts.add(partListDTO.getParts());
        int userId = userService.findByUserName(loggedUser.getUsername()).getId();
        for(int i = 0; i < parts.size(); i ++){
            //если переключение на "сломан"
            if (doubleParts.get(0).get(i).isStatus() && !doubleParts.get(1).get(i).isStatus()) {
                History rowHistory = new History(userId, bike.getId(), "поломка: " + parts.get(i).getName(), LocalDateTime.now());
                historyService.save(rowHistory);
            }
            //если переключение на "работает"
            if (!doubleParts.get(0).get(i).isStatus() && doubleParts.get(1).get(i).isStatus()) {
                History rowHistory = new History(userId, bike.getId(), "ремонт: " + parts.get(i).getName(), LocalDateTime.now());
                historyService.save(rowHistory);
            }
        }

        parts.clear();
        parts.addAll(partListDTO.getParts());
        bike.checkWorks();
        bikeService.save(bike);
        model.addAttribute("parts", bike.getParts());
        model.addAttribute("bike", bike);
        model.addAttribute("comment", new Comment());
        model.addAttribute("comments", bike.getComments());

        return "redirect:/bikes/show/" + partListDTO.getParams();
    }


    //Этот метод setAllImpTrue устанавливает статус всех запчастей с
    //важностью менее 3 в качестве "работает" и сохраняет изменения.
    @GetMapping("/fine/{id_bike}")
    public String setAllImpTrue(Model model, @PathVariable(value = "id_bike")Integer id,
                                @AuthenticationPrincipal SamUserDetails loggedUser,
                                @Param("currentPage") String currentPage,
                                @Param("sortField") String sortField,
                                @Param("sortDir") String sortDir,
                                @Param("commentSortField") String commentSortField,
                                @Param("commentSortDir") String commentSortDir,
                                @Param("keyword") String keyword) {

        int userId = userService.findByUserName(loggedUser.getUsername()).getId();

        Bike bike = bikeService.getById(id);
        List<Part> parts = bike.getParts();

        parts.forEach(s -> {
            if (s.getImportance() < 3 && !s.isStatus()) {
                s.setStatus(true);
                History rowHistory = new History(userId, bike.getId(), "ремонт: " + s.getName(), LocalDateTime.now());
                historyService.save(rowHistory);
            }
        });

        bike.setParts(parts);
        bike.setStatus(true);
        bikeService.save(bike);
        model.addAttribute("bike", bike);
        model.addAttribute("parts", parts);
        model.addAttribute("comment", new Comment());
        model.addAttribute("comments", bike.getComments());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", StringUtil.reverseSortDir(sortDir));
        model.addAttribute("commentSortField", commentSortField);
        model.addAttribute("commentSortDir", commentSortDir);
        model.addAttribute("commentReverseSortDir", StringUtil.reverseSortDir(commentSortDir));
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("keyword", keyword);

        return StringUtil.createBikePageRedirectLink(bike.getId(), currentPage, sortField, sortDir, commentSortDir, keyword);
    }


    //Этот метод setAllTrue устанавливает статус "работает" для всех запчастей велосипеда и сохраняет изменения.
    @GetMapping("/fineAll/{id_bike}")
    public String setAllTrue(Model model, @PathVariable(value = "id_bike")Integer id,
                             @AuthenticationPrincipal SamUserDetails loggedUser,
                             @Param("currentPage") String currentPage,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("commentSortField") String commentSortField,
                             @Param("commentSortDir") String commentSortDir,
                             @Param("keyword") String keyword) {

        int userId = userService.findByUserName(loggedUser.getUsername()).getId();

        Bike bike = bikeService.getById(id);
        List<Part> parts = bike.getParts();

        parts.forEach(s -> {
            if (!s.isStatus()) {
                s.setStatus(true);
                History rowHistory = new History(userId, bike.getId(), "ремонт: " + s.getName(), LocalDateTime.now());
                historyService.save(rowHistory);
            }
        });

        bike.setParts(parts);
        bike.setStatus(true);
        bikeService.save(bike);
        model.addAttribute("bike", bike);
        model.addAttribute("parts", parts);
        model.addAttribute("comment", new Comment());
        model.addAttribute("comments", bike.getComments());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", StringUtil.reverseSortDir(sortDir));
        model.addAttribute("commentSortField", commentSortField);
        model.addAttribute("commentSortDir", commentSortDir);
        model.addAttribute("commentReverseSortDir", StringUtil.reverseSortDir(commentSortDir));
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("keyword", keyword);

        return StringUtil.createBikePageRedirectLink(bike.getId(), currentPage, sortField, sortDir, commentSortDir, keyword);
    }
}
