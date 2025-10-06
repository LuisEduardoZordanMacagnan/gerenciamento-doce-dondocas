package br.com.ifsc.docedondocas.gerenciamentodocedondocas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "O nome não pode estar vazio")
    private String nome;

    @NotEmpty(message = "O cpf não pode estar vazio")
    @Column(unique = true)
    private String cpf;
}
