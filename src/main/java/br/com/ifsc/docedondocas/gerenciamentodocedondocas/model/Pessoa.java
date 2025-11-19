package br.com.ifsc.docedondocas.gerenciamentodocedondocas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
    @Size(min = 11, max = 11, message = "O CPF deve ter exatamente 11 digitos")
    private String cpf;

    static public String limpaCpf(String cpf){
        if (cpf == null) return null;
        return cpf.replaceAll("\\D", "").trim();
    }
}
