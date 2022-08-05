package com.gencode.Gencode.controllers;

import com.gencode.Gencode.dto.ObjectDto;
import com.gencode.Gencode.service.Generator;
import com.gencode.Gencode.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class IndexController {

    @Autowired
    private Utils utils;

    @Autowired
    private Generator generator;

    @RequestMapping("/")
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect("/index");
    }

    @RequestMapping("/index")
    public String getIndex(Model model) {
        model.addAttribute("objectdto", ObjectDto.builder().build());
        return "index";
    }

    @PostMapping("/generate")
    public String submitForm(@Valid ObjectDto objectDto, Errors errors, Model model) throws IOException {
        if (null != errors && errors.getErrorCount() > 0) {
            return "index";
        } else {
            objectDto.setName((!objectDto.getName().isEmpty()) ? utils.formatAppName(objectDto.getName()) : "");

            // REPOSITORY GENERATION
            String repositoryContent = generator.generateReporitory(objectDto.getName(), objectDto.getIdType());
            objectDto.setRepositoryContent(repositoryContent);

            // SERVICE GENERATION
            String serviceContent = generator.generateService(objectDto.getName(), objectDto.getIdType());
            objectDto.setServiceContent(serviceContent);

            model.addAttribute("objectdto", objectDto);


            return "appname_success";
        }
    }

}
