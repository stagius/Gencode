package com.gencode.Gencode.service;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Service
@Data
public class Generator {

    private static final String OUTPUT = "output/src/main/java";

    public String generateReporitory(String appname, String idType) throws IOException {
        Logger.getGlobal().info("Generating " + appname + " Repository");
        String repositoriesPath = OUTPUT + "/repositories/";
        String fileContent = "";

        // Path does not exist
        if (!Files.exists(Paths.get(repositoriesPath))) {
            Files.createDirectories(Paths.get(repositoriesPath));
            Logger.getGlobal().info("Created folders : " + repositoriesPath);
        }

        FileWriter fileWriter = new FileWriter(repositoriesPath + appname + "Repository.java");
        PrintWriter printWriter = new PrintWriter(fileWriter);

        // File content generation
        fileContent = "@Repository\n" +
                "public interface " + appname + "Repository extends JpaRepository<" + appname + ", "+ idType +"> {}";
        printWriter.printf(fileContent);

        printWriter.close();

        return fileContent;
    }

    public String generateService(String appname, String idType) throws IOException {
        Logger.getGlobal().info("Generating " + appname + " Service");
        String servicesPath = OUTPUT + "/services/";
        String fileContent = "";

        // Path does not exist
        if (!Files.exists(Paths.get(servicesPath))) {
            Files.createDirectories(Paths.get(servicesPath));
            Logger.getGlobal().info("Created folders : " + servicesPath);
        }

        FileWriter fileWriter = new FileWriter(servicesPath + appname + "Service.java");
        PrintWriter printWriter = new PrintWriter(fileWriter);

        // File content generation
        fileContent = "@Service\n" +
                "public class " + appname + "Service {\n"
                + "\tprivate " + appname + "Repository " + appname.toLowerCase() + "Repository;\n"
                + "\n"

                + "\t@Autowired\n"
                + "\tpublic " + appname + "Service(" + appname + "Repository " + appname.toLowerCase() + "Repository) {\n"
                + "\t\tthis." + appname.toLowerCase() + "Repository = " + appname.toLowerCase() + "Repository;\n"
                + "\t}\n"
                + "\n"

                + "\t/**\n"
                + "\t* Returns all " + appname + "s.\n"
                + "\t*\n"
                + "\t* @return List of " + appname + "\n"
                + "\t*/\n"
                + "\tpublic List<" + appname + "> findAll" + appname + "s() {\n"
                + "\t\treturn this." + appname.toLowerCase() + "Repository.findAll();\n"
                + "\t}\n"
                + "\n"

                + "\t/**\n"
                + "\t* Search by id.\n"
                + "\t*\n"
                + "\t* @param id id\n"
                + "\t* @return " + appname + "\n"
                + "\t*/\n"
                + "\tpublic " + appname + " find" + appname + "ById(" + idType + " id) {\n"
                + "\t\treturn this." + appname.toLowerCase() + "Repository.findById(id).orElse(null);\n"
                + "\t}"


                + "\n}";
        printWriter.printf(fileContent);

        printWriter.close();

        return fileContent;
    }

}
