package br.com.ifsc.docedondocas.gerenciamentodocedondocas.controller;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.Usuario;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.repository.UsuarioRepository;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.service.CookieService;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.service.EmailService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    private String root = "/usuario";

    @Autowired
    private UsuarioRepository u;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/login")
    public String login() {
        return "usuario/login";
    }

    @PostMapping("/logar")
    public String login(Usuario usuario, Model model, HttpServletResponse response) throws UnsupportedEncodingException {
        Usuario uLogin = u.login(usuario.getCpf(), usuario.getSenha());
        if (uLogin != null) {
            CookieService.setCookie(response, "usuarioId", String.valueOf(uLogin.getId()), 10000);
            CookieService.setCookie(response, "usuarioNome", uLogin.getNome(), 10000);
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

    @GetMapping("/cadastro")
    public String cadastro(){
        return "usuario/cadastro";
    }

    @RequestMapping(value = "/cadastro", method = RequestMethod.POST)
    public String cadastroUsuario(@Valid Usuario usuario, BindingResult result, Model model) {
        if(result.hasErrors()){
            model.addAttribute("erro", "Erro");
            return "usuario/cadastro";
        }
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
    public String editarUsuario(@Valid Usuario usuario, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("erro", "Erro");
            return "usuario/cadastro";
        }
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