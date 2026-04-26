package br.com.ifsc.docedondocas.gerenciamentodocedondocas.dto;

public class VendaResumoDTO {

    private Double total;
    private Long quantidade;

    public VendaResumoDTO(Double total, Long quantidade) {
        this.total = total;
        this.quantidade = quantidade;
    }

    public Double getTotal() { return total; }
    public Long getQuantidade() { return quantidade; }
}