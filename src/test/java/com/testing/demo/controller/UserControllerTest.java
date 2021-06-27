package com.testing.demo.controller;

import com.testing.demo.entity.User;
import com.testing.demo.service.UserService;
import net.bytebuddy.asm.Advice;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userServiceTest;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

    @Test
    void shouldReturnAllUsers() throws Exception {

        User user = new User();
        user.setUsername("test");
        user.setPassword("test");

       when(userServiceTest.getAllUsers()).thenReturn(Arrays.asList(user));

       this.mockMvc.perform(get("/api/user/"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].username", Matchers.is("test")))
               .andExpect(jsonPath("$[0].password", Matchers.is("test")));

       verify(userServiceTest, times(1)).getAllUsers();
       verifyNoMoreInteractions(userServiceTest);

    }

    @Test
    void shouldReturnUserById() throws Exception {

        User user = new User();
        user.setUsername("test");
        user.setPassword("test");

        when(userServiceTest.getUserById(anyLong())).thenReturn(user);

        this.mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", Matchers.is("test")))
                .andExpect(jsonPath("$.password", Matchers.is("test")));

        verify(userServiceTest).getUserById(anyLong());
        verifyNoMoreInteractions(userServiceTest);

    }

    @Test
    void shouldAddUser() throws Exception {

        this.mockMvc.perform(post("/api/user/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"test\", \"password\":\"test\"}"))
                .andExpect(status().isAccepted());
        verify(userServiceTest).addUser(userArgumentCaptor.capture());

        User savedUser = userArgumentCaptor.getValue();

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("test");
        assertThat(savedUser.getPassword()).isEqualTo("test");
    }

    @Test
    void shouldDeleteById() throws Exception {

        this.mockMvc.perform(delete("/api/user/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        verify(userServiceTest).deleteById(anyLong());
        verifyNoMoreInteractions(userServiceTest);
    }


}