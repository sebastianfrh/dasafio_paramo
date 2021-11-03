package sat.recruitment.api.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import sat.recruitment.api.core.contracts.UserRequest;
import sat.recruitment.api.core.entities.UserEntity;
import sat.recruitment.api.core.entities.UserType;
import sat.recruitment.api.core.errors.ExistingEntityException;
import sat.recruitment.api.core.errors.RepositoryException;
import sat.recruitment.api.core.usecases.CreateUserUseCase;

@WebMvcTest(SatRecruitmentController.class)
public class SatRecruitmentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CreateUserUseCase createUserServiceMock;

	@Test
	void testCreateValidUserReturnStatusCreated() throws Exception {
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		var userRequest = aNormalUserRequest();

		when(createUserServiceMock.execute(userRequest)).thenReturn(aNormalUserEntity());

		var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/create-user")
				.contentType("application/json;charset=UTF-8").content(objectMapper.writeValueAsString(userRequest)))
				.andReturn();

		assertTrue(result.getResponse().getStatus() == MockHttpServletResponse.SC_CREATED);
		verify(createUserServiceMock, times(1)).execute(ArgumentMatchers.refEq(userRequest));
	}

	@Test
	void testCreateInvalidUserReturnBadRequest() throws Exception {
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		var userRequest = aInvalidUserRequest();

		var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/create-user")
				.contentType("application/json;charset=UTF-8").content(objectMapper.writeValueAsString(userRequest)))
				.andReturn();

		assertTrue(result.getResponse().getStatus() == MockHttpServletResponse.SC_BAD_REQUEST);
		verify(createUserServiceMock, times(0)).execute(ArgumentMatchers.refEq(userRequest));
	}

	@Test
	void testCreateUserThatAlreadyExistsReturnBadRequest() throws Exception {
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		var userRequest = aNormalUserRequest();

		when(createUserServiceMock.execute(any(UserRequest.class)))
				.thenThrow(new ExistingEntityException("user " + userRequest.getName() + " already exists"));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/create-user").contentType("application/json;charset=UTF-8")
				.content(objectMapper.writeValueAsString(userRequest)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.error").exists());

		verify(createUserServiceMock, times(1)).execute(ArgumentMatchers.refEq(userRequest));
	}

	@Test
	void testCreateUserAndServiceThrowsRepositoryExceptionShoulReturnFailedDependency() throws Exception {
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		var userRequest = aNormalUserRequest();

		when(createUserServiceMock.execute(any(UserRequest.class))).thenThrow(new RepositoryException("error"));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/create-user").contentType("application/json;charset=UTF-8")
				.content(objectMapper.writeValueAsString(userRequest)))
				.andExpect(MockMvcResultMatchers.status().isFailedDependency())
				.andExpect(MockMvcResultMatchers.jsonPath("$.error").exists());

		verify(createUserServiceMock, times(1)).execute(ArgumentMatchers.refEq(userRequest));
	}

	@Test
	void testCreateUserAndServiceThrowsIOExceptionShoulReturnInternalServerError() throws Exception {
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		var userRequest = aNormalUserRequest();

		when(createUserServiceMock.execute(any(UserRequest.class))).thenThrow(new IOException("error"));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/create-user").contentType("application/json;charset=UTF-8")
				.content(objectMapper.writeValueAsString(userRequest)))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(MockMvcResultMatchers.jsonPath("$.error").exists());

		verify(createUserServiceMock, times(1)).execute(ArgumentMatchers.refEq(userRequest));
	}

	private UserRequest aNormalUserRequest() {
		return new UserRequest("John Smith", "jsmith@gmail.com", "an address 123", "+543675675443", UserType.NORMAL,
				Double.valueOf(0));
	}

	private UserEntity aNormalUserEntity() {
		return new UserEntity("John Smith", "jsmith@gmail.com", "an address 123", "+543675675443", UserType.NORMAL,
				Double.valueOf(0));
	}

	private UserRequest aInvalidUserRequest() {
		return new UserRequest(null, null, "an address 123", "+543675675443", UserType.NORMAL, Double.valueOf(0));
	}

}
