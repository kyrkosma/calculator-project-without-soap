package org.springframework.samples.mvc.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sign-in/*")
public class LoginController {

    @GetMapping("/")
    public String loginPage() {
        return "loginPage";
    }

}
