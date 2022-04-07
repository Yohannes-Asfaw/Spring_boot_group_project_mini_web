package com.kelem.kelem.dao;

import java.util.List;

import com.kelem.kelem.model.QuestionModel;
import com.kelem.kelem.model.ReportedQuestionModel;

import org.springframework.data.repository.CrudRepository;

public interface ReportedQuestionRepository extends CrudRepository<ReportedQuestionModel, Long>{
    // public List<ReportedQuestionModel> findByQuestion(QuestionModel question);
}
