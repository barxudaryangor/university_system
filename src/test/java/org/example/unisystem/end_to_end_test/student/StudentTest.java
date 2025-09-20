package org.example.unisystem.end_to_end_test.student;

import org.example.unisystem.dto.course.CourseCreateDTO;
import org.example.unisystem.dto.student.StudentCreateDTO;
import org.example.unisystem.dto.student.StudentPatchDTO;
import org.example.unisystem.dto.student.StudentUpdateDTO;
import org.example.unisystem.end_to_end_test.container.ContainerConfig;
import org.example.unisystem.dto.student.StudentDTO;
import org.example.unisystem.enums.Gender;
import org.example.unisystem.jpa_repo.CourseJpaRepository;
import org.example.unisystem.jpa_repo.StudentJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@Import(ContainerConfig.class)
public class StudentTest {
    @Autowired
    WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    StudentJpaRepository studentJpaRepository;

    @Autowired
    CourseJpaRepository courseJpaRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        studentJpaRepository.deleteAll();
        courseJpaRepository.deleteAll();
        jdbcTemplate.execute("ALTER SEQUENCE student_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE course_id_seq RESTART WITH 1");
    }

    @Test
    void getStudentById() throws Exception {
        StudentCreateDTO request = new StudentCreateDTO("Ani", "Hakobyan", Gender.FEMALE,
                LocalDate.of(2004, 5, 10), LocalDate.of(2021, 9, 1), "ani@example.com", null, null);

        String requestBody = objectMapper.writeValueAsString(request);

        String response = mockMvc.perform(post("/uni/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andReturn().getResponse().getContentAsString();

        StudentDTO created = objectMapper.readValue(response, StudentDTO.class);

        mockMvc.perform(get("/uni/students/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.getId()))
                .andExpect(jsonPath("$.name").value(created.getName()))
                .andExpect(jsonPath("$.surname").value(created.getSurname()))
                .andExpect(jsonPath("$.sex").value(created.getSex().toString()))
                .andExpect(jsonPath("$.birthdate").value(created.getBirthdate().toString()))
                .andExpect(jsonPath("$.enrolmentDate").value(created.getEnrolmentDate().toString()))
                .andExpect(jsonPath("$.email").value(created.getEmail()));
    }

    @Test
    void getAllStudents() throws Exception {
        StudentCreateDTO request = new StudentCreateDTO("Ani", "Hakobyan", Gender.FEMALE,
                LocalDate.of(2004, 5, 10),
                LocalDate.of(2021, 9, 1), "ani@example.com",
                null, null);

        StudentCreateDTO request2 = new StudentCreateDTO(
                "Gor", "Barxudaryan", Gender.MALE,
                LocalDate.of(2005,2,22),
                LocalDate.now(),"gor.barxudaryan.2014@gmail.com", null, null
        );

        String requestBody = objectMapper.writeValueAsString(request);
        String request2Body = objectMapper.writeValueAsString(request2);

        String response = mockMvc.perform(post("/uni/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andReturn().getResponse().getContentAsString();

        String response2 = mockMvc.perform(post("/uni/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request2Body))
                .andReturn().getResponse().getContentAsString();

        StudentDTO student = objectMapper.readValue(response, StudentDTO.class);
        StudentDTO student2 = objectMapper.readValue(response2, StudentDTO.class);

        mockMvc.perform(get("/uni/students?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(student.getId()))
                .andExpect(jsonPath("$.content[0].name").value(student.getName()))
                .andExpect(jsonPath("$.content[0].surname").value(student.getSurname()))
                .andExpect(jsonPath("$.content[0].sex").value(student.getSex().toString()))
                .andExpect(jsonPath("$.content[0].birthdate").value(student.getBirthdate().toString()))
                .andExpect(jsonPath("$.content[0].enrolmentDate").value(student.getEnrolmentDate().toString()))
                .andExpect(jsonPath("$.content[0].email").value(student.getEmail()))
                .andExpect(jsonPath("$.content[1].id").value(student2.getId()))
                .andExpect(jsonPath("$.content[1].name").value(student2.getName()))
                .andExpect(jsonPath("$.content[1].surname").value(student2.getSurname()))
                .andExpect(jsonPath("$.content[1].sex").value(student2.getSex().toString()))
                .andExpect(jsonPath("$.content[1].birthdate").value(student2.getBirthdate().toString()))
                .andExpect(jsonPath("$.content[1].enrolmentDate").value(student2.getEnrolmentDate().toString()))
                .andExpect(jsonPath("$.content[1].email").value(student2.getEmail())).andExpect(jsonPath("$.pageNum").value(0))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.pageNum").value(0));
    }

    @Test
    void createStudent() throws Exception {
        StudentCreateDTO request = new StudentCreateDTO(
                "Gor", "Barxudaryan", Gender.MALE,
                LocalDate.of(2005,2,22),
                LocalDate.now(),"gor.barxudaryan.2014@gmail.com", null, null
        );

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/uni/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(result -> {
                    System.out.println("==== RESPONSE BODY ====");
                    System.out.println(result.getResponse().getContentAsString());
                    System.out.println("=======================");
                })
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Gor"))
                .andExpect(jsonPath("$.surname").value("Barxudaryan"));

    }

    @Test
    void addStudentToCourse() throws Exception {
        StudentCreateDTO request = new StudentCreateDTO(
                "Gor", "Barxudaryan", Gender.MALE,
                LocalDate.of(2005,2,22),
                LocalDate.now(),"gor.barxudaryan.2014@gmail.com", null, null
        );

        String requestBody = objectMapper.writeValueAsString(request);

        CourseCreateDTO request2 = new CourseCreateDTO(
                "title", 10, null, null, null
        );

        String requestBody2 = objectMapper.writeValueAsString(request2);

        mockMvc.perform(post("/uni/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        mockMvc.perform(post("/uni/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody2));

        mockMvc.perform(post("/uni/students/1/courses/1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.courses[0].id").value(1L));
    }

    @Test
    void updateStudent() throws Exception {
        StudentCreateDTO  request = new StudentCreateDTO (
                "Garik", "Barxudaryan",
                Gender.MALE, LocalDate.of(2006,9,19),
                LocalDate.of(2025,9,1), "garik@gmail.com",
                null, null);

        String requestBody = objectMapper.writeValueAsString(request);

        String response = mockMvc.perform(post("/uni/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andReturn().getResponse().getContentAsString();

        StudentDTO created = objectMapper.readValue(response, StudentDTO.class);

        StudentUpdateDTO updatedRequest = new StudentUpdateDTO(
                "Gor",
                "Asatryan",
                Gender.MALE,
                LocalDate.of(2006,9,19),
                LocalDate.of(2025,9,1),
                "gor@gmail.com",
                null,
                null
        );

        String updatedBody = objectMapper.writeValueAsString(updatedRequest);

        String newUpdatedStudent = mockMvc.perform(put("/uni/students/" + created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedRequest.getName()))
                .andExpect(jsonPath("$.surname").value(updatedRequest.getSurname()))
                .andExpect(jsonPath("$.sex").value(updatedRequest.getSex().toString()))
                .andExpect(jsonPath("$.birthdate").value(updatedRequest.getBirthdate().toString()))
                .andExpect(jsonPath("$.enrolmentDate").value(updatedRequest.getEnrolmentDate().toString()))
                .andExpect(jsonPath("$.email").value(updatedRequest.getEmail()))
                        .andReturn().getResponse().getContentAsString();

        StudentDTO newUpdatedBody = objectMapper.readValue(newUpdatedStudent, StudentDTO.class);
        mockMvc.perform(get("/uni/students/" + newUpdatedBody.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedRequest.getName()))
                .andExpect(jsonPath("$.surname").value(updatedRequest.getSurname()))
                .andExpect(jsonPath("$.sex").value(updatedRequest.getSex().toString()))
                .andExpect(jsonPath("$.birthdate").value(updatedRequest.getBirthdate().toString()))
                .andExpect(jsonPath("$.enrolmentDate").value(updatedRequest.getEnrolmentDate().toString()))
                .andExpect(jsonPath("$.email").value(updatedRequest.getEmail()));
    }

    @Test
    void patchStudent() throws Exception {
        StudentCreateDTO request = new StudentCreateDTO("Ani", "Hakobyan", Gender.FEMALE,
                LocalDate.of(2004, 5, 10), LocalDate.of(2021, 9, 1), "ani@example.com", null, null);

        String requestBody = objectMapper.writeValueAsString(request);

        String response = mockMvc.perform(post("/uni/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn().getResponse().getContentAsString();

        StudentDTO created = objectMapper.readValue(response, StudentDTO.class);

        StudentPatchDTO patch = new StudentPatchDTO(
                "Gor", "Barxudaryan", Gender.MALE, LocalDate.of(2005,2,22),
                LocalDate.of(2022, 10, 10), "gor.b@gmail.com", null, null
        );

        mockMvc.perform(patch("/uni/students/" + created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(patch.getName()))
                .andExpect(jsonPath("$.surname").value(patch.getSurname()))
                .andExpect(jsonPath("$.enrolmentDate").value(patch.getEnrolmentDate().toString()))
                .andExpect(jsonPath("$.birthdate").value(patch.getBirthdate().toString()))
                .andExpect(jsonPath("$.email").value(patch.getEmail()))
                .andExpect(jsonPath("$.sex").value(patch.getSex().toString()));

    }

    @Test
    void deleteStudent() throws Exception {
        StudentCreateDTO request = new StudentCreateDTO(
                "Garik", "Barxudaryan",
                Gender.MALE, LocalDate.of(2006,9,19),
                LocalDate.of(2025,9,1), "garik@gmail.com",
                null, null);

        String requestBody = objectMapper.writeValueAsString(request);

        String response = mockMvc.perform(post("/uni/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn().getResponse().getContentAsString();

        StudentDTO responseBody = objectMapper.readValue(response, StudentDTO.class);

        mockMvc.perform(delete("/uni/students/" + responseBody.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/uni/students/"+responseBody.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteStudentFromDTO() throws Exception {
        StudentCreateDTO request = new StudentCreateDTO(
                "Gor", "Barxudaryan", Gender.MALE,
                LocalDate.of(2005,2,22),
                LocalDate.now(),"gor.barxudaryan.2014@gmail.com", null, null
        );

        String requestBody = objectMapper.writeValueAsString(request);

        CourseCreateDTO request2 = new CourseCreateDTO(
                "title", 10, null, null, null
        );

        String requestBody2 = objectMapper.writeValueAsString(request2);


        mockMvc.perform(post("/uni/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        mockMvc.perform(post("/uni/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody2));

        mockMvc.perform(post("/uni/students/1/courses/1"))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/uni/students/1/courses/1"))
                .andExpect(status().isNoContent());
    }
}
