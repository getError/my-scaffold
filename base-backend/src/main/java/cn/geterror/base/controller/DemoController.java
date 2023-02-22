package cn.geterror.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/demo")
public class DemoController extends BaseController{
    @GetMapping("/alive")
    public JsonResult checkAlive(){
        return ok().data("alive");
    }
}
