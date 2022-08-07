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
                "public interface " + repoName + "Repository extends JpaRepository<" + repoName + ", " + idType + "> {}";
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
                + "\t* Returns all " + utils.checkPlural(serviceName) + ".\n"
                + "\t*\n"
                + "\t* @return List of " + serviceName + "\n"
                + "\t*/\n"
                + "\tpublic List<" + serviceName + "> findAll" + utils.checkPlural(serviceName) + "() {\n"
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

                + "\n\n\t/**\n"
                + "\t* Creates a new " + serviceName.toLowerCase() + ".\n"
                + "\t*\n"
                + "\t* @param dto " + serviceName.toLowerCase() + " infos\n"
                + "\t* @return " + serviceName + "\n"
                + "\t*/\n"
                + "\tpublic " + serviceName + " create" + serviceName+ "(" + serviceName + "Dto dto) {\n"
                + "\t\tif (dto == null) {\n"
                + "\t\t\tthrow new IllegalArgumentException(\"" + serviceName + "Dto cannot be null\");\n"
                + "\t\t}\n"
                + "\t\t" + serviceName + " " + serviceName.toLowerCase() + " = Mapper.mapDtoTo" + serviceName+ "(dto);\n"
                + "\t\treturn this." + serviceName.toLowerCase() + "Repository.save(" + serviceName.toLowerCase() + ");\n"
                + "\t}"


                + "\n}";
    }

    public String generateDomain(String domainName, String idType, Map<String, String> attributesList, List<String> relationsList) {
        Logger.getGlobal().info("Generating " + domainName + " Domain");
        // Content generation
        String fileContent =
                "@Builder\n" +
                        "@Entity\n" +
                        "@Table(name = \"" + utils.checkPlural(domainName.toLowerCase()) + "\", schema = \"public\")\n" +
                        "@Data\n" +
                        "@NoArgsConstructor\n" +
                        "@AllArgsConstructor\n" +
                        "public class " + domainName + " implements Serializable {\n"
                        + "\n\t@Id\n"
                        + "\t@GeneratedValue(strategy = GenerationType.IDENTITY)\n"
                        + "\tprivate " + idType + " id;";

        // ATTRIBUTES
        List<String> tempAttributesList = new ArrayList<>();
        attributesList.forEach((fieldName, fieldType) -> tempAttributesList.add(appendFieldsForDomain(fieldName, fieldType)));
        String appendAttributes = String.join("", tempAttributesList);

        // RELATIONS
        List<String> tempRelationsList = new ArrayList<>();
        relationsList.forEach(value -> {
            if (value.startsWith("@OneToMany")) {
                String obj = value.split(":")[1];
                String attribute = value.split(":")[2];
                String adding = "\t@OneToMany(mappedBy = \"" + attribute + "\")"
                        + "\n\t@JsonIgnore"
                        + "\n\tprivate List<" + obj + "> " + obj.toLowerCase() + "s = new ArrayList<>();\n"
                        + "\n\t// TODO ADD THE FOLLOWING ATTRIBUTE TO ENTITY : " + obj;
                tempRelationsList.add(adding);
            } else if (value.startsWith("@ManyToOne")) {
                String relationType = value.split(":")[0];
                String obj = value.split(":")[1];
                String attribute = value.split(":")[2];
                String adding = "\n\t" + relationType
                        + "\n\tprivate " + obj + " " + attribute + ";\n";
                tempRelationsList.add(adding);
            }
        });
        String appendRelations = String.join("", tempRelationsList);

        return fileContent + "\n" + appendAttributes + "\n" + appendRelations + "}";
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
                        "\t@GetMapping(\"/" + utils.checkPlural(controllerName.toLowerCase()) + "\")\n" +
                        "\tpublic List<" + controllerName + "Dto> get" + utils.checkPlural(controllerName) + "() {\n" +
                        "\t\tList<" + controllerName + "Dto> " + controllerName.toLowerCase() + " = this." + controllerName.toLowerCase() + "Service.findAll" + utils.checkPlural(controllerName) + "().stream()\n" +
                        "\t\t\t.map(Mapper::map" + controllerName + "ToDto)\n" +
                        "\t\t\t.collect(Collectors.toList());\n" +
                        "\t\treturn " + controllerName.toLowerCase() + ";\n" +
                        "\t}\n" +
                        "\n" +
                        "\t@GetMapping(\"/" + controllerName.toLowerCase() + "/{id}\")\n" +
                        "\tpublic " + controllerName + "Dto get" + controllerName + "ById(@PathVariable " + idType + " id) {\n" +
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
                "@Builder\n"
                + "@NoArgsConstructor\n"
                + "@Component\n"
                + "public class Mapper {\n"
                + "\n\t// Quick note : use only 1 Mapper class that has all of the generated mapper methods (and use them statically)\n"
                + "\n"
                + "\t/*\n\t* " + dtoName + " Entity to DTO mapper\n\t*/"
                + "\n\tpublic static " + dtoName + "Dto map" + dtoName + "ToDto(" + dtoName + " " + dtoName.toLowerCase() + ") {\n"
                + "\t\treturn " + dtoName + "Dto.builder()";

        List<String> tempMapToDtoList = new ArrayList<>();
        tempMapToDtoList.add("\t\t\t.id(" + dtoName.toLowerCase() + ".getId())\n");
        attributesList.forEach((fieldName, fieldType) -> tempMapToDtoList.add(appendFieldsForMapperToDto(dtoName, fieldName, fieldType)));
        String appendMapToDto = String.join("", tempMapToDtoList);
        fileContent += "\n" + appendMapToDto + "\t\t\t.build();\n\t}\n";

        fileContent +=
                "\n\t/*\n\t* DTO to " + dtoName + " Entity mapper\n\t*/"
                + "\n\tpublic static " + dtoName + " mapDtoTo" + dtoName + "(" + dtoName + "Dto dto) {\n"
                + "\t\treturn " + dtoName + ".builder()";
        List<String> tempMapDtoToEntityList = new ArrayList<>();
        tempMapDtoToEntityList.add("\t\t\t.id(dto.getId())\n");
        attributesList.forEach((fieldName, fieldType) -> tempMapDtoToEntityList.add(appendFieldsForMapperToEntity(fieldName, fieldType)));
        String appendMapToEntity = String.join("", tempMapDtoToEntityList);
        fileContent += "\n" + appendMapToEntity + "\t\t\t.build();\n\t}";

        return fileContent + "\n}";
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

    private String appendFieldsForMapperToDto(String objectName, String name, String type) {
        return "\t\t\t." + name + "(" + objectName.toLowerCase() + ".get" + utils.formatFistLetterCapital(name) + "())\n";
    }

    private String appendFieldsForMapperToEntity(String name, String type) {
        return "\t\t\t." + name + "(dto.get" + utils.formatFistLetterCapital(name) + "())\n";
    }

}
