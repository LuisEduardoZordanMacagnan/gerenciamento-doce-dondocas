package br.com.ifsc.docedondocas.gerenciamentodocedondocas.repository;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.Usuario;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, String> {

    Usuario findById(Long id);

    @Query(value = "SELECT * FROM docedondocas.usuario WHERE cpf = :cpf AND senha = :senha", nativeQuery = true)
    public Usuario login(String cpf, String senha);

    Usuario getUsuarioById(long id);

    long id(long id);
}
