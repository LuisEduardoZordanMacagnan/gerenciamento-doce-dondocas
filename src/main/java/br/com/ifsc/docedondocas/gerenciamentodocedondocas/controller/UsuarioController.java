package br.com.ifsc.docedondocas.gerenciamentodocedondocas.controller;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.Pessoa;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.usuario.RecuperacaoSenhaDDO;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.usuario.Usuario;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.usuario.UsuarioDTO;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.usuario.UsuarioRole;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.repository.UserDetailRepository;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.repository.UsuarioRepository;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.service.CookieService;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.service.EmailService;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.service.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.UnsupportedEncodingException;
import java.net.URI;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioRepository u;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    @PostMapping("/logar")
    public ResponseEntity login(@Valid @RequestBody UsuarioDTO data) throws UnsupportedEncodingException {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.cpf(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        //Usuario uLogin = u.login(usuario.getCpf(), usuario.getSenha());

        /*if (auth.isAuthenticated()) {
            CookieService.setCookie(response, "usuarioId", String.valueOf(uLogin.getId()), 10000);
            CookieService.setCookie(response, "usuarioNome", uLogin.getNome(), 10000);
            var token = tokenService.generateToken((Usuario) auth.getPrincipal());
            CookieService.setCookie(response, "token", token, 10000);
            return "redirect:"+root+"/lista";
        }*/

        Usuario usuario = (Usuario) auth.getPrincipal();

        if ( usuario == null || !usuario.isEnabled() ) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        var token = tokenService.generateToken(usuario);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/sair")
    public String sair(HttpServletResponse response) throws UnsupportedEncodingException {
        CookieService.setCookie(response, "usuarioId", "", 0);
        CookieService.setCookie(response, "usuarioNome", "", 0);
        return null;
    } //REPLANEJAR ^^

    @PostMapping("/recuperar-senha")
    public ResponseEntity recuperarSenhaEmail(@Valid @RequestParam String cpf) {

        Usuario usuario = u.findByCpf(cpf);

        if ( usuario == null ) return ResponseEntity.notFound().build();
        if ( usuario.getEmail() == null ) return ResponseEntity.badRequest().build();

        try {
            String assunto = "Recuperação de Senha - Doce Don Docas";
            String mensagem = tokenService.generateTokenForgotPassword((Usuario) usuario);

            emailService.enviarEmail(usuario.getEmail(), assunto, mensagem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/recuperar-senha/validar")
    public ResponseEntity recuperarSenha(@Valid @RequestBody RecuperacaoSenhaDDO data) {
        long id = tokenService.validateTokenForgotPassword(data.token());
        Usuario usuario = u.findById(id);
        if ( usuario == null ) return ResponseEntity.badRequest().build();
        usuario.setSenha(new BCryptPasswordEncoder().encode(data.novaSenha()));
        u.save(usuario);
        return ResponseEntity.ok(usuario);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity cadastroUsuario(@Valid @RequestBody UsuarioDTO data) {
        String senhaEncriptada = null;
        if ( !(data.senha() == null || data.senha().isEmpty()) ) senhaEncriptada = new BCryptPasswordEncoder().encode(data.senha());
        Usuario usuario = Usuario.builder()
                .nome(data.nome())
                .cpf(Pessoa.limpaCpf(data.cpf()))
                .senha(senhaEncriptada)
                .email(data.email())
                .role(data.role())
                .ativo(true)
                .build(); //new Usuario(data.nome(), data.cpf(), senhaEncriptada, data.email(), data.role());
        //EDITAR DEPOIS
        usuario.setRole(UsuarioRole.ADMIN);
        u.save(usuario);
        URI location = URI.create("/usuario/"+usuario.getId());
        return ResponseEntity.created(location).body(usuario);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity listar(){
        return ResponseEntity.ok(u.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity listar(@PathVariable long id){
        Usuario usuario = u.findById(id);
        if (usuario == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(usuario);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity editarUsuario(@Valid @RequestBody UsuarioDTO data){
        Usuario usuario = u.findById(data.id());
        if(usuario == null) {
            return ResponseEntity.notFound().build();
        }
        if (data.nome() != null) usuario.setNome(data.nome());
        if (data.cpf() != null) usuario.setCpf(data.cpf());
        if (data.senha() != null) usuario.setSenha(new BCryptPasswordEncoder().encode(data.senha()));
        if (data.email() != null) usuario.setEmail(data.email());
        //EDITAR DEPOIS
        usuario.setRole(UsuarioRole.ADMIN);

        u.save(usuario);
        return ResponseEntity.ok(usuario);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deletarUsuario(@PathVariable Long id){
        Usuario usuario = u.findById(id);
        if (usuario == null) return ResponseEntity.notFound().build();
        u.deleteById(id.toString());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("ativacao/{id}")
    public ResponseEntity ativacao(@PathVariable Long id, @RequestParam Boolean ativo){
        Usuario usuario = u.findById(id);
        if (usuario == null) return ResponseEntity.notFound().build();
        usuario.setAtivo(ativo);
        u.save(usuario);
        return ResponseEntity.ok(usuario);
    }

    /*@GetMapping("/esqueci-senha")
    public String esqueciSenha() {
        return "usuario/esqueci-senha";
    }*/

}