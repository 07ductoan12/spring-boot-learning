package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

/** WebController */
@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        List<Info> profile = new ArrayList<>();

        profile.add(new Info("fullname", "Nguyễn Đức Toàn"));
        profile.add(new Info("gmail", "07ductoan12@gmail.com"));
        profile.add(
                new Info("facebook", "https://www.facebook.com/profile.php?id=100005369418888"));

        model.addAttribute("toanProfile", profile);
        return "profile";
    }
}
