package com.accenture.externalapis.demo.client;

import com.accenture.externalapis.demo.config.ExternalServiceProperties;
import com.accenture.externalapis.demo.dto.BookApiResponse;
import com.accenture.externalapis.demo.dto.BookDto;
import com.accenture.externalapis.demo.mapper.BookMapper;
import org.springframework.core.codec.DecodingException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class BookWebClientImpl implements BookWebClient {

    private WebClient webClient;
    private final BookMapper mapper;

    public BookWebClientImpl(WebClient.Builder builder, ExternalServiceProperties properties, BookMapper mapper) {
        this.mapper = mapper;
        this.webClient = builder
                .baseUrl(properties.baseUrl())
                .build();
    }

    @Override
    public Mono<BookDto> getBookAsync(Long id) {
        return webClient.get()
                .uri("/books/{id}", id)
                .retrieve()
                .bodyToMono(BookApiResponse.class)
                .map(mapper::toDto)
                .switchIfEmpty(Mono.error(
                        new ProductNotFoundException("Book with id " + id + " not found")))
                .onErrorMap(WebClientResponseException.NotFound.class,
                        ex -> new ProductNotFoundException("Book with id " + id + " not found"))
                .onErrorMap(WebClientResponseException.class, ex ->
                        ex.getStatusCode().is2xxSuccessful()
                                ? new ClientException("Unreadable response from external service", ex)
                                : new ClientException("External service error:" + ex.getStatusCode(), ex))
                .onErrorMap(WebClientRequestException.class,
                        ex -> new ClientException("External service unreachable", ex))
                .onErrorMap(DecodingException.class,
                        ex -> new ClientException("Unreadable response from external service", ex));
    }

    @Override
    public Flux<BookDto> getAllBooksAsync() {
        return webClient.get()
                .uri("/books")
                .retrieve()
                .bodyToFlux(BookApiResponse.class)
                .map(mapper::toDto)
                .onErrorMap(WebClientResponseException.NotFound.class,
                        ex -> new ProductNotFoundException("No books found"))
                .onErrorMap(WebClientResponseException.class,
                        ex -> new ClientException("External service error:" + ex.getStatusCode(), ex))
                .onErrorMap(WebClientRequestException.class,
                        ex -> new ClientException("External service unreachable", ex))
                .onErrorMap(DecodingException.class,
                        ex -> new ClientException("Unreadable response from external service", ex));
    }

    @Override
    public Mono<List<BookDto>> getBooksInParallel(Long id1, Long id2) {
        Mono<BookDto> book1 = getBookAsync(id1);
        Mono<BookDto> book2 = getBookAsync(id2);
        return Mono.zip(book1, book2, List::of);
    }
}
