package com.base.projectbase.model;

import com.base.projectbase.config.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "students")
public class Student {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;

    private String name;
    private int age;

    private LocalDate birthday;
}
