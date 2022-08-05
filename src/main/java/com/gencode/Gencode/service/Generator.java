package com.gencode.Gencode.service;

import com.gencode.Gencode.utils.Utils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
@Data
public class Generator {

    @Autowired
    private Utils utils;

    public String generateReporitory(String repoName, String idType) {
        Logger.getGlobal().info("Generating " + repoName + " Repository");
        // Content generation
        return "@Repository\n" +
                "public interface " + repoName + "Repository extends JpaRepository<" + repoName + ", "+ idType +"> {}";
    }

    public String generateService(String serviceName, String idType) {
        Logger.getGlobal().info("Generating " + serviceName + " Service");
        // Content generation
        return "@Service\n" +
                "public class " + serviceName + "Service {\n"
                + "\tprivate " + serviceName + "Repository " + serviceName.toLowerCase() + "Repository;\n"
                + "\n"

                + "\t@Autowired\n"
                + "\tpublic " + serviceName + "Service(" + serviceName + "Repository " + serviceName.toLowerCase() + "Repository) {\n"
                + "\t\tthis." + serviceName.toLowerCase() + "Repository = " + serviceName.toLowerCase() + "Repository;\n"
                + "\t}\n"
                + "\n"

                + "\t/**\n"
                + "\t* Returns all " + serviceName + "s.\n"
                + "\t*\n"
                + "\t* @return List of " + serviceName + "\n"
                + "\t*/\n"
                + "\tpublic List<" + serviceName + "> findAll" + serviceName + "s() {\n"
                + "\t\treturn this." + serviceName.toLowerCase() + "Repository.findAll();\n"
                + "\t}\n"
                + "\n"

                + "\t/**\n"
                + "\t* Search by id.\n"
                + "\t*\n"
                + "\t* @param id id\n"
                + "\t* @return " + serviceName + "\n"
                + "\t*/\n"
                + "\tpublic " + serviceName + " find" + serviceName + "ById(" + idType + " id) {\n"
                + "\t\treturn this." + serviceName.toLowerCase() + "Repository.findById(id).orElse(null);\n"
                + "\t}"
            + "\n}";
    }

    public String generateDomain(String domainName, String idType, Map<String, String> attributesList) {
        Logger.getGlobal().info("Generating " + domainName + " Domain");
        // Content generation
        String fileContent =
            "@Builder\n" +
            "@Entity\n" +
            "@Table(name = \"" + domainName.toLowerCase() + "s\", schema = \"public\")\n" +
            "@Data\n" +
            "@NoArgsConstructor\n" +
            "@AllArgsConstructor\n" +
            "public class " + domainName + " implements Serializable {\n"
            + "\n\t@Id\n"
            + "\t@GeneratedValue(strategy = GenerationType.IDENTITY)\n"
            + "\tprivate " + idType + " id;";

        List<String> tempList = new ArrayList<>();
        attributesList.forEach((fieldName, fieldType) -> tempList.add(appendFieldsForDomain(fieldName, fieldType)));

        String finalAppend = String.join("", tempList);
        return fileContent + "\n" + finalAppend + "}";
    }

    public String generateController(String controllerName, String idType) {
        Logger.getGlobal().info("Generating " + controllerName + " Controller");
        // Content generation
        String fileContent =
                "@RestController\n" +
                "public class " + controllerName + "Controller {\n" +
                "\n" +
                "\t@Autowired\n" +
                "\tprivate " + controllerName + "Service " + controllerName.toLowerCase() + "Service;\n" +
                "\n" +
                "\t@GetMapping(\"/" + controllerName.toLowerCase() + "s\")\n" +
                "\tpublic List<" + controllerName + "Dto> get" + controllerName + "s() {\n" +
                "\t\tList<" + controllerName + "Dto> " + controllerName.toLowerCase() + " = this." + controllerName.toLowerCase() + "Service.findAll" + controllerName + "s().stream()\n" +
                "\t\t\t.map(Mapper::map" + controllerName + "ToDto)\n" +
                "\t\t\t.collect(Collectors.toList());\n" +
                "\t\treturn " + controllerName.toLowerCase() + ";\n" +
                "\t}\n" +
                "\n" +
                "\t@GetMapping(\"/" + controllerName.toLowerCase() + "/{id}\")\n" +
                "\tpublic " + controllerName + "Dto get" + controllerName + "(@PathVariable " + idType + " id) {\n" +
                "\t\treturn Mapper.map" + controllerName + "ToDto(this." + controllerName.toLowerCase() + "Service.find" + controllerName + "ById(id));\n" +
                "\t}";

        return fileContent + "\n}";
    }

    public String generateDto(String dtoName, String idType, Map<String, String> attributesList) {
        Logger.getGlobal().info("Generating " + dtoName + " DTO");
        // Content generation
        String fileContent =
                "@Builder\n" +
                "@Data\n" +
                "@AllArgsConstructor\n" +
                "@NoArgsConstructor\n" +
                "public class " + dtoName + "Dto {\n" +
                "\n\tprivate " + idType + " id;";

        List<String> tempList = new ArrayList<>();
        attributesList.forEach((fieldName, fieldType) -> tempList.add(appendFieldsForDto(fieldName, fieldType)));

        String finalAppend = String.join("", tempList);
        return fileContent + "\n" + finalAppend + "}";
    }

    public String generateMapper(String dtoName, String idType, Map<String, String> attributesList) {
        Logger.getGlobal().info("Generating " + dtoName + " Mapper");
        // Content generation
        String fileContent =
                "@Builder\n" +
                "@NoArgsConstructor\n" +
                "@Component\n" +
                "public class Mapper {\n"
                + "\n\t// Quick note : use only 1 Mapper class that has all of the generated mapper methods (and use them statically)\n"
                + "\n\t/*\n\t* " + dtoName + " Entity to DTO mapper\n\t*/"
                + "\n\tpublic static " + dtoName + "Dto map" + dtoName + "ToDto(" + dtoName + " " + dtoName.toLowerCase() + ") {\n"
                + "\t\treturn " + dtoName + "Dto.builder()";

        List<String> tempList = new ArrayList<>();
        attributesList.forEach((fieldName, fieldType) -> tempList.add(appendFieldsForMapper(dtoName, fieldName, fieldType)));

        String finalAppend = String.join("", tempList);
        return fileContent + "\n" + finalAppend + "\t\t\t.build();\n\t}\n}";
    }



    private String appendFieldsForDomain(String name, String type) {
        if (type.equalsIgnoreCase("boolean")) {
            if (name.startsWith("is")) {
                name = name.replaceAll("is", "");
            }
        }
        return "\n\t@Column(name = \"" + name.toLowerCase() + "\")\n\tprivate " + type + " " + name.toLowerCase() + ";\n";
    }

    private String appendFieldsForDto(String name, String type) {
        return "\n\tprivate " + type + " " + name + ";\n";
    }

    private String appendFieldsForMapper(String objectName, String name, String type) {
        return "\t\t\t." + name + "(" + objectName.toLowerCase() + ".get" + utils.formatFistLetterCapital(name) + "())\n";
    }

}
