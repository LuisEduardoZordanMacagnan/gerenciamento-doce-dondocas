package br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.usuario;

import jakarta.validation.constraints.NotEmpty;

public record RecuperacaoSenhaDDO(
        @NotEmpty String token,
        @NotEmpty String novaSenha
) {
}
