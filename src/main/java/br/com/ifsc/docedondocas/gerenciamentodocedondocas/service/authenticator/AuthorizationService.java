package br.com.ifsc.docedondocas.gerenciamentodocedondocas.service.authenticator;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.repository.UserDetailRepository;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UserDetailRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByCpf(username);
    }
}
