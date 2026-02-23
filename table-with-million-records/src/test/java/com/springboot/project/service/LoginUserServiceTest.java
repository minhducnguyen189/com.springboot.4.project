package com.springboot.project.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.springboot.project.entity.LoginUserEntity;
import com.springboot.project.entity.UserRoleEnumEntity;
import com.springboot.project.exception.BadCredentialException;
import com.springboot.project.exception.ResourceNotFoundException;
import com.springboot.project.generated.model.LoginUserResponseModel;
import com.springboot.project.model.LoginUserModel;
import com.springboot.project.repository.LoginUserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class LoginUserServiceTest {

        @Mock
        private LoginUserRepository loginUserRepository;

        @InjectMocks
        private LoginUserService loginUserService;

        private MockedStatic<SecurityContextHolder> securityContextHolderMockedStatic;

        @BeforeEach
        void setUp() {
                securityContextHolderMockedStatic = mockStatic(SecurityContextHolder.class);
        }

        @AfterEach
        void tearDown() {
                securityContextHolderMockedStatic.close();
        }

        // --- upsertLoginUser ---

        @Test
        void should_create_new_user_when_user_does_not_exist() {
                // given
                LoginUserModel loginUserModel = LoginUserModel.builder()
                                .email("new@example.com")
                                .username("newuser")
                                .name("New User")
                                .roles(List.of(UserRoleEnumEntity.USER))
                                .build();

                when(loginUserRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());

                // when
                loginUserService.upsertLoginUser(loginUserModel);

                // then
                verify(loginUserRepository).findByEmail("new@example.com");
                verify(loginUserRepository).save(argThat(entity -> {
                        assertEquals("new@example.com", entity.getEmail());
                        assertEquals("newuser", entity.getUsername());
                        assertEquals("New User", entity.getName());
                        return true;
                }));
        }

        @Test
        void should_update_existing_user_when_user_already_exists() {
                // given
                LoginUserModel loginUserModel = LoginUserModel.builder()
                                .email("existing@example.com")
                                .username("updateduser")
                                .name("Updated Name")
                                .roles(List.of(UserRoleEnumEntity.ADMIN))
                                .build();

                LoginUserEntity existingEntity = new LoginUserEntity();
                existingEntity.setEmail("existing@example.com");
                existingEntity.setUsername("olduser");
                existingEntity.setName("Old Name");

                when(loginUserRepository.findByEmail("existing@example.com"))
                                .thenReturn(Optional.of(existingEntity));

                // when
                loginUserService.upsertLoginUser(loginUserModel);

                // then
                verify(loginUserRepository).findByEmail("existing@example.com");
                verify(loginUserRepository).save(argThat(entity -> {
                        assertEquals("existing@example.com", entity.getEmail());
                        assertEquals("updateduser", entity.getUsername());
                        assertEquals("Updated Name", entity.getName());
                        return true;
                }));
        }

        // --- getCurrentLoginUser ---

        @Test
        void should_return_current_user_when_authenticated_and_user_exists() {
                // given
                LoginUserModel principal = LoginUserModel.builder()
                                .email("auth@example.com")
                                .username("authuser")
                                .name("Auth User")
                                .roles(List.of(UserRoleEnumEntity.USER))
                                .build();

                Authentication authentication = mock(Authentication.class);
                SecurityContext securityContext = mock(SecurityContext.class);

                securityContextHolderMockedStatic
                                .when(SecurityContextHolder::getContext)
                                .thenReturn(securityContext);
                when(securityContext.getAuthentication()).thenReturn(authentication);
                when(authentication.getPrincipal()).thenReturn(principal);

                LoginUserEntity userEntity = new LoginUserEntity();
                userEntity.setEmail("auth@example.com");
                userEntity.setUsername("authuser");
                userEntity.setName("Auth User");
                userEntity.setRoles(List.of(UserRoleEnumEntity.USER));

                when(loginUserRepository.findByEmail("auth@example.com"))
                                .thenReturn(Optional.of(userEntity));

                // when
                LoginUserResponseModel response = loginUserService.getCurrentLoginUser();

                // then
                assertNotNull(response);
                assertEquals("auth@example.com", response.getEmail());
                assertEquals("Auth User", response.getName());
        }

        @Test
        void should_throw_resource_not_found_when_authenticated_but_user_not_in_database() {
                // given
                LoginUserModel principal = LoginUserModel.builder()
                                .email("missing@example.com")
                                .username("missinguser")
                                .name("Missing User")
                                .roles(List.of(UserRoleEnumEntity.USER))
                                .build();

                Authentication authentication = mock(Authentication.class);
                SecurityContext securityContext = mock(SecurityContext.class);

                securityContextHolderMockedStatic
                                .when(SecurityContextHolder::getContext)
                                .thenReturn(securityContext);
                when(securityContext.getAuthentication()).thenReturn(authentication);
                when(authentication.getPrincipal()).thenReturn(principal);

                when(loginUserRepository.findByEmail("missing@example.com"))
                                .thenReturn(Optional.empty());

                // when & then
                ResourceNotFoundException exception = assertThrows(
                                ResourceNotFoundException.class,
                                () -> loginUserService.getCurrentLoginUser());
                assertTrue(exception.getMessage().contains("missing@example.com"));
        }

        @Test
        void should_throw_bad_credential_when_authentication_is_null() {
                // given
                SecurityContext securityContext = mock(SecurityContext.class);

                securityContextHolderMockedStatic
                                .when(SecurityContextHolder::getContext)
                                .thenReturn(securityContext);
                when(securityContext.getAuthentication()).thenReturn(null);

                // when & then
                assertThrows(
                                BadCredentialException.class, () -> loginUserService.getCurrentLoginUser());
        }

        @Test
        void should_throw_bad_credential_when_principal_is_not_login_user_model() {
                // given
                Authentication authentication = mock(Authentication.class);
                SecurityContext securityContext = mock(SecurityContext.class);

                securityContextHolderMockedStatic
                                .when(SecurityContextHolder::getContext)
                                .thenReturn(securityContext);
                when(securityContext.getAuthentication()).thenReturn(authentication);
                when(authentication.getPrincipal()).thenReturn("anonymousUser");

                // when & then
                assertThrows(
                                BadCredentialException.class, () -> loginUserService.getCurrentLoginUser());
        }
}
