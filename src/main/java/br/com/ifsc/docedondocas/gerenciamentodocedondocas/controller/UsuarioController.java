package br.com.ifsc.docedondocas.gerenciamentodocedondocas.controller;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.Usuario;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.repository.UsuarioRepository;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.service.CookieService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    private String root = "/usuario";

    @Autowired
    private UsuarioRepository u;

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
}
