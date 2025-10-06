package br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.usuario;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.Pessoa;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Usuario extends Pessoa implements UserDetails {
    @NotEmpty(message = "A senha não pode estar vazia")
    private String senha;

    @NotEmpty(message = "O email não pode estar vazio")
    private String email;

    //@NotEmpty
    private UsuarioRole role;

    public Collection<? extends GrantedAuthority> getAuthorities(){
        if (this.role == UsuarioRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.getCpf();
    }

    /*public Usuario(String nome, String cpf, String senha, String email, UsuarioRole role) {
        super(nome, cpf);
        this.senha = senha;
        this.email = email;
        this.role = role;
    }*/
}