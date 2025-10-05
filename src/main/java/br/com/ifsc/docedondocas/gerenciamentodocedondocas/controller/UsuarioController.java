package br.com.ifsc.docedondocas.gerenciamentodocedondocas.controller;

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

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);
    private String root = "/usuario";

    @Autowired
    private UsuarioRepository u;

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioRepository usuarioRepository;

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

        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(token);
    }

    @PostMapping("/sair")
    public String sair(HttpServletResponse response) throws UnsupportedEncodingException {
        CookieService.setCookie(response, "usuarioId", "", 0);
        CookieService.setCookie(response, "usuarioNome", "", 0);
        return "redirect:"+root+"/login";
    } //REPLANEJAR ^^

    @PostMapping("/recuperar-senha")
    public String recuperarSenha(@RequestParam String cpf, Model model) {

        Usuario usuario = u.findByCpf(cpf);

        if (usuario != null) {
            if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
                model.addAttribute("erro", "Usuário não possui email cadastrado!");
                return "usuario/esqueci-senha";
            }

            try {
                String assunto = "Recuperação de Senha - Doce Don Docas";
                String mensagem = String.format(
                        "Olá %s!\n\nSua senha é: %s\n\nAtenciosamente,\nEquipe Doce Don Docas",
                        usuario.getNome(),
                        usuario.getSenha()
                );

                emailService.enviarEmail(usuario.getEmail(), assunto, mensagem);
                model.addAttribute("sucesso", "Senha enviada para: " + usuario.getEmail());
            } catch (Exception e) {
                model.addAttribute("erro", "Erro ao enviar email: " + e.getMessage());
            }
        } else {
            model.addAttribute("erro", "CPF não encontrado!");
        }

        return "usuario/esqueci-senha";
    }

    @RequestMapping(value = "/cadastro", method = RequestMethod.POST)
    public ResponseEntity cadastroUsuario(@Valid @RequestBody UsuarioDTO data) {
        String senhaEncriptada = new BCryptPasswordEncoder().encode(data.senha());
        Usuario usuario = new Usuario(data.nome(), data.cpf(), senhaEncriptada, data.email(), data.role());
        //EDITAR DEPOIS
        usuario.setRole(UsuarioRole.ADMIN);
        u.save(usuario);
        return ResponseEntity.ok(usuario);
    }

    @RequestMapping("/usuarios")
    public ResponseEntity listar(){
        return ResponseEntity.ok(u.findAll());
    }

    @RequestMapping("/usuarios/{id}")
    public ResponseEntity listar(@PathVariable long id){
        return ResponseEntity.ok(u.getUsuarioById(id));
    }

    @RequestMapping(value = "editar", method = RequestMethod.POST)
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

    @RequestMapping("/delete/{id}")
    public ResponseEntity deletarUsuario(@PathVariable Long id){
        u.deleteById(id.toString());
        return ResponseEntity.ok(true);
    }

    /*@GetMapping("/esqueci-senha")
    public String esqueciSenha() {
        return "usuario/esqueci-senha";
    }*/

}