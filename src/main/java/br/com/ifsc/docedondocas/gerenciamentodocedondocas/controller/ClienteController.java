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
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Cliente> novoCliente(@Valid @RequestBody Cliente cliente) {
        clienteRepository.save(cliente);
        URI location = URI.create("/cliente/"+cliente.getId());
        return ResponseEntity.created(location).body(cliente);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deletarUsuario(@PathVariable Long id){
        Cliente cliente = clienteRepository.findById(id);
        if(cliente == null) {
            return ResponseEntity.notFound().build();
        }
        clienteRepository.deleteById(id.toString());
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}/status", method = RequestMethod.PUT)
    public ResponseEntity<Cliente> alterarStatus(@PathVariable Long id){

        Cliente cliente = clienteRepository.findById(id);

        if(cliente == null) {
            return ResponseEntity.notFound().build();
        }

        cliente.setAtivo(!cliente.getAtivo());
        clienteRepository.save(cliente);

        return ResponseEntity.ok(cliente);
    }

    @RequestMapping(method = RequestMethod.PUT)
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

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity listar(){
        return ResponseEntity.ok(clienteRepository.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity listar(@PathVariable long id){
        Cliente cliente = clienteRepository.findById(id);
        if(cliente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }


}