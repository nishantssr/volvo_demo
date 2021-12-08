package com.volvo.demo;

import com.volvo.demo.service.author.AuthorService;
import com.volvo.demo.service.book.BookService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class VolvoDemoApplication {

	public static void main(String[] args) throws IOException, InterruptedException {
		Server authorServer = ServerBuilder
				.forPort(9091)
				.addService(new AuthorService())
				.build();

		Server bookServer = ServerBuilder
				.forPort(9092)
				.addService(new BookService())
				.build();

		authorServer.start();
		bookServer.start();
		System.out.println("gRPC Author Server started, listening on port:" + authorServer.getPort());
		System.out.println("gRPC Book Server started, listening on port:" + bookServer.getPort());
		authorServer.awaitTermination();
		bookServer.awaitTermination();
		SpringApplication.run(VolvoDemoApplication.class, args);
	}

}
