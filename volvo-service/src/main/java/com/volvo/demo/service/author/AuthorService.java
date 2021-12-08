package com.volvo.demo.service.author;

import com.volvo.demo.author.*;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AuthorService extends AuthorServiceGrpc.AuthorServiceImplBase {
    final Logger log = LoggerFactory.getLogger(AuthorService.class);
    private final AtomicInteger authorIdCounter = new AtomicInteger(1);
    private final Map<Integer, Author> authorsById = new HashMap<>();

    public void createAuthor(CreateAuthorRequest request, StreamObserver<Author> responseObserver) {
        log.info("AuthorService createAuthor called with {}:", request);
        int id = authorIdCounter.getAndIncrement();
        Author author = Author.newBuilder()
                .setId(id)
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .build();
        authorsById.put(id, author);
        responseObserver.onNext(author);
        responseObserver.onCompleted();
    }

    public void getAuthor(GetAuthorRequest request, StreamObserver<Author> responseObserver) {
        log.info("AuthorService getAuthor called with {}:", request);
        responseObserver.onNext(authorsById.get(request.getId()));
        responseObserver.onCompleted();
    }

    public void addBook(AddBookRequest request, StreamObserver<Author> responseObserver) {
        log.info("AuthorService addBook called with {}:", request);
        int authorId = request.getAuthorId();
        Author author = authorsById.get(authorId).toBuilder()
                .addBookIds(request.getBookId())
                .build();
        authorsById.put(authorId, author);
        responseObserver.onNext(author);
        responseObserver.onCompleted();
    }
}