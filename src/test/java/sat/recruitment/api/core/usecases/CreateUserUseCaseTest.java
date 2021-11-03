package sat.recruitment.api.core.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sat.recruitment.api.core.contracts.UserRequest;
import sat.recruitment.api.core.entities.UserEntity;
import sat.recruitment.api.core.entities.UserType;
import sat.recruitment.api.core.errors.ExistingEntityException;
import sat.recruitment.api.core.providers.UserProvider;

@ExtendWith(MockitoExtension.class)
public class CreateUserUseCaseTest {

	@Mock
	UserProvider userProviderMock;

	@Test
	public void testCreateUserWhenUserExistShoulReturnException() throws Exception {
		var mockUserEntity = aNormalUserEntity();
		assertNotNull(userProviderMock);

		when(userProviderMock.exists(mockUserEntity)).thenReturn(true);

		CreateUserUseCase service = new CreateUserUseCase(userProviderMock);
		try {
			service.execute(aNormalUserRequest());
		} catch (ExistingEntityException e) {
			assertTrue(e instanceof ExistingEntityException);
			assertEquals(e.getMessage(), "user " + mockUserEntity.getName() + " already exists");
			verify(userProviderMock, times(1)).exists(ArgumentMatchers.refEq(mockUserEntity));
		}
	}

	@Test
	public void testCreateUserWhenUserNotExistShoulSaveAndReturn() throws Exception {
		var mockUserEntity = aNormalUserEntity();
		assertNotNull(userProviderMock);

		when(userProviderMock.exists(mockUserEntity)).thenReturn(false);
		when(userProviderMock.save(mockUserEntity)).thenReturn(mockUserEntity);

		CreateUserUseCase service = new CreateUserUseCase(userProviderMock);
		var result = service.execute(aNormalUserRequest());

		assertNotNull(result);
		assertEquals(result, mockUserEntity);

		verify(userProviderMock, times(1)).exists(ArgumentMatchers.refEq(mockUserEntity));
		verify(userProviderMock, times(1)).save(ArgumentMatchers.refEq(mockUserEntity));
	}

	@ParameterizedTest(name = "Run {index}: userEntity={0}, userRequest={1}")
	@MethodSource("testCreateUsersParameters")
	public void testCreateNormalUserMoneyShouldCalculateCorrectGifSaveAndReturn(UserEntity userEntity, UserRequest userRequest)
			throws Throwable {
		assertNotNull(userProviderMock);

		when(userProviderMock.exists(userEntity)).thenReturn(false);
		when(userProviderMock.save(userEntity)).thenReturn(userEntity);

		CreateUserUseCase service = new CreateUserUseCase(userProviderMock);
		var result = service.execute(userRequest);

		assertNotNull(result);
		assertEquals(result, userEntity);

		verify(userProviderMock, times(1)).exists(ArgumentMatchers.refEq(userEntity));
		verify(userProviderMock, times(1)).save(ArgumentMatchers.refEq(userEntity));
	}

	private UserEntity aNormalUserEntity() {
		return new UserEntity("John Smith", "jsmith@gmail.com", "an address 123", "+543675675443", UserType.NORMAL,
				Double.valueOf(0));
	}

	private UserRequest aNormalUserRequest() {
		return new UserRequest("John Smith", "jsmith@gmail.com", "an address 123", "+543675675443", UserType.NORMAL,
				Double.valueOf(0));
	}

	private static UserEntity aUserEntityWithTypeAndMoney(UserType userType, Double money) {
		return new UserEntity("John Smith", "jsmith@gmail.com", "an address 123", "+543675675443", userType, money);
	}

	private static UserRequest aUserRequestWithTypeAndMoney(UserType userType, Double money) {
		return new UserRequest("John Smith", "jsmith@gmail.com", "an address 123", "+543675675443", userType, money);
	}

	private static Stream<Arguments> testCreateUsersParameters() throws Throwable {
		return Stream.of(
				Arguments.of(aUserEntityWithTypeAndMoney(UserType.NORMAL, Double.valueOf(224)),
						aUserRequestWithTypeAndMoney(UserType.NORMAL, Double.valueOf(200))),
				Arguments.of(aUserEntityWithTypeAndMoney(UserType.NORMAL, Double.valueOf(43.2)),
						aUserRequestWithTypeAndMoney(UserType.NORMAL, Double.valueOf(40))),
				Arguments.of(aUserEntityWithTypeAndMoney(UserType.SUPERUSER, Double.valueOf(240)),
						aUserRequestWithTypeAndMoney(UserType.SUPERUSER, Double.valueOf(200))),
				Arguments.of(aUserEntityWithTypeAndMoney(UserType.PREMIUM, Double.valueOf(600)),
						aUserRequestWithTypeAndMoney(UserType.PREMIUM, Double.valueOf(200))));
	}
}
