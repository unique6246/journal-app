package com.example.demo.security;

import com.example.demo.entity.Journal;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SpringSecurityTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    @MockBean
    private UserService userService;

    // Public endpoints should be accessible without authentication
    @Test
    void testPublicEndpoint_ShouldAllowAccessWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/public/create-user"))
                .andExpect(status().isMethodNotAllowed()); // POST is the only valid method on this endpoint
    }

    @Test
    void testPublicEndpoint_ShouldAllowPostWithoutAuthentication() throws Exception {
        doNothing().when(userService).saveNewUser(ArgumentMatchers.any(User.class));

        mockMvc.perform(post("/public/create-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"password\"}"))
                .andExpect(status().isCreated());
    }

    // "/journal/**" requires authentication, so accessing it without authentication should be forbidden
    @Test
    void testJournalEndpointWithoutAuth_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/journal"))
                .andExpect(status().isUnauthorized());
    }

    // "/journal/**" should be accessible to any authenticated user
    @Test
    @WithMockUser(username = "user")
    void testJournalEndpointWithAuth_ShouldAllowAccess() throws Exception {
        User mockUser = new User("user", "password");
        mockUser.setJournalList(Collections.singletonList(new Journal("Test Journal"))); // Adding a sample journal

        when(userService.getUserByUsername("user")).thenReturn(mockUser);

        mockMvc.perform(get("/journal"))
                .andExpect(status().isOk());
    }

    // "/user/**" requires authentication; unauthenticated requests should return unauthorized
    @Test
    void testUserEndpointWithoutAuth_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isUnauthorized());
    }


    // "/admin/**" requires "ADMIN" role; a regular user should get forbidden
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testAdminEndpointAsUser_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/admin/all-users"))
                .andExpect(status().isForbidden());
    }

    // "/admin/**" requires "ADMIN" role; an admin user should be able to access
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminEndpointAsAdmin_ShouldAllowAccess() throws Exception {
        List<User> mockUsers = List.of(new User("testUser", "password"));
        when(userService.getAllUsers()).thenReturn(mockUsers);

        mockMvc.perform(get("/admin/all-users"))
                .andExpect(status().isOk());
    }

    // Ensure that the CSRF protection is disabled
    @Test
    void testCsrfDisabled_ShouldAllowRequestWithoutCsrfToken() throws Exception {
        mockMvc.perform(post("/public/create-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"password\"}"))
                .andExpect(status().isCreated()); // Should succeed without needing CSRF token due to CSRF disabled
    }

    // Default requests not mapped explicitly should require authentication
    @Test
    void testAnyOtherRequestWithoutAuth_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/other"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user")
    void testAnyOtherRequestWithAuth_ShouldAllowAccess() throws Exception {
        mockMvc.perform(get("/other"))
                .andExpect(status().isNotFound());
    }
}
