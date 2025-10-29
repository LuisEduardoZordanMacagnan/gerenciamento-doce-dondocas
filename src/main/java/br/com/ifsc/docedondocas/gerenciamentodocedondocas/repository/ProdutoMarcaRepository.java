package br.com.ifsc.docedondocas.gerenciamentodocedondocas.repository;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.ProdutoMarca;
import org.springframework.data.repository.CrudRepository;

public interface ProdutoMarcaRepository extends CrudRepository<ProdutoMarca, Long> {
    ProdutoMarca findById(long id);
}
