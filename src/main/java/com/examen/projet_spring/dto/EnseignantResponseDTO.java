package com.examen.projet_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnseignantResponseDTO {

    private Long id;

    private String specialite;

    private String fullname;

    private String email;
}