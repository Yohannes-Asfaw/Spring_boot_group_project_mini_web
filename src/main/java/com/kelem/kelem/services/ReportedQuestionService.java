package com.kelem.kelem.services;

import java.util.List;

import com.kelem.kelem.dao.ReportedQuestionRepository;
import com.kelem.kelem.model.ReportedQuestionModel;

import org.springframework.stereotype.Service;

@Service
public class ReportedQuestionService {
    ReportedQuestionRepository repository;
    public List<ReportedQuestionModel> listAll(){
        return (List<ReportedQuestionModel>) repository.findAll();
    }
    
}
