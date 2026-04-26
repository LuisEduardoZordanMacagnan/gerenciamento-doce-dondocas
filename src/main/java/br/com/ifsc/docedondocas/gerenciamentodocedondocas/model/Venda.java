package br.com.ifsc.docedondocas.gerenciamentodocedondocas.model;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.cliente.Cliente;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

/**
 * Modelo de Venda
 * Representa uma transação de venda no sistema
 */
@Entity
@Table(name = "vendas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Data da venda é obrigatória")
    @Column(nullable = false)
    private LocalDateTime data;

    @NotNull(message = "Cliente é obrigatório")
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull(message = "Produto é obrigatório")
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @NotNull(message = "Quantidade é obrigatória")
    @Column(nullable = false)
    private Integer quantidade;

    @NotNull(message = "Valor unitário é obrigatório")
    @Column(nullable = false)
    private Double valorUnitario;

    @NotNull(message = "Valor total é obrigatório")
    @Column(nullable = false)
    private Double valor;

    @Column(length = 500)
    private String observacoes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }

    /**
     * Validar dados da venda antes de salvar
     */
    @PrePersist
    protected void onCreate() {
        if (data == null) {
            data = LocalDateTime.now();
        }
        criadoEm = LocalDateTime.now();
        atualizadoEm = LocalDateTime.now();

        // Validar que valor = quantidade * valorUnitario
        if (quantidade != null && valorUnitario != null) {
            valor = quantidade * valorUnitario;
        }
    }

    /**
     * Calcular valor total da venda
     */
    public void calcularValorTotal() {
        if (quantidade != null && valorUnitario != null) {
            this.valor = quantidade * valorUnitario;
        }
    }

    /**
     * Verificar se a venda é válida
     */
    public boolean isValida() {
        return cliente != null &&
               produto != null &&
               quantidade != null &&
               quantidade > 0 &&
               valorUnitario != null &&
               valorUnitario > 0 &&
               valor != null &&
               valor > 0;
    }
}
