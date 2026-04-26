package br.com.ifsc.docedondocas.gerenciamentodocedondocas.service;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.Venda;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.repository.VendaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendaService {

    private final VendaRepository repository;

    public VendaService(VendaRepository repository) {
        this.repository = repository;
    }

    public List<Venda> listar() {
        return repository.findAll();
    }

    public Venda salvar(Venda venda) {
        return repository.save(venda);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}