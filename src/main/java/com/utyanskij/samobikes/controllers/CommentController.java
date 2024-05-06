package com.utyanskij.samobikes.controllers;


import com.utyanskij.samobikes.Utils.StringUtil;
import com.utyanskij.samobikes.entities.*;
import com.utyanskij.samobikes.entities.*;
import com.utyanskij.samobikes.services.BikeService;
import com.utyanskij.samobikes.services.CommentService;
import com.utyanskij.samobikes.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


//Этот код представляет собой контроллер комментариев,
// который обрабатывает операции сохранения и удаления комментариев.
@Controller
public class CommentController {
    CommentService commentService;

    BikeService bikeService;

    UserServiceImpl userService;

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @Autowired
    public void setBikeService(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    //Метод создает новый комментарий, сохраняет его в базе данных,
    // обновляет список комментариев
    // для соответствующего велосипеда и перенаправляет пользователя на страницу велосипеда.
    @PostMapping("/comment/save/{id}")
    public String saveComment (Model model, @PathVariable(value = "id") Integer id,
                               @AuthenticationPrincipal SamUserDetails loggedUser,
                               @RequestParam(value = "currentPage") String currentPage,
                               @RequestParam(value = "sortField") String sortField,
                               @RequestParam(value = "sortDir") String sortDir,
                               @RequestParam(value = "commentSortField") String commentSortField,
                               @RequestParam(value = "commentSortDir") String commentSortDir,
                               @RequestParam(value = "keyword") String keyword,
                               @RequestParam(value = "text") String text){
        Comment comment = new Comment();
        comment.setCommentText(text);
        comment.setCommentedAt(LocalDateTime.now());

        Bike bike = bikeService.getById(id);
        bike.addComment(comment);

        User user = userService.findByUserName(loggedUser.getUsername());
        user.addComment(comment);

        comment.setBike(bike);
        comment.setUser(user);

        commentService.insert(user.getId(), bike.getId(), comment.getCommentText(), comment.getCommentedAt());

        Comment newComment = new Comment();
        List<Part> parts = bike.getParts();

        model.addAttribute("comment", newComment);
        model.addAttribute("comments", commentService.findByBikeId(id, "commentedAt", sortDir));
        model.addAttribute("bike", bike);
        model.addAttribute("parts", parts);
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


    //Метод deleteComment удаляет комментарий из базы данных, обновляет список комментариев
    // для соответствующего велосипеда и перенаправляет пользователя на страницу велосипеда.
    @GetMapping("/comment/delete/{id}")
    public String deleteComment(Model model, @PathVariable(value = "id") Integer id,
                             @Param(value = "currentPage") String currentPage,
                             @Param(value = "sortField") String sortField,
                             @Param(value = "sortDir") String sortDir,
                             @Param(value = "commentSortField") String commentSortField,
                             @Param(value = "commentSortDir") String commentSortDir,
                             @Param(value = "keyword") String keyword){
        Comment comment = commentService.getById(id);
        Bike bike = comment.getBike();

        commentService.deleteById(id);

        Comment newComment = new Comment();
        List<Part> parts = bike.getParts();

        model.addAttribute("comment", newComment);
        model.addAttribute("comments", commentService.findByBikeId(bike.getId(), "commentedAt", sortDir));
        model.addAttribute("bike", bike);
        model.addAttribute("parts", parts);
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
