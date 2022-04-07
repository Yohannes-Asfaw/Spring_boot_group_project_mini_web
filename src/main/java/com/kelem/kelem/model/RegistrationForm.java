package com.kelem.kelem.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;

@Data
public class RegistrationForm {
    @NotNull(message="Username can't be null")
    @NotBlank(message="Username can't be blank")
    private String username;
    @NotNull(message="Password can't be null")
    @NotBlank(message="password can't be blank")
    private String password;
    @NotNull(message="Last Name can't be null")
    @NotBlank(message="Last Name can't be blank")
    private String lastName;
    @NotNull(message="First Name can't be null")
    @NotBlank(message="First Name can't be blank")
    private String firstName;

   /**
    * In order avoid exposing our user class to the login form, we will instead use this proxiy object to mimic our user from which we extract the user information we need.
    * @param encoder
    * @return
    */
   public UserModel toUser(PasswordEncoder encoder) {
       UserModel user = new UserModel();
       user.setFirstName(this.firstName);
       user.setLastName(this.lastName);
       user.setUsername(this.username);
       user.setPassword(encoder.encode(this.password));
       user.setRole("MEMBER");
       return user;
   }
}

