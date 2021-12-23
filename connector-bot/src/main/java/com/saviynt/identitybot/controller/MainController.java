package com.saviynt.identitybot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.saviynt.identitybot.model.Job;
import com.saviynt.identitybot.repository.JobRepository;

@Controller
public class MainController {
	@Autowired
	JobRepository jobRepository;
    @GetMapping("/")
    public String root(Model model) {

		List<Job> jobs = jobRepository.findAllByCreatedAtDesc();
		model.addAttribute("jobs", jobs);
		model.addAttribute("url", "jobs");
        return "jobs";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

//    @GetMapping("/user")
//    public String userIndex() {
//        return "user/index";
//    }
}