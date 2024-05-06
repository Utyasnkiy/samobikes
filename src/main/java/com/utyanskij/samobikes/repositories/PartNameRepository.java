package com.utyanskij.samobikes.repositories;

import com.utyanskij.samobikes.entities.PartName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartNameRepository extends JpaRepository<PartName, Integer> {
}

//Этот интерфейс позволяет выполнять различные операции с данными о наименованиях деталей, такие как чтение, запись, обновление и удаление записей.