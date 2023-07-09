package  org.example.service;

import org.example.domain.Nota;
import org.example.domain.Student;
import org.example.domain.Tema;
import org.example.repository.NotaXMLRepo;
import org.example.repository.StudentXMLRepo;
import org.example.repository.TemaXMLRepo;
import org.example.service.Service;
import org.example.validation.NotaValidator;
import org.example.validation.StudentValidator;
import org.example.validation.TemaValidator;
import org.example.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;

import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
public class IncrementalIntegrationTest {
    StudentValidator studentValidator = new StudentValidator();
    TemaValidator temaValidator = new TemaValidator();

    @Mock
    StudentXMLRepo studentRepo;
    @Mock
    TemaXMLRepo temaRepo;

    NotaValidator notaValidator;
    @Mock
    NotaXMLRepo noteRepo;

    Service service;

    @BeforeEach
    void init() {
        notaValidator = new NotaValidator(studentRepo, temaRepo);
        service = new Service(studentRepo, studentValidator, temaRepo, temaValidator, noteRepo, notaValidator);


        lenient().when(studentRepo.save(new Student("123", "Maria", 205, "test@email.com"))).thenReturn(null);
        lenient().when(temaRepo.save(new Tema("A5", "Description", 6, 4))).thenReturn(null);
        lenient().when(studentRepo.findOne("5")).thenReturn(new Student("5", "Ana", 200, "ana@email.com"));
        lenient().when(temaRepo.findOne("2")).thenReturn(new Tema("A5", "Description", 15, 4));
        lenient().when(noteRepo.save(new Nota("N1", "5", "2", 7, LocalDate.now()))).thenReturn(null);

    }


    @Test
    void saveStudent() {
        Assertions.assertNull(service.addStudent(new Student("123", "Maria", 205, "test@email.com")));
    }

    @Test
    void saveAssignment() {
        // Student
        Assertions.assertNull(service.addStudent(new Student("123", "Maria", 205, "test@email.com")));

        // Assignment
        Assertions.assertNull(service.addTema(new Tema("A5", "Description", 6, 4)));
    }

    @Test
    void saveGrade() {
        // Student
        Assertions.assertNull(service.addStudent(new Student("123", "Maria", 205, "test@email.com")));

        // Assignment
        Assertions.assertNull(service.addTema(new Tema("A5", "Description", 6, 4)));

        // Grade
        Assertions.assertThrows(ValidationException.class, () -> {
            service.addNota(new Nota("N1", "5","2", 7, LocalDate.now()), "Good");
        });
    }
}
