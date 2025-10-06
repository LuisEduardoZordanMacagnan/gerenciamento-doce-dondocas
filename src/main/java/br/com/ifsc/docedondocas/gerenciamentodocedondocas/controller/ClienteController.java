package br.com.ifsc.docedondocas.gerenciamentodocedondocas.controller;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.cliente.Cliente;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.usuario.Usuario;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.usuario.UsuarioDTO;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.usuario.UsuarioRole;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @RequestMapping(value = "/cadastro", method = RequestMethod.POST)
    public ResponseEntity<Cliente> novoCliente(@Valid @RequestBody Cliente cliente) {
        clienteRepository.save(cliente);
        return ResponseEntity.ok(cliente);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public ResponseEntity deletarUsuario(@PathVariable Long id){
        clienteRepository.deleteById(id.toString());
        return ResponseEntity.ok(true);
    }

    @RequestMapping(value = "editar", method = RequestMethod.POST)
    public ResponseEntity editarUsuario(@Valid @RequestBody Cliente data){
        Cliente cliente = clienteRepository.findById(data.getId());
        if(cliente == null) {
            return ResponseEntity.notFound().build();
        }
        if (data.getNome() != null) cliente.setNome(data.getNome());
        if (data.getCpf() != null) cliente.setCpf(data.getCpf());
        if (data.getTelefone() != null) cliente.setTelefone(data.getTelefone());
        if (data.getCidade() != null) cliente.setCidade(data.getCidade());
        if (data.getBairro() != null) cliente.setBairro(data.getBairro());
        if (data.getRua() != null) cliente.setRua(data.getRua());
        if (data.getNumeroCasa() != null) cliente.setNumeroCasa(data.getNumeroCasa());

        clienteRepository.save(cliente);
        return ResponseEntity.ok(cliente);
    }

    @RequestMapping(value = "/clientes", method = RequestMethod.POST)
    public ResponseEntity listar(){
        return ResponseEntity.ok(clienteRepository.findAll());
    }

    @RequestMapping(value = "/clientes/{id}", method = RequestMethod.POST)
    public ResponseEntity listar(@PathVariable long id){
        return ResponseEntity.ok(clienteRepository.findById(id));
    }


}
