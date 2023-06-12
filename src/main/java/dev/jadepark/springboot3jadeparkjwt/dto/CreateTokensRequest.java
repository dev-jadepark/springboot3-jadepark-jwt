package dev.jadepark.springboot3jadeparkjwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTokensRequest {

    private String email;
    private String password;
}
