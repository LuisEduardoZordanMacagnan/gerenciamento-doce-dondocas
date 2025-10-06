package br.com.ifsc.docedondocas.gerenciamentodocedondocas.repository;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.cliente.Cliente;
import org.springframework.data.repository.CrudRepository;

public interface ClienteRepository extends CrudRepository<Cliente, String> {
    //Cliente findByCpf(String cpf);
    Cliente findById(long id);
}
