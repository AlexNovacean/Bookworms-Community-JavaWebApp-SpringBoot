package ro.sci.bookwormscommunity.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CsvViewController {

    @RequestMapping("/download")
    public String home() {
        return "csvDownload";
    }
}