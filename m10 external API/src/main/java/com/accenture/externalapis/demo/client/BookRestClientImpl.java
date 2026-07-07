package com.accenture.externalapis.demo.client;

import com.accenture.externalapis.demo.config.ExternalServiceProperties;
import com.accenture.externalapis.demo.dto.BookApiResponse;
import com.accenture.externalapis.demo.dto.BookDto;
import com.accenture.externalapis.demo.mapper.BookMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class BookRestClientImpl implements BookRestClient {

    private RestClient restClient;
    private final BookMapper mapper;

    public BookRestClientImpl(RestClient.Builder builder, ExternalServiceProperties properties, BookMapper mapper) {
        this.mapper = mapper;
        this.restClient = builder
                .baseUrl(properties.baseUrl())
                .build();
    }

    @Override
    public BookDto getBook(Long id) {
        try {
            BookApiResponse response = restClient.get()
                    .uri("/books/{id}", id)
                    .retrieve()
                    .body(BookApiResponse.class);
            if (response == null) {
                throw new ProductNotFoundException("Book with id " + id + " not found");
            }
            return mapper.toDto(response);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ProductNotFoundException("Book with id " + id + " not found");
        } catch (HttpClientErrorException ex) {
            throw new ClientException("Client error: " + ex.getStatusCode(), ex);
        } catch (ResourceAccessException ex) {
            throw new ClientException("External service unreachable", ex);
        } catch (HttpServerErrorException ex) {
            throw new ClientException("External service error: " + ex.getStatusCode(), ex);
        } catch (RestClientException ex) {
            throw new ClientException("Unreadable response from external service ", ex);
        }
    }

    @Override
    public List<BookDto> getAllBooks() {
        try {
            BookApiResponse[] response = restClient.get()
                    .uri("/books")
                    .retrieve()
                    .body(BookApiResponse[].class);
            if (response == null) {
                return Collections.emptyList();
            }
            return Arrays.stream(response)
                    .map(mapper::toDto)
                    .toList();


        } catch (HttpClientErrorException.NotFound ex) {
            throw new ProductNotFoundException("No books found");
        } catch (HttpClientErrorException ex) {
            throw new ClientException("Client error: " + ex.getStatusCode(), ex);
        } catch (ResourceAccessException ex) {
            throw new ClientException("External service unreachable", ex);
        } catch (HttpServerErrorException ex) {
            throw new ClientException("External service error: " + ex.getStatusCode(), ex);
        } catch (RestClientException ex) {
            throw new ClientException("Unreadable response from external service ", ex);
        }
    }
}
