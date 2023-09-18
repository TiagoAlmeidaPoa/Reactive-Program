package br.com.almeidatiago.webfluxcourse.model.request;

public record UserRequest(
    String name,
    String email,
    String password
) {}
