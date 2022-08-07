package com.gencode.Gencode.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class ObjectDto {

    @Size(min = 3)
    @NotNull
    @NotEmpty(message = "Name can not be empty!")
    private String name;

    @Size(min = 3)
    @NotNull
    @NotEmpty(message = "Id Type can not be empty!")
    private String idType;

    private String attributes;

    private String relations;

    private String domainContent;

    private String serviceContent;

    private String repositoryContent;

    private String controllerContent;

    private String dtoContent;

    private String mapperContent;

    public String toString() {
        return "name=" + name
                + ", idType=" + idType
                + ", attributes=" + attributes
                + ", relations=" + relations
                + ", domainContent=" + domainContent.substring(0, 5) + "..., "
                + ", serviceContent=" + serviceContent.substring(0, 5) + "..., "
                + ", repositoryContent=" + repositoryContent.substring(0, 5) + "..., "
                + ", controllerContent=" + controllerContent.substring(0, 5) + "..., "
                + ", dtoContent=" + dtoContent.substring(0, 5) + "..., "
                + ", mapperContent=" + mapperContent.substring(0, 5) + "...";
    }
}
