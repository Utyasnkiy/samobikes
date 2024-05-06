package com.utyanskij.samobikes.services;

import com.utyanskij.samobikes.repositories.CommentRepository;
import com.utyanskij.samobikes.entities.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

//это класс сервиса, предоставляющий методы для управления комментариями к велосипедам.
@Service
public class CommentService {

    CommentRepository commentRepository;

    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    //получает все комментарии из репозитория.
    public Collection<Comment> findAll(){
        return commentRepository.findAll();
    }


    //находит комментарии для определенного велосипеда, отсортированные по указанному полю и направлению сортировки.
    public Collection<Comment> findByBikeId(Integer bikeId, String sortField, String sortDir){
        if ("asc".equals(sortDir)) return commentRepository.findByBikeIdOrderByCommentedAtAsc(bikeId);
        else return commentRepository.findByBikeIdOrderByCommentedAtDesc(bikeId);
    }


    //сохраняет комментарий в репозитории.
    public void save(Comment comment){
        commentRepository.save(comment);
    }

    // вставляет новый комментарий с указанными данными.
    public void insert(Integer userId,Integer bikeId,String commentText,LocalDateTime commentedAt){
        commentRepository.insert(userId, bikeId, commentText, commentedAt);
    }


    // удаляет комментарий по его идентификатору.
    public void deleteById(Integer id){
        commentRepository.deleteById(id);
    }


    //получает комментарий по его идентификатору.
    public Comment getById(Integer id){
        return commentRepository.findById(id).get();
    }
}

