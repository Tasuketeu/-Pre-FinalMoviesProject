package com.company.base.accenture.movies;

import com.company.base.accenture.movies.BL.MovieServiceImpl;
import com.company.base.accenture.movies.BL.UserServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
public class MoviesApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MoviesApplication.class, args);
        //new Main().configure(new SpringApplicationBuilder(Main.class)).run(args);
        ApplicationContext context =
                new AnnotationConfigApplicationContext("com.company.base.accenture.movies");

        MovieServiceImpl containMovies= context.getBean(MovieServiceImpl.class);
        UserServiceImpl containUsers = context.getBean(UserServiceImpl.class);

        containUsers.registerUsers("admin", "admin", "admin", "true");

        containMovies.getMoviesFromDB();

        System.out.println("Вы не в системе");

        Scanner sc = new Scanner(System.in);

        System.out.println("Введите команду:\n" +
                "exit -- выход из программы");

        String commands = sc.next();

        while (!(commands.equals("exit"))) {
            commands = sc.next();
        }
    }
}
