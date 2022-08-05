package com.gencode.Gencode.dto;

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

    private String serviceContent;

    private String repositoryContent;

    @Size(min = 3)
    @NotNull
    @NotEmpty(message = "Id Type can not be empty!")
    private String idType;

}
