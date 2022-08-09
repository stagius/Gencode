package com.gencode.Gencode.controllers;

import com.gencode.Gencode.dtos.ObjectDto;
import com.gencode.Gencode.service.Generator;
import com.gencode.Gencode.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    private Utils utils;

    @Autowired
    private Generator generator;

    @RequestMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Received a request from : " + request.getServerName() + ", redirecting to /index...");
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
            objectDto.setName((!objectDto.getName().isEmpty()) ? utils.formatFistLetterCapital(objectDto.getName()) : "");

            // REPOSITORY GENERATION
            String repositoryContent = generator.generateRepository(objectDto.getName(), objectDto.getIdType());
            objectDto.setRepositoryContent(repositoryContent);

            // SERVICE GENERATION
            String serviceContent = generator.generateService(objectDto.getName(), objectDto.getIdType());
            objectDto.setServiceContent(serviceContent);

            // DOMAIN GENERATION
            Map<String, String> attributesList = utils.formatAttributes(objectDto.getAttributes());
            List<String> relationsList = utils.formatRelations(objectDto.getRelations());
            String domainContent = generator.generateDomain(objectDto.getName(), objectDto.getIdType(), attributesList, relationsList);
            objectDto.setDomainContent(domainContent);

            // CONTROLLER GENERATION
            String controllerContent = generator.generateController(objectDto.getName(), objectDto.getIdType());
            objectDto.setControllerContent(controllerContent);

            // DTO GENERATION
            String dtoContent = generator.generateDto(objectDto.getName(), objectDto.getIdType(), attributesList);
            objectDto.setDtoContent(dtoContent);

            // MAPPER GENERATION
            String mapperContent = generator.generateMapper(objectDto.getName(), objectDto.getIdType(), attributesList);
            objectDto.setMapperContent(mapperContent);


            model.addAttribute("objectdto", objectDto);
            return "appname_success";
        }
    }

}
