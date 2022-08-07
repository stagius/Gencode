package com.gencode.Gencode.utils;

import lombok.Data;
import org.springframework.stereotype.Service;

import javax.persistence.OneToMany;
import java.util.*;

@Service
@Data
public class Utils {

    public String formatFistLetterCapital(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public Map<String, String> formatAttributes(String attributes) {
        // ex: message=String,age=int etc.
        Map<String, String> result = new HashMap<>();
        List<String> tempList = Arrays.asList(attributes.replaceAll(" ", "").split(","));

        tempList.forEach(value -> {
            result.put(value.split("=")[0], value.split("=")[1]);
        });
        return result;
    }

    public List<String> formatRelations(String relations) {
        // input example : Activite->responsable;@OneToMany|Utilisateur->responsable;@ManyToOne(cascade = CascadeType.PERSIST)
        List<String> result = new ArrayList<>();

        // example :
        //    0:  Activite->responsable;@OneToMany
        //    1:  Utilisateur->responsable;@ManyToOne(cascade = CascadeType.PERSIST)
        List<String> pipeSplit = Arrays.asList(relations.split("\\|"));

        pipeSplit.forEach(pipeElem -> {

            // example :
            // 0:   Activite->responsable
            // 1:   @OneToMany
            //  and
            // 0:   Utilisateur->responsable
            // 1:   @ManyToOne(cascade = CascadeType.PERSIST)
            List<String> separatorSplit = Arrays.asList(pipeElem.split(";"));

            String obj = separatorSplit.get(0).split("->")[0];
            String attribute = separatorSplit.get(0).split("->")[1];


            result.add(separatorSplit.get(1) + ":" + obj + ":" + attribute);
        });

        // RESULT :
        // 0:   @OneToMany:Activite:responsable
        // 1:   @ManyToOne(cascade = CascadeType.PERSIST):Utilisateur:responsable

        return result;
    }

    public String checkPlural(String serviceName) {
        if (serviceName.endsWith("y")) {
            return serviceName.substring(0, serviceName.length()-1) + (serviceName.substring(serviceName.length() - 1).replaceAll("y", "ies"));
        } else {
            return serviceName + "s";
        }
    }
}
