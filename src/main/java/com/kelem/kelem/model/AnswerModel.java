package com.kelem.kelem.model;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AnswerModel {

    // @Transient
    // AnswerRepository answerRepository;
    // @Transient
    // private static VoteRepository voteRepository;

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    // @OnDelete(action = OnDeleteAction.CASCADE)
    private QuestionModel question;
    @NotNull(message="Content can't be null")
    @NotBlank(message="Content can't be blank")
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserModel user;
    @OneToMany(mappedBy = "answerModel")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Vote> vote;

    // public Long upVote() {
    //     return voteRepository.findUpVote(this.id);
    // }

    // public Long downVote() {
    //     return voteRepository.findUpVote(this.id);
    // }
    @OneToOne
    private ReportedAnswerModel reportedAnswer; 
    
}
