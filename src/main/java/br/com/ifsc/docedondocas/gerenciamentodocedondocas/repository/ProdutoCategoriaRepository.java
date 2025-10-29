package br.com.ifsc.docedondocas.gerenciamentodocedondocas.repository;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.ProdutoCategoria;
import org.springframework.data.repository.CrudRepository;

public interface ProdutoCategoriaRepository extends CrudRepository<ProdutoCategoria, String> {
    ProdutoCategoria findById(long id);

    void deleteById(long id);
}
