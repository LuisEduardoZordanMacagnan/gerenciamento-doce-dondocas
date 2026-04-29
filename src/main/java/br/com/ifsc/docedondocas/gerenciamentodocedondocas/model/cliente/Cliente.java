package br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.cliente;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.Pessoa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Cliente extends Pessoa {
    @NotEmpty(message = "O telefone não pode estar vazio")
    private String telefone;

    @NotEmpty(message = "A cidade não pode estar vazia")
    private String cidade;

    @NotEmpty(message = "O bairro não pode estar vazio")
    private String bairro;

    @NotEmpty(message = "A rua não pode estar vazia")
    private String rua;

    @NotEmpty(message = "O número da casa não pode estar vazio")
    private String numeroCasa;

    private Boolean ativo;

    @PrePersist
    protected void onCreate() {
        if (ativo == null) {
            ativo = true;
        }
    }
}
