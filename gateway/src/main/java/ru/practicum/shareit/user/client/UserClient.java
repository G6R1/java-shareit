package ru.practicum.shareit.user.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserClient {
    private final WebClient client;
    private final String serverUri;


    public UserClient(@Value("${shareit-server.url}") String serverUri) {
        this.client = WebClient.create();
        this.serverUri = serverUri;
    }

    public UserDto getUser(Long userId) {
        return client.get()
                .uri(serverUri + "/users/" + userId)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    public List<UserDto> getAllUser() {
        return client.get()
                .uri(serverUri + "/users")
                .retrieve()
                .bodyToFlux(UserDto.class)
                .collect(Collectors.toList())
                .block();
    }

    public UserDto createUser(UserDto userDto) {
        return client.post()
                .uri(serverUri + "/users")
                .body(Mono.just(userDto), UserDto.class)
                .retrieve()
                //.onStatus(HttpStatus::isError, response -> response.bodyToMono(String.class).flatMap(error -> Mono.error(new WebClientResponseException(error))))
                .bodyToMono(UserDto.class)
                .block();
    }

    public UserDto patchUser(Long requestorId, UserDto userDto) {
        return client.patch()
                .uri(serverUri + "/users/" + requestorId)
                .body(Mono.just(userDto), UserDto.class)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    public void deleteUser(Long userId) {
        client.delete()
                .uri(serverUri + "/users/" + userId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

}
