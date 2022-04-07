package com.kelem.kelem.services;

import java.util.List;

import com.kelem.kelem.dao.ReportedAnswerRepository;
import com.kelem.kelem.model.ReportedAnswerModel;

import org.springframework.stereotype.Service;

@Service
public class ReportedAnswerService {
    ReportedAnswerRepository repository;
    public List<ReportedAnswerModel> listAll(){
        return (List<ReportedAnswerModel>) repository.findAll();
    }
    
}
