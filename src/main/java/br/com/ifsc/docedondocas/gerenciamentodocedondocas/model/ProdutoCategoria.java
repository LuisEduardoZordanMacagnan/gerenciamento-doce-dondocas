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
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProdutoCategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotEmpty(message = "Categoria não pode ser vazia")
    @Size(min = 2, message = "Categoria precisa ser maior que 2 caracteres")
    private String categoria;
    private Boolean ativo;

    @PrePersist
    protected void onCreate() {
        if (ativo == null) {
            ativo = true;
        }
    }
}
