package br.com.ifsc.docedondocas.gerenciamentodocedondocas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotEmpty(message = "O título não pode ser fazio")
    private String titulo;
    @NotNull(message = "O valor não pode ser vazio")
    private double valor;
    @ManyToOne
    @JoinColumn(name = "marca_id")
    private ProdutoMarca marca;
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private ProdutoCategoria categoria;
}