package com.volvo.demo.service.book;

import com.volvo.demo.book.*;
import com.volvo.demo.service.author.AuthorService;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class BookService extends BookServiceGrpc.BookServiceImplBase {
    final Logger log = LoggerFactory.getLogger(BookService.class);
    private final AtomicInteger bookIdCounter = new AtomicInteger(1);
    private final Map<Integer, Book> booksById = new HashMap<>();

    public void createBook(CreateBookRequest request, StreamObserver<Book> responseObserver) {
        log.info("BookService createBook called with {}:", request);
        int id = bookIdCounter.getAndIncrement();
        Book book = Book.newBuilder()
                .setId(id)
                .setTitle(request.getTitle())
                .setAuthorId(request.getAuthorId())
                .build();
        booksById.put(id, book);
        responseObserver.onNext(book);
        responseObserver.onCompleted();
    }

    public void getBook(GetBookRequest request, StreamObserver<Book> responseObserver) {
        log.info("BookService getBook called with {}:", request);
        responseObserver.onNext(booksById.get(request.getId()));
        responseObserver.onCompleted();
    }

    public void listBooks(ListBooksRequest request, StreamObserver<ListBooksResponse> responseObserver) {
        log.info("BookService listBooks called with {}:", request);
        ListBooksResponse.Builder builder = ListBooksResponse.newBuilder();
        request.getIdsList().forEach(id -> {
            builder.addBooks(booksById.get(id));
        });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}