package com.m_landalex.employee_user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.view.DemoRunFile;

@ActiveProfiles( "test" )
@ExtendWith( SpringExtension.class )
@SpringBootTest( classes = { DemoRunFile.class } )
@DisplayName( "Integration AddressService.class test" )
@DirtiesContext( classMode = ClassMode.BEFORE_EACH_TEST_METHOD )
public class AddressServiceTest {

	@Autowired
	private AddressService addressService;
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "test should return list with size 2" )
	@Test
	public void saveTest( ) {
		Address newAddress = Address.builder().street("test_street").houseNumber(99).city("test_city").postCode("99999")
				.build();
		addressService.save(newAddress);
		List<Address> returnedList = addressService.fetchAll();
		
		assertNotNull(returnedList);
		assertEquals(2, returnedList.size());
	}

	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "test should return list with size 1" )
	@Test
	public void fetchAllTest() {
		List<Address> returnList = addressService.fetchAll();
		
		assertNotNull(returnList);
		assertEquals(1, returnList.size());
	}

	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "test should return address by id 1L" )
	@Test
	public void fetchByIdTest( ) {
		Address returnedAddress = addressService.fetchById(1L);
		
		assertNotNull(returnedAddress);
		assertEquals("test_city", returnedAddress.getCity());
		assertEquals(Integer.valueOf(10), returnedAddress.getHouseNumber());
	}

	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "test should return list with size 1" )
	@Test
	public void deleteByIdTest( ) {
		Address newAddress = Address.builder().street("test_street").houseNumber(99).city("test_city").postCode("99999")
				.build();
		addressService.save(newAddress);
		addressService.deleteById(2L);
		List<Address> returnedList = addressService.fetchAll();
		
		assertNotNull(returnedList);
		assertEquals(Integer.valueOf(1), returnedList.size());
	}

	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "test should return list with size 1 and updated address" )
	@Test
	public void updateTest( ) {
		Address toUpdateAddress = Address.builder().street("test_updated_street").houseNumber(99).city("test_updated_city").postCode("99999")
				.build();
		toUpdateAddress.setId(1L);
		addressService.update(toUpdateAddress);
		List<Address> returnedList = addressService.fetchAll();
		
		assertNotNull(returnedList);
		assertEquals(1, returnedList.size());
		
		Address updatedAddress = returnedList.get(0);
		assertNotNull(updatedAddress);
		assertEquals("test_updated_city", updatedAddress.getCity());
		assertEquals(Integer.valueOf(99), updatedAddress.getHouseNumber());
	}
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "should return returnedCount=1" )
	@Test
	public void countAllAddressesTest() {
		long returnedCount = addressService.countAllAddresses();
		assertEquals( 1, returnedCount );
	}
	
}
