package pwr.isa.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pwr.isa.backend.entity.User;
import pwr.isa.backend.Controller.UserDataUtil;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class GlobalExceptionHandlerTests {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public GlobalExceptionHandlerTests(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testThatGlobalExceptionHandlerHandlesEntityNotFoundException() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users/1")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGlobalExceptionHandlerHandlesIllegalArgumentException() throws Exception {
        User user = UserDataUtil.createInvalidUserNoEmail();
        user.setID(null);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }
}
