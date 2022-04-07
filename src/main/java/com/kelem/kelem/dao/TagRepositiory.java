package com.kelem.kelem.dao;


import java.util.List;

import com.kelem.kelem.model.TagModel;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface TagRepositiory extends CrudRepository<TagModel, Long> {
    @Query(value = "select * from tag_model where tag like %:keyTag%", nativeQuery = true)
    List<TagModel> findByTag(@Param("keyTag") String keyTag);
}
