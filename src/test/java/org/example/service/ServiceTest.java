package org.example.service;

import org.example.domain.Student;
import org.example.repository.NotaXMLRepo;
import org.example.repository.StudentXMLRepo;
import org.example.repository.TemaXMLRepo;
import org.example.validation.NotaValidator;
import org.example.validation.StudentValidator;
import org.example.validation.TemaValidator;
import org.example.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    StudentValidator studentValidator = new StudentValidator();
    TemaValidator temaValidator = new TemaValidator();

    StudentXMLRepo studentRepo = new StudentXMLRepo("fisiere/Studenti.xml");
    TemaXMLRepo temaRepo = new TemaXMLRepo("fisiere/Teme.xml");

    NotaValidator notaValidator = new NotaValidator(studentRepo, temaRepo);
    NotaXMLRepo noteRepo = new NotaXMLRepo("fisiere/Note.xml");

    Service service = new Service(studentRepo, studentValidator, temaRepo, temaValidator, noteRepo, notaValidator);

    @Test
    public void testAddStudentIdUnique() {
        // Add successfully a student -> null will be returned - unique id
        Assertions.assertNull(service.addStudent(new Student("123", "Test", 205, "test@email.com")));

        // Add already existing student -> student will be returned - duplicate id
        Assertions.assertEquals("123" ,service.addStudent(new Student("123", "Test", 205, "test@email.com")).getID());
        Assertions.assertNotNull(service.deleteStudent("123").getID());
    }

    @Test
    public void testAddStudentIdNull(){
        // Add a student with a null id - validation exception is thrown
        Assertions.assertThrows(ValidationException.class, () -> {
            service.addStudent(new Student(null, "Test", 205, "test@email.com"));
        });
    }
}