package org.launchcode.techjobs.persistent.controllers;

import jakarta.validation.Valid;
import org.launchcode.techjobs.persistent.models.Employer;
import org.launchcode.techjobs.persistent.models.Job;
import org.launchcode.techjobs.persistent.models.Skill;
import org.launchcode.techjobs.persistent.models.data.EmployerRepository;
import org.launchcode.techjobs.persistent.models.data.JobRepository;
import org.launchcode.techjobs.persistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
public class HomeController {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private SkillRepository skillRepository;

    @RequestMapping("/") //http://localhost:8080/
    public String index(Model model) {
        model.addAttribute("jobs",jobRepository.findAll());

        model.addAttribute("title", "MyJobs");

        return "index";
    }

    @GetMapping("add") //http://localhost:8080/add
    public String displayAddJobForm(Model model) {
	    model.addAttribute("title", "Add Job");
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("skills",skillRepository.findAll());

        model.addAttribute(new Job());

        return "add";
    }

    @PostMapping("add") //http://localhost:8080/add
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                       Errors errors, Model model, int employerId, @RequestParam List<Integer> skills) {

        if (errors.hasErrors()) {
	    model.addAttribute("title", "Add Job");
            return "add";
        }
        List<Skill> skillObjs = (List<Skill>) skillRepository.findAllById(skills);
        newJob.setSkills(skillObjs);


        Optional<Employer> result=employerRepository.findById(employerId);
        if (result.isEmpty()) {
            model.addAttribute("title", "Invalid employer ID: " + employerId);
        } else {
            Employer employer = result.get();
            newJob.setEmployer(employer);


            jobRepository.save(newJob);
        }

        return "redirect:";
    }

    @GetMapping("view/{jobId}") //http://localhost:8080/view/152
    public String displayViewJob(Model model, @PathVariable int jobId) {

            Optional<Job> result= jobRepository.findById(jobId);
            Job job=result.get();
            model.addAttribute("job",job);

            return "view";
    }

}
