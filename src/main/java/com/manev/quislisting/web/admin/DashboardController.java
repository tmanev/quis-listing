package com.manev.quislisting.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/ql-admin")
public class DashboardController {

    @RequestMapping(method = RequestMethod.GET)
    public String dashboardPage() {
        return "admin/index";
    }

}
