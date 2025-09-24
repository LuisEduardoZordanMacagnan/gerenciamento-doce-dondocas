package br.com.ifsc.docedondocas.gerenciamentodocedondocas.repository;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailRepository extends JpaRepository<Usuario, Long> {
    UserDetails findByCpf(String cpf);
}
