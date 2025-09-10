package org.example.unisystem.end_to_end_test.student;

import org.example.unisystem.dto.student.StudentCreateDTO;
import org.example.unisystem.dto.student.StudentPatchDTO;
import org.example.unisystem.dto.student.StudentUpdateDTO;
import org.example.unisystem.end_to_end_test.container.ContainerConfig;
import org.example.unisystem.dto.student.StudentDTO;
import org.example.unisystem.enums.Gender;
import org.example.unisystem.jpa_repo.StudentJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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

    @BeforeEach
    void setMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        studentJpaRepository.deleteAll();
    }

    @Test
    void findStudentById() throws Exception {
        StudentCreateDTO request = new StudentCreateDTO("Ani", "Hakobyan", Gender.Female,
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
    void findAllStudents() throws Exception {
        StudentCreateDTO request = new StudentCreateDTO("Ani", "Hakobyan", Gender.Female,
                LocalDate.of(2004, 5, 10),
                LocalDate.of(2021, 9, 1), "ani@example.com",
                null, null);

        StudentCreateDTO request2 = new StudentCreateDTO(
                "Gor", "Barxudaryan", Gender.Male,
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

        StudentDTO created = objectMapper.readValue(response, StudentDTO.class);
        StudentDTO created2 = objectMapper.readValue(response2, StudentDTO.class);

        mockMvc.perform(get("/uni/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(created.getId()))
                .andExpect(jsonPath("$[1].id").value(created2.getId()));
    }

    @Test
    void testCreateStudent() throws Exception {
        StudentCreateDTO request = new StudentCreateDTO(
                "Gor", "Barxudaryan", Gender.Male,
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
    void updateStudent() throws Exception {
        StudentCreateDTO  request = new StudentCreateDTO (
                "Garik", "Barxudaryan",
                Gender.Male, LocalDate.of(2006,9,19),
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
                Gender.Male,
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
        StudentCreateDTO request = new StudentCreateDTO("Ani", "Hakobyan", Gender.Female,
                LocalDate.of(2004, 5, 10), LocalDate.of(2021, 9, 1), "ani@example.com", null, null);

        String requestBody = objectMapper.writeValueAsString(request);

        String response = mockMvc.perform(post("/uni/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn().getResponse().getContentAsString();

        StudentDTO created = objectMapper.readValue(response, StudentDTO.class);

        StudentPatchDTO patch = new StudentPatchDTO(
                "Gor", "Barxudaryan", Gender.Male, LocalDate.of(2005,2,22),
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
                Gender.Male, LocalDate.of(2006,9,19),
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
}
