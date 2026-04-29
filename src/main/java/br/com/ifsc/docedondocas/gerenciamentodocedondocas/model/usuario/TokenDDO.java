package br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.usuario;

import jakarta.validation.constraints.NotEmpty;

public record TokenDDO (
        @NotEmpty String token
){
}
