package pl.com.bottega.cymes.showscheduler.adapters;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerErrorException;
import pl.com.bottega.cymes.showscheduler.domain.Movie;
import pl.com.bottega.cymes.showscheduler.domain.MovieCatalog;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MovieCatalogAdapter implements MovieCatalog {

    private final RestTemplate moviesRestTemplate;

    @Override
    public Movie get(Long movieId) {
        var movieResponse = moviesRestTemplate.getForObject("/movies/{movieId}", GetMovieResponse.class, movieId);
        return movieResponse.toDomain();
    }
}

@Component
@RequiredArgsConstructor
class MovieCatalogWebClientAdapter implements MovieCatalog {

    private final WebClient moviesClient;

    @Override
    public Movie get(Long movieId) {
        return moviesClient
            .get()
            .uri(builder -> builder.path("/movies/{movieId}").build(movieId))
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return response.bodyToMono(GetMovieResponse.class);
                } else {
                    return Mono.error(new ServerErrorException("Failed to fetch movie"));
                }
            }).map(GetMovieResponse::toDomain).block();
    }
}

@Data
class GetMovieResponse {
    private Long id;
    private Integer durationMinutes;

    Movie toDomain() {
        return new Movie(id, durationMinutes);
    }
}