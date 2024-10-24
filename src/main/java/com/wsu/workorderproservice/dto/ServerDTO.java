package com.wsu.workorderproservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServerDTO {
    private Integer id;

    @NotBlank(message = "First Name must not be null or blank")
    @Size(max = 35)
    private String firstName; 

    @NotBlank(message = "First Name must not be null or blank")
    @Size(max = 35)
    private String lastName;

    @NotBlank(message = "First Name must not be null or blank")
    @Size(max = 10)
    private String availability;

}
