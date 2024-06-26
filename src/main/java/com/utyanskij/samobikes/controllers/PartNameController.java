package com.utyanskij.samobikes.controllers;

import com.utyanskij.samobikes.Utils.FileUploadUtil;
import com.utyanskij.samobikes.entities.PartName;
import com.utyanskij.samobikes.services.PartNameService;
import com.utyanskij.samobikes.services.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;


//Этот класс PartNameController управляет операциями, связанными с наименованиями деталей велосипеда.
@Controller
public class PartNameController {
    PartNameService partNameService;

    PartService partService;

    @Autowired
    public void setPartNameService(PartNameService partNameService) {
        this.partNameService = partNameService;
    }

    @Autowired
    public void setPartService(PartService partService) {
        this.partService = partService;
    }

    //Этот метод извлекает все наименования деталей из базы данных и добавляет их в модель. Затем он возвращает имя представления "part-names",
    // которое, вероятно, отображает таблицу всех наименований деталей.
    @GetMapping("/part-names")
    public String showPartNamesTable(Model model){
        List<PartName> partNames = partNameService.getAll();
        model.addAttribute("partNames", partNames);

        return "part-names";
    }


    //showAddBikeForm: Этот метод подготавливает новый объект PartName и добавляет его в модель. Затем он возвращает имя представления "part-names-edit",
    // которое, вероятно, содержит форму для добавления нового наименования детали.
    @GetMapping("/part-names/add")
    public String showAddBikeForm(Model model){
        PartName partName = new PartName();
        model.addAttribute("partName", partName);
        return "part-names-edit";
    }


    //showAddBikeForm: Этот метод подготавливает новый объект PartName и добавляет его в модель. Затем он возвращает имя представления "part-names-edit",
    // которое, вероятно, содержит форму для добавления нового наименования детали.
    @PostMapping("/part-names/edit")
    public String savePartName (@ModelAttribute(value = "partName") PartName partName,
                            RedirectAttributes redirectAttributes,
                            @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            partName.setDescription(fileName);
            partService.saveForAll(partName);
            partNameService.save(partName);

            String uploadDir = "photos/part-photos/";
            FileUploadUtil.deleteFile(uploadDir, partName.getDescription());
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        } else {
            if (partName.getDescription().isEmpty()) partName.setDescription("");
            partService.saveForAll(partName);
            partNameService.save(partName);
        }

        redirectAttributes.addFlashAttribute(
                "message",
                "Запчасть " + partName.getName()  + " сохранена для всех велосипедов.");
        return "redirect:/part-names";
    }


    //Этот метод обрабатывает удаление наименования детали. Сначала он извлекает наименование детали по его идентификатору, удаляет его связанный файл изображения из каталога, удаляет наименование детали из базы данных и добавляет сообщение во флэш-атрибут.
    //Наконец, он перенаправляет пользователя на конечную точку "/part-names".
    @GetMapping("/part-names/delete/{id}")
    public String deletePartName(@PathVariable(value = "id") Integer id,
                             RedirectAttributes redirectAttributes){
        PartName partName = partNameService.getById(id);

        String deleteDir = "photos/part-photos/";
        FileUploadUtil.deleteFile(deleteDir, partName.getDescription());

        partNameService.deleteById(id);
        partService.deleteByName(partName.getName());
        redirectAttributes.addFlashAttribute(
                "message",
                "Запчасть " + partName.getName()  + " удалена из всех велосипедов.");
        return "redirect:/part-names";
    }
}
