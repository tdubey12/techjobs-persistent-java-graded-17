package org.launchcode.techjobs.persistent.controllers;

import jakarta.validation.Valid;
import org.launchcode.techjobs.persistent.models.Employer;
import org.launchcode.techjobs.persistent.models.data.EmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("employers")
public class EmployerController {
    @Autowired
    private EmployerRepository employerRepository;

    @GetMapping("/") //http://localhost:8080/employers/
    public String index(Model model) {
        model.addAttribute("title", "All Employers");
        model.addAttribute("employers", employerRepository.findAll());
        return "employers/index";
    }

    @GetMapping("add") //http://localhost:8080/employers/add
    public String displayAddEmployerForm(Model model) {

        model.addAttribute(new Employer());

        return "employers/add";
    }

    @PostMapping("add")  //http://localhost:8080/employers/add
    public String processAddEmployerForm(@ModelAttribute @Valid Employer newEmployer,
                                    Errors errors, Model model) {

        if (errors.hasErrors()) {
            return "employers/add";
        }

        employerRepository.save(newEmployer);


        return "redirect:";
    }

    @GetMapping("view/{employerId}")   //http://localhost:8080/employers/view/1
    public String displayViewEmployer(Model model, @PathVariable int employerId) {

        Optional optEmployer = employerRepository.findById(employerId);
        if (optEmployer.isPresent()) {
            Employer employer = (Employer) optEmployer.get();
            model.addAttribute("employer", employer);

            return "employers/view";
        } else {
            return "employers/view";
        }

    }
}
