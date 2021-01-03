package com.biubiu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("")
public class IndexController {


    @RequestMapping(value = {"/picture/**", "/"})
    public String index(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //TODO 权限校验
        return "dist/index";
    }

    @GetMapping("/api/currentUser")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userid", "105281");
        userMap.put("name", "张音乐");
        userMap.put("avatar", "");
        return new ResponseEntity<>(userMap, HttpStatus.OK);
    }
}
