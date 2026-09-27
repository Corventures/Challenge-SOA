package br.com.blindspot.api.dto.response;

import java.util.List;

public record UserInfoResponseDTO(String username, List<String> authorities) {
}