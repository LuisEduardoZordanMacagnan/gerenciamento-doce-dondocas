package br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.usuario;

import jakarta.validation.constraints.NotEmpty;

public record UsuarioDTO(
        long id,
        String nome,
        String cpf,
        String senha,
        String email,
        UsuarioRole role) {
}
