package com.hse.course;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hse.course.model.Interest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Type;
import java.util.HashSet;

@SpringBootApplication
public class CourseWorkApplication {

    public static void main(String[] args) {
		SpringApplication.run(CourseWorkApplication.class, args);

    }

}
