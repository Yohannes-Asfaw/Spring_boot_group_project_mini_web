package com.kelem.kelem.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.kelem.kelem.dao.AnswerRepository;
import com.kelem.kelem.model.AnswerModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * An added layer of abstraction.
 */
@Service
public class AnswerService {
    
    @Autowired
    AnswerRepository answerRepository;

    public List<AnswerModel> allAnswers() {
        Iterable<AnswerModel> result = answerRepository.findAll();
        List<AnswerModel> output2 = StreamSupport.stream(result.spliterator(), false)
                                            .collect(Collectors.toList());
        return output2;
    }
    public void save(AnswerModel discussion){
        answerRepository.save(discussion);
    }
    public void delete(Long id){
        answerRepository.deleteById(id);
    }
}