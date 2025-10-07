package br.com.ifsc.docedondocas.gerenciamentodocedondocas.dto;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {
}
