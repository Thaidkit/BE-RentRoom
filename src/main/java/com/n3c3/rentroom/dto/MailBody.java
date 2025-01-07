package com.n3c3.rentroom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public record MailBody(String to, String subject, String text) {
}
