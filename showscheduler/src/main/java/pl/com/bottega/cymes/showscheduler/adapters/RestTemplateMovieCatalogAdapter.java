package pl.com.bottega.cymes.showscheduler.adapters;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerErrorException;
import pl.com.bottega.cymes.showscheduler.domain.Movie;
import pl.com.bottega.cymes.showscheduler.domain.MovieCatalog;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Primary
public class RestTemplateMovieCatalogAdapter implements MovieCatalog {

    private final RestTemplate moviesRestTemplate;

    @Override
    public Movie get(Long movieId) {
        var response = moviesRestTemplate.getForObject("/movies/{movieId}", GetMovieResponse.class, movieId);
        return new Movie(response.id, response.durationMinutes);
    }
}

@Component
@RequiredArgsConstructor
class WebClientMovieCatalogAdapter implements MovieCatalog {
    private final WebClient moviesClient;

    @Override
    public Movie get(Long movieId) {
        return moviesClient.get().uri(uriBuilder -> uriBuilder.path("/movies/{movieId}").build(movieId))
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return response.bodyToMono(GetMovieResponse.class);
                } else {
                    return Mono.error(new ServerErrorException("failed"));
                }
            })
            .map(response -> new Movie(response.id, response.durationMinutes))
            .block();
    }
}

@Data
class GetMovieResponse {
    Long id;
    Integer durationMinutes;
}
