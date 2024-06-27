package com.base.projectbase.repository;

import com.base.projectbase.model.Category;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends MongoRepository<Category, String> {
    Category findCategoryById(ObjectId id);
}
