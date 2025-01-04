package br.com.alura.challenges.forum.hub.models.requests;

import br.com.alura.challenges.forum.hub.models.entities.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.password.PasswordEncoder;

public record RegisterUserRequest(
        @NotNull(message = "O campo nome é obrigatório")
        @Size(min = 3, max = 128, message = "O campo nome deve ter entre 3 à 128 caracteres")
        String name,

        @NotNull(message = "O campo email é obrigatório")
        @Size(min = 4, max = 255, message = "O campo email deve ter entre 4 à 255 caracteres")
        String email,

        @NotNull(message = "campo senha é obrigatório")
        @Size(min = 6, message = "O campo senha não pode ser menor do que 6 caracteres")
        String password
) {

    public User parseToEntity(PasswordEncoder passwordEncoder) {
        return new User(
                name,
                email,
                passwordEncoder.encode(password)
        );
    }

}
