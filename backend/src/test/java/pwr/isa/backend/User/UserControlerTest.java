package pwr.isa.backend.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControlerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UserService userService;

    @Autowired
    public UserControlerTest(MockMvc mockMvc, ObjectMapper objectMapper, UserService userService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @Test
    public void testThatUserIsCreatedSuccessfully() throws Exception {
        User user = UserDataUtil.createValidUser();
        user.setID(null);
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreatedUsersIsReadedSuccessfully() throws Exception {
        User user = UserDataUtil.createValidUser();
        user.setID(null);
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.password").value(user.getPassword())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.role").value(user.getRole().toString())
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatUserIsNotCreatedAndResponseDoesntContainBrokenUser() throws Exception {
        User user = UserDataUtil.createInvalidUserNoEmail();
        user.setID(null);
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatGetSingleEndpointReturnsUser() throws Exception {
        User user = UserDataUtil.createValidUser();
        user.setID(null);
        String userJson = objectMapper.writeValueAsString(user);

        userService.createUser(user);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users/1")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.password").value(user.getPassword())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.role").value(user.getRole().toString())
        ).andExpect(
                MockMvcResultMatchers.status().isFound()
        );
    }

    @Test
    public void testThatGetAllEndpointReturnsListOfUsers() throws Exception {
        User user = UserDataUtil.createValidUser();
        user.setID(null);
        userService.createUser(user);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users/")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$").isArray()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].email").value(user.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].password").value(user.getPassword())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].role").value(user.getRole().toString())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUserIsFullUpdatedCorrectly() throws Exception {
        User user = UserDataUtil.createValidUser();
        User testUser = UserDataUtil.createOtherValidUser();

        user.setID(null);
        testUser.setID(1L);

        userService.createUser(user);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(testUser.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.password").value(testUser.getPassword())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.role").value(testUser.getRole().toString())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUserIsNotUpdatedIfInvalidIdIsProvided() throws Exception {
        User user = UserDataUtil.createValidUser();
        User testUser = UserDataUtil.createInvalidUserNoEmail();

        user.setID(null);
        testUser.setID(1L);

        userService.createUser(user);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/users/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser))
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatUserIsNotUpdatedIfThereIsAFieldMissingInRequestBody() throws Exception {
        User user = UserDataUtil.createValidUser();
        User testUser = UserDataUtil.createInvalidUserNoPassword();

        user.setID(null);
        testUser.setID(1L);
        testUser.setPassword(null);

        userService.createUser(user);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser))
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatUsersFieldsAreNotClearedIfOnlySomeFieldsAreProvided() throws Exception {
        User user = UserDataUtil.createValidUser();
        User testUser = UserDataUtil.createOtherValidUser();

        user.setID(null);
        testUser.setID(1L);
        testUser.setPassword(null);
        testUser.setRole(null);

        userService.createUser(user);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(testUser.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.password").value(user.getPassword())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.role").value(user.getRole().toString())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test void testThatUserIsNotPatchedIfEmailProvidedIsNotUniqe() throws Exception {
        User user = UserDataUtil.createValidUser();
        User testUser = UserDataUtil.createOtherValidUser();
        User testUser2 = UserDataUtil.createOtherValidUser();

        user.setID(null);
        testUser.setID(null);
        testUser2.setID(null);
        testUser2.setEmail("valid@gmail.com");

        userService.createUser(user);
        userService.createUser(testUser);
        userService.createUser(testUser2);

        testUser.setEmail(testUser2.getEmail());

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser))
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatUserIsDeletedSuccessfully() throws Exception {
        User user = UserDataUtil.createValidUser();
        user.setID(null);
        userService.createUser(user);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/users/1")
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatUserIsNotDeletedIfInvalidIdIsProvided() throws Exception {
        User user = UserDataUtil.createValidUser();
        user.setID(null);
        userService.createUser(user);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/users/2")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }
}
