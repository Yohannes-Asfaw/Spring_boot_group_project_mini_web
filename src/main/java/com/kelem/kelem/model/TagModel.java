package com.kelem.kelem.model;



import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class TagModel {
    @Id
    @GeneratedValue
    private Long id;
    private String tag;
    @OneToMany(mappedBy="tags")
    List<QuestionModel> questionModel;    
}
