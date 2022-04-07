package com.kelem.kelem.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
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
public class UserModel {
    
    public enum ROLE {
        ADMIN,
        MEMBER;
    }
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Long id;
    @Column(unique = true)
    String username;
    String password;
    @NotNull(message="First name can't be null.")
    @NotBlank(message="Last name can't be blank.")
    String firstName;
    @NotNull(message="First name can't be null.")
    @NotBlank(message="Last name can't be blank.")
    String lastName;
    String role;
    @Column(nullable = true, length = 255)
	private String profilePicture;
    @OneToMany(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="user")
    private List<QuestionModel> questionModel;
    @OneToMany(mappedBy="user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<AnswerModel> answerModel;
    @OneToMany(mappedBy="userModel")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Vote> vote;


    @Transient
    public String getProfilePicture() {
        if (profilePicture.length() == 0) {
            return "/user-photos/1/abebe.jpeg";
        }
         
        return "/user-photos/" + id + "/" + profilePicture;
    }
}