package br.com.ifsc.docedondocas.gerenciamentodocedondocas.controller;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.Venda;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.service.VendaService;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.dto.VendaResumoDTO;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendas")
@CrossOrigin("*")
public class VendaController {

    private final VendaService service;

    public VendaController(VendaService service) {
        this.service = service;
    }

    // 🔹 LISTAR
    @GetMapping
    public List<Venda> listar() {
        return service.listar();
    }

    // 🔹 CADASTRAR
    @PostMapping
    public Venda salvar(@RequestBody Venda venda) {
        return service.salvar(venda);
    }

    // 🔹 DELETAR
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    // 🔹 RELATÓRIO SIMPLES
    @GetMapping("/resumo")
    public VendaResumoDTO resumo() {
        List<Venda> vendas = service.listar();

        double total = vendas.stream()
                .mapToDouble(Venda::getValor)
                .sum();

        long quantidade = vendas.size();

        return new VendaResumoDTO(total, quantidade);
    }
}