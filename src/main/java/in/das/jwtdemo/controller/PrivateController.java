package in.das.jwtdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/api")
public class PrivateController {

    @GetMapping("/data")
    public String data(){
        return "accessing private data...";
    }
}
