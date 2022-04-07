package com.kelem.kelem.dao;

import java.util.List;

import com.kelem.kelem.model.AnswerModel;
import com.kelem.kelem.model.DiscussionModel;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DiscussionRepository extends CrudRepository<DiscussionModel, Long> {
    List<DiscussionModel> findByAnswer(AnswerModel answer);
    List<DiscussionModel> findByAnswer_id(Long id);
    @Query(value="SELECT answer_id FROM discussion_model WHERE id like %?1 ", nativeQuery=true)
    Long findByDiscussion_id(Long discussion_id);
    

}