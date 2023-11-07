package com.example.springbootpratices;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM student s WHERE s.email = ?1")
    Optional<Student> findStudentByEmailUsingJPQL(String email);

    @Query(value = "SELECT * FROM student WHERE student.email = ?1", nativeQuery = true)
    Optional<Student> findStudentByEmail(String email);


    @Transactional
    @Modifying // Not need to map into Entity
    @Query("DELETE FROM student s where s.id = ?1")
    void deleteStudentById(Long id);
}
