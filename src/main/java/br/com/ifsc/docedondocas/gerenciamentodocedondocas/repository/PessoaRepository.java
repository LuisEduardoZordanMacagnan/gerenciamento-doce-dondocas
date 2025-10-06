package br.com.ifsc.docedondocas.gerenciamentodocedondocas.repository;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.Pessoa;
import org.springframework.data.repository.CrudRepository;

public interface PessoaRepository extends CrudRepository<Pessoa, String> {

}
