package com.kelem.kelem.dao;

import com.kelem.kelem.model.ReportedAnswerModel;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportedAnswerRepository extends CrudRepository<ReportedAnswerModel, Long>{
}
