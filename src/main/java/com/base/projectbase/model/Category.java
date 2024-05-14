package com.base.projectbase.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.base.projectbase.config.ObjectIdSerializer;

@Data
@Document(collection = "categories")
public class Category {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;
    private String name;

    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + name + "\"" +
                '}';
    }
}
