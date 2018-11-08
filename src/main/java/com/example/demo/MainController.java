package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class MainController {
    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserService userService;
    // home page

    @RequestMapping("/")
    public String listCourses(Model model){
        model.addAttribute("courses", courseRepository.findAll());

        return "list";
    }

    //register a course
    @GetMapping("/add")
    public String courseForm(Model model){
        model.addAttribute("course",new Course());
        return "courseform";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){

        if(result.hasErrors()){
            return "registration";
        }

        else {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Created");
        }
        return "redirect:/";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    //save the course
    @PostMapping("/process")
    public String processForm(@Valid@ModelAttribute Course course, BindingResult result, Model model, HttpServletRequest request ){

        String username=request.getParameter("username");

        if(result.hasErrors()){
            return "courseform";
        }
        model.addAttribute("username",userService.findByUsername(username));
        courseRepository.save(course);

        return "redirect:/";
    }

    //to see in detail
    @RequestMapping("/detail/{id}")
    public String showCourse(@PathVariable("id") long id, Model model){

        model.addAttribute("course",courseRepository.findById(id).get());
        return "show";
    }

    //update a content
    @RequestMapping("/update/{id}")
    public String updateCourse(@PathVariable("id") long id, Model model){

        model.addAttribute("course",courseRepository.findById(id));
        return  "courseform";
    }

    // delete a content
    @RequestMapping("/delete/{id}")
    public String delCourse(@PathVariable("id") long id){
        courseRepository.deleteById(id);
        return "redirect:/";
    }




}
