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
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProdutoMarca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Marca não pode ser vazia")
    @Size(min = 2, message = "Marca precisa ser maior que 2 caracteres")
    private String marca;
    private Boolean ativo;

    @PrePersist
    protected void onCreate() {
        if (ativo == null) {
            ativo = true;
        }
    }
}
