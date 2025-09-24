package br.com.ifsc.docedondocas.gerenciamentodocedondocas.repository;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.usuario.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends CrudRepository<Usuario, String> {

    Usuario findById(Long id);

    @Query(value = "SELECT * FROM docedondocas.usuario WHERE cpf = :cpf AND senha = :senha", nativeQuery = true)
    public Usuario login(String cpf, String senha);

    @Query(value = "SELECT * FROM docedondocas.usuario WHERE cpf = :cpf", nativeQuery = true)
    public Usuario findByCpf(String cpf);

    Usuario getUsuarioById(long id);

    long id(long id);

    @Query(value = "SELECT * FROM docedondocas.usuario WHERE id = :id", nativeQuery = true)
    public UserDetails userDetailsFindById(String cpf);
}