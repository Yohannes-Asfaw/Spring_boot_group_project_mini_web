package com.kelem.kelem.controller;

import java.io.IOException;
import java.util.List;

import com.kelem.kelem.dao.ReportedAnswerRepository;
import com.kelem.kelem.dao.ReportedQuestionRepository;
import com.kelem.kelem.dao.UserRepository;
import com.kelem.kelem.model.RegistrationForm;
import com.kelem.kelem.model.ReportedAnswerModel;
import com.kelem.kelem.model.ReportedQuestionModel;
import com.kelem.kelem.model.UserModel;
import com.kelem.kelem.services.ReportedQuestionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AdminController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReportedAnswerRepository ansRepo;
    @Autowired
    private ReportedQuestionRepository quesRepo;
    @Autowired
    private ReportedQuestionService reportedQuestionService;
    private final PasswordEncoder passwordEncoder;


    private UserModel loggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // finding the user from the user database based on the principal's name
        UserModel user = userRepository.findByUsername(username);
        return user;
    }

    @GetMapping("/admin")
    public String getAdminPage(Model model) {
        UserModel currentlyLoggedInUser = this.loggedInUser();
        List<ReportedAnswerModel> reportedAns = (List<ReportedAnswerModel>) ansRepo.findAll();
        List<ReportedQuestionModel> reportedques = (List<ReportedQuestionModel>) quesRepo.findAll();
        List<UserModel> users = userRepository.findAllByRole("MEMBER");
        if (currentlyLoggedInUser.getRole().equals("ADMIN")) {
            model.addAttribute("reportedAns", reportedAns);
            model.addAttribute("reportedQues", reportedques);
            model.addAttribute("currentlyLoggedInUser", currentlyLoggedInUser);
            model.addAttribute("users", users);
            return "admin-page";
        }
        return "redirect:/";
    }

    @GetMapping("/add-admin")
    public String addAdmin(Model model) {
        model.addAttribute("userModel", new UserModel());
        return "admin-registration";
    }

    @PostMapping("/add-admin")
    public String adminAdded(RegistrationForm tempUser)
            throws IOException {
        // String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        UserModel user = tempUser.toUser(passwordEncoder);
        user.setProfilePicture("");
        user.setRole("ADMIN");
        UserModel savedUser = userRepository.save(user);
        // String uploadDir = "src/main/resources/static/user-photos/" + savedUser.getId();
        // FileUpload.saveFile(uploadDir, fileName, multipartFile);
        String temp = "redirect:/admin";
        return temp;

    }


}
