package br.com.ifsc.docedondocas.gerenciamentodocedondocas.controller;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.usuario.Usuario;
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

    @GetMapping("/login")
    public String login() {
        return "usuario/login";
    }

    @PostMapping("/logar")
    public String login(Usuario usuario, Model model, HttpServletResponse response) throws UnsupportedEncodingException {
        var usernamePassword = new UsernamePasswordAuthenticationToken(usuario.getCpf(), usuario.getSenha());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        //Usuario uLogin = u.login(usuario.getCpf(), usuario.getSenha());

        if (auth.isAuthenticated()) {
            /*CookieService.setCookie(response, "usuarioId", String.valueOf(uLogin.getId()), 10000);
            CookieService.setCookie(response, "usuarioNome", uLogin.getNome(), 10000);*/
            var token = tokenService.generateToken((Usuario) auth.getPrincipal());
            CookieService.setCookie(response, "token", token, 10000);
            return "redirect:"+root+"/lista";
        }

        model.addAttribute("erro", "Usuário Invalido!");
        return "usuario/login";
    }

    @PostMapping("/sair")
    public String sair(HttpServletResponse response) throws UnsupportedEncodingException {
        CookieService.setCookie(response, "usuarioId", "", 0);
        CookieService.setCookie(response, "usuarioNome", "", 0);
        return "redirect:"+root+"/login";
    }

    @GetMapping("/cadastro")
    public String cadastro(){
        return "usuario/cadastro";
    }

    @RequestMapping(value = "/cadastro", method = RequestMethod.POST)
    public String cadastroUsuario(@Valid Usuario usuario, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("erro", "Erro");
            return "usuario/cadastro";
        }
        String senhaEncriptada = new BCryptPasswordEncoder().encode(usuario.getSenha());
        usuario.setSenha(senhaEncriptada);
        //EDITAR DEPOIS
        usuario.setRole(UsuarioRole.ADMIN);
        u.save(usuario);
        return "redirect:"+root+"/lista";
    }

    @GetMapping("/lista")
    public String listar(Model model){
        model.addAttribute("usuarios", u.findAll());
        return "usuario/lista";
    }

    @GetMapping("/cadastro/{id}")
    public String editar(@PathVariable Long id, Model model){
        model.addAttribute("usuario", u.getUsuarioById(id));
        return "usuario/cadastro";
    }

    @RequestMapping(value = "/cadastro/{id}", method = RequestMethod.POST)
    public String editarUsuario(@Valid Usuario us, BindingResult result, Model model){
        Usuario usuario = u.findById(us.getId());
        if(usuario == null) {
            model.addAttribute("erro", "Erro");
            return "usuario/cadastro";
        }
        usuario.setNome(us.getNome());
        usuario.setCpf(us.getCpf());
        if (us.getSenha() != null) {
            usuario.setSenha(new BCryptPasswordEncoder().encode(us.getSenha()));
        }
        usuario.setEmail(us.getEmail());
        //EDITAR DEPOIS
        usuario.setRole(UsuarioRole.ADMIN);

        u.save(usuario);
        return "redirect:"+root+"/lista";
    }

    @GetMapping("/delete/{id}")
    public String deletar(@PathVariable Long id){
        u.deleteById(id.toString());
        return "redirect:"+root+"/lista";
    }

    @GetMapping("/esqueci-senha")
    public String esqueciSenha() {
        return "usuario/esqueci-senha";
    }

}