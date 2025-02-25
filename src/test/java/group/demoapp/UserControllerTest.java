package group.demoapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.demoapp.aspect.exception.UserException;
import group.demoapp.controller.UserController;
import group.demoapp.controller.view.UserView;
import group.demoapp.repository.entity.Order;
import group.demoapp.repository.entity.OrderProjection;
import group.demoapp.repository.entity.User;
import group.demoapp.repository.entity.UserProjection;
import group.demoapp.service.UserService;
import group.demoapp.service.dto.UserSummaryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private String authHeader;

    @BeforeEach
    public void setUp() {
        objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);

        String username = "USER";
        String password = "PASSWORD";

        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        authHeader = "Basic " + encodedAuth;
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = new User(1L, "John Doe", "john@example.com");
        User user2 = new User(2L, "Jane Doe", "jane@example.com");

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        MvcResult result = mockMvc.perform(get("/api/users_without_orders"))
                .andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();
        List<UserView.UserSummary> users = objectMapper.readValue(json, new TypeReference<>() {
        });

        assertNotNull(users);
        assertEquals(2, users.size());

        assertNotNull(users.get(0));
        assertEquals("John Doe", users.get(0).getName());

        assertNotNull(users.get(1));
        assertEquals("Jane Doe", users.get(1).getName());

    }

    @Test
    public void testGetAllUsersPageable() throws Exception {
        User user1 = new User(1L, "John Doe", "john@example.com");
        User user2 = new User(2L, "Jane Doe", "jane@example.com");
        User user3 = new User(3L, "Jack Doe", "jack@example.com");
        User user4 = new User(4L, "Jim Doe", "jim@example.com");
        User user5 = new User(5L, "Jean Doe", "jean@example.com");
        User user6 = new User(6L, "Juno Doe", "juno@example.com");

        Page<User> userPage = new PageImpl<>(Arrays.asList(user1, user2, user3, user4, user5, user6));

        when(userService.getAllUsers(any(Pageable.class))).thenReturn(userPage);

        MvcResult result = mockMvc.perform(get("/api/users_without_orders_pageable?page=0&size=6"))
                .andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();
        List<UserView.UserSummary> users = objectMapper.readValue(json, new TypeReference<>() {
        });

        assertNotNull(users);
        assertEquals(6, users.size());
        assertEquals("John Doe", users.get(0).getName());
        assertEquals("Juno Doe", users.get(5).getName());
    }

    @Test
    public void testGetUserByIdWithUserOrders() throws Exception {
        User user = new User(1L, "John Doe", "john@example.com");
        Order order = new Order(1L, "Boiler", "Shipping", 4000, user);
        user.addOrder(order);

        when(userService.getUserById(1L)).thenReturn(user);
        MvcResult result = mockMvc.perform(get("/api/get_user_with_orders/1"))
                .andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();
        UserView.UserDetails gotUser = objectMapper.readValue(json, UserView.UserDetails.class);

        assertNotNull(gotUser);
        assertEquals("John Doe", gotUser.getName());
        assertEquals(gotUser.getOrders().get(0).getInfo(), "Boiler");

    }

    @Test
    public void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById(any())).thenThrow(new UserException("User not found"));

        mockMvc.perform(get("/api/get_user_with_orders/1")).andExpect(status().isConflict());

    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User(1L, "John Doe", "john@example.com");
        UserSummaryDto userDto = new UserSummaryDto("John Doe", "john@example.com");

        when(userService.createUser(any(UserSummaryDto.class))).thenReturn(user);

        MvcResult result = mockMvc.perform(post("/api/create_user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        UserView.UserSummary readUser = objectMapper.readValue(json, UserView.UserSummary.class);

        assertNotNull(readUser);
        assertEquals("John Doe", readUser.getName());
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserSummaryDto userDto = new UserSummaryDto("John Smith", "john@example.com");

        MvcResult result = mockMvc.perform(put("/api/update_user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        int startIndex = json.indexOf(":") + 2;
        int endIndex = json.indexOf("\"}", startIndex);

        String responseMessage = json.substring(startIndex, endIndex);
        assertEquals("User updated", responseMessage);
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        MvcResult result = mockMvc.perform(delete("/api/delete_user/1"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        int startIndex = json.indexOf(":") + 2;
        int endIndex = json.indexOf("\"}", startIndex);

        String responseMessage = json.substring(startIndex, endIndex);
        assertEquals("User deleted", responseMessage);
    }

    @Test
    public void testEmailValidationWithUserDto() throws Exception {
        UserSummaryDto userCorrectDto = new UserSummaryDto("John Smith", "john@example.com");

        mockMvc.perform(post("/api/create_user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCorrectDto)))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/update_user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCorrectDto)))
                .andExpect(status().isOk())
                .andReturn();

        UserSummaryDto userInvalidEmailDto = new UserSummaryDto("John Smith", "john_example.com");
        mockMvc.perform(post("/api/create_user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInvalidEmailDto)))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/update_user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInvalidEmailDto)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testUserProjection() throws Exception {
        UserProjection userProjection = new UserProjection() {
            @Override
            public String getName() {
                return "Roma";
            }

            @Override
            public String getEmail() {
                return "roma@example.com";
            }

            @Override
            public List<OrderProjection> getOrders() {
                OrderProjection orderProjection = new OrderProjection() {

                    @Override
                    public String getInfo() {
                        return "Bike";
                    }

                    @Override
                    public String getStatus() {
                        return "Shipping";
                    }

                    @Override
                    public Integer getSum() {
                        return 10000;
                    }
                };
                return List.of(orderProjection);
            }
        };
        when(userService.getUserByName(any())).thenReturn(userProjection);
        MvcResult result = mockMvc.perform(get("/api/user_projection?name=roma"))
                .andExpect(status().isOk())
                .andReturn();
        String jsonString = result.getResponse().getContentAsString();


        // Парсим JSON-строку в JsonNode
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        // Извлекаем name
        String name = jsonNode.get("name").asText();
        String info = "";

        // Извлекаем info из первого заказа
        JsonNode ordersNode = jsonNode.get("orders");
        if (ordersNode.isArray() && ordersNode.size() > 0) {
            info = ordersNode.get(0).get("info").asText();
        }

        assertEquals("Roma", name);
        assertEquals(info, "Bike");
    }
}

