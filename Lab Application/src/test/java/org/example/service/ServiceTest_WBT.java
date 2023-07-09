package org.example.service;

import org.example.domain.Student;
import org.example.domain.Tema;
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

class ServiceTest_WBT {

    StudentValidator studentValidator = new StudentValidator();
    TemaValidator temaValidator = new TemaValidator();

    StudentXMLRepo studentRepo = new StudentXMLRepo("fisiere/Studenti.xml");
    TemaXMLRepo temaRepo = new TemaXMLRepo("fisiere/Teme.xml");

    NotaValidator notaValidator = new NotaValidator(studentRepo, temaRepo);
    NotaXMLRepo noteRepo = new NotaXMLRepo("fisiere/Note.xml");

    Service service = new Service(studentRepo, studentValidator, temaRepo, temaValidator, noteRepo, notaValidator);


    @Test
    public void testAddAssignmentIdNull() {
        Assertions.assertThrows(ValidationException.class, () -> {
            service.addTema(new Tema(null, "Descrption1", 8, 6));
        });
    }

    @Test
    public void testAddAssignmentDescriptionEmpty() {
        Assertions.assertThrows(ValidationException.class, () -> {
            service.addTema(new Tema("A1", "", 8, 6));
        });
    }

    @Test
    public void testAddAssignmentDescriptionNull() {
        Assertions.assertThrows(ValidationException.class, () -> {
            service.addTema(new Tema("A1", null, 8, 6));
        });
    }

    @Test
    public void testAddAssignmentDeadline15() {
        Assertions.assertThrows(ValidationException.class, () -> {
            service.addTema(new Tema("A1", "Descrption1", 15, 5));
        });
    }

    @Test
    public void testAddAssignmentReceivingWeek0() {
        Assertions.assertThrows(ValidationException.class, () -> {
            service.addTema(new Tema("A1", "Descrption1", 7, 0));
        });
    }

    @Test
    public void testAddAssignmentIdUnique() {
        // Add successfully an assignment -> null will be returned - unique id
        Assertions.assertNull(service.addTema(new Tema("A5", "Description", 6, 4)));

        // Add already existing assignment -> assignment will be returned - duplicate id
        Assertions.assertEquals("A5", service.addTema(new Tema("A5", "Description", 6, 4)).getID());
        Assertions.assertNotNull(service.deleteTema("A5").getID());
    }

}