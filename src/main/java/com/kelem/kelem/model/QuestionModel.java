package com.kelem.kelem.model;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Data;

@Entity
@Data
public class QuestionModel {
    
    public enum Status {
        ANSWERED,
        UNANSWERED;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Long id;
    @NotNull(message="The topic can't be null")
    @NotBlank(message="The topic can't be blank")
    String topic;
    // String description;
    @NotNull(message="The content can't be null")
    @NotBlank(message="The content can't be blank")
    String content;
    @ManyToOne(fetch = FetchType.LAZY)
    UserModel user;
    String status; 
    @ManyToOne(fetch = FetchType.LAZY)
    private TagModel tags;
    @OneToMany(mappedBy="question")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<AnswerModel> answer;
}
