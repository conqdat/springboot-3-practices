package com.base.projectbase.repository;

import com.base.projectbase.model.Category;
import com.base.projectbase.model.Student;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudentRepo extends MongoRepository<Student, String> {

    @Query(value = "{ 'age': { $gt: ?0, $lt: ?1 } }")
    List<Student> findByAgeBetween(Integer minAge, Integer maxAge);

    @Query(value = "{ 'birthday': { $gte: ?0, $lte: ?1 } }")
    List<Student> findByBirthdayBetween(LocalDate startDate, LocalDate endDate);
}
