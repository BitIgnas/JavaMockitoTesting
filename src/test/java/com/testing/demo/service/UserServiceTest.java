package com.testing.demo.service;

import com.testing.demo.entity.User;
import com.testing.demo.repository.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyShort;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userServiceTest;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> idArgumentCaptor;

    @BeforeEach
    void setUp() {
        userArgumentCaptor = ArgumentCaptor.forClass(User.class);
    }

    @Test
    void shouldGetAllUsers() {

        //when
        when(userRepository.findAll())
                .thenReturn(Arrays.asList(new User("test", "test", LocalDate.of(1992, 3, 4))));

        List<User> users = userRepository.findAll();

        verify(userRepository).findAll();
        verifyNoMoreInteractions(userRepository);

        //then
        assertThat(users).isNotNull();
        assertThat(users.size()).isNotEqualTo(0);
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getUsername()).isEqualTo("test");
    }

    @Test
    void ShouldGetUserById() {

        //given
        final Long userId = 1L;
        final User user = new User("test", "test", LocalDate.of(1992, 3, 4));
        user.setId(userId);

        //when
        when(userServiceTest.getUserById(userId)).thenReturn(user);
        User capturedUser = userServiceTest.getUserById(userId);

        verify(userRepository, times(1)).getById(idArgumentCaptor.capture());
        Long capturedUserId = idArgumentCaptor.getValue();

        //then
        assertThat(capturedUserId).isNotNull();
        assertThat(capturedUserId).isEqualTo(userId);
        assertThat(capturedUser.getId()).isEqualTo(userId);

    }

    @Test
    void shouldAddNewUser() {

        //given
        User user = new User("test", "test", LocalDate.of(1992, 3, 4));

        //when
        userServiceTest.addUser(user);

        verify(userRepository).save(userArgumentCaptor.capture());
        verifyNoMoreInteractions(userRepository);


        User capturedUser = userArgumentCaptor.getValue();

        //then
        assertThat(capturedUser).isNotNull();
        assertThat(capturedUser.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void ShouldDeleteUserById() {

        //given
        final Long userId = 1L;

        //then
        userRepository.deleteById(1L);

        verify(userRepository, times(1)).deleteById(userId);
        verifyNoMoreInteractions(userRepository);
    }
}