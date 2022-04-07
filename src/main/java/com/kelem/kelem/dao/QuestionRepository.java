package com.kelem.kelem.dao;


import java.util.List;

import com.kelem.kelem.model.QuestionModel;
import com.kelem.kelem.model.TagModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
 

@Repository
public interface QuestionRepository extends PagingAndSortingRepository<QuestionModel, Long>{
    
    @Query(value = "select * from question_model where content like %:keyWord%", nativeQuery = true)
    List<QuestionModel> findByContent(@Param("keyWord") String keyWord);
    @Query(value = "SELECT * FROM question_model WHERE MATCH(topic, description, content) " + "AGAINST (?1)", nativeQuery = true)          
    public Page<QuestionModel> search(String keyWord, Pageable pageable);
    List<QuestionModel> findByTags(TagModel tagModel);
}
