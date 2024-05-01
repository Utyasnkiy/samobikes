package com.utyanskij.samobikes.services;

import com.utyanskij.samobikes.repositories.PartNameRepository;
import com.utyanskij.samobikes.entities.PartName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartNameService {
    private PartNameRepository partNameRepository;

    @Autowired
    public void setPartNameRepository(PartNameRepository partNameRepository) {
        this.partNameRepository = partNameRepository;
    }

    public List<PartName> getAll(){
        return partNameRepository.findAll();
    }

    public PartName getById(Integer id){
        return partNameRepository.getReferenceById(id);
    }

    public void save(PartName partName){
        partNameRepository.save(partName);
    }

    public void deleteById(Integer id){
        partNameRepository.deleteById(id);
    }
}
