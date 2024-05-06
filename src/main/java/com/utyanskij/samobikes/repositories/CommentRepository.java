package com.utyanskij.samobikes.repositories;

import com.utyanskij.samobikes.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;


//это интерфейс репозитория, который обеспечивает доступ к данным о комментариях к велосипедам в базе данных.
// Он расширяет интерфейс JpaRepository, предоставляя стандартные методы для работы с сущностями JPA.
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Collection<Comment> findByBikeIdOrderByCommentedAtDesc(Integer bikeId);

    Collection<Comment> findByBikeIdOrderByCommentedAtAsc(Integer bikeId);


    // выполняет вставку нового комментария в базу данных с указанными параметрами.
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO comments (user_id, bike_id, comment_text, commented_at) VALUES (:userId, :bikeId, :commentText, :commentedAt)",
            nativeQuery = true)
    void insert(@Param("userId") Integer userId, @Param("bikeId") Integer bikeId,
                @Param("commentText") String commentText, @Param("commentedAt") LocalDateTime commentedAt);
}
//Этот интерфейс позволяет выполнять различные операции с данными о комментариях к велосипедам,
// такие как поиск, сортировка и вставка новых комментариев.