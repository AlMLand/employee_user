package com.m_landalex.employee_user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;

import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.AddressService;
import com.m_landalex.employee_user.view.controller.rest.AddressRestController;

@ExtendWith(MockitoExtension.class)
public class AddressControllerTest {

	@Mock
	private AddressService mockedAddressService;
	private AddressRestController addressController;
	private ExtendedModelMap extendedModelMap;
	private List<Address> listAdresses;

	@BeforeEach
	public void setUp() {
		addressController = new AddressRestController();
		extendedModelMap = new ExtendedModelMap();
		listAdresses = new ArrayList<>();
		ReflectionTestUtils.setField(addressController, "service", mockedAddressService);
		Address address = Address.builder().street("TEST_street_1").houseNumber(10).city("TEST_city_1")
				.postCode("11111").build();
		address.setId(1L);
		listAdresses.add(address);
	}

	@Test
	public void create_Test() throws AsyncXAResourcesException {
		Address newAddress = Address.builder().street("TEST_street_2").houseNumber(20).city("TEST_city_2")
				.postCode("22222").build();
		Mockito.when(mockedAddressService.save(newAddress)).thenAnswer(new Answer<Address>() {

			@Override
			public Address answer(InvocationOnMock invocation) throws Throwable {
				listAdresses.add(newAddress);
				return newAddress;
			}
		});
		extendedModelMap.addAttribute("createAddress", addressController.create(newAddress));
		Address returnedAddress = (Address) extendedModelMap.get("createAddress");
		
		assertNotNull(returnedAddress);
		assertEquals(2, listAdresses.size());
		assertEquals("TEST_street_2", returnedAddress.getStreet());
	}

	@Test
	public void create_ShouldThrowRuntimeExceptionTest() throws AsyncXAResourcesException {
		Address newAddress = null;
		Mockito.when(mockedAddressService.save(newAddress)).thenThrow(RuntimeException.class);
		
		assertThrows(RuntimeException.class, () -> {
			addressController.create(newAddress);
		});
	}

	@Test
	public void list_Test() {
		Mockito.when(mockedAddressService.fetchAll()).thenReturn(listAdresses);
		extendedModelMap.addAttribute("list", addressController.list());
		@SuppressWarnings("unchecked")
		List<Address> returnList = (List<Address>) extendedModelMap.get("list");
		
		assertNotNull(returnList);
		assertEquals(1, returnList.size());
	}

	@Test
	public void findByIdT_est() {
		Mockito.when(mockedAddressService.fetchById(Mockito.anyLong())).thenReturn(listAdresses.get(0));
		extendedModelMap.addAttribute("fetchAddressById", addressController.findById(111L));
		Address returnedAddress = (Address) extendedModelMap.get("fetchAddressById");
		
		assertNotNull(returnedAddress);
		assertEquals("TEST_city_1", returnedAddress.getCity());
		assertEquals(Integer.valueOf(10), returnedAddress.getHouseNumber());
		assertEquals("11111", returnedAddress.getPostCode());
		assertEquals("TEST_street_1", returnedAddress.getStreet());
	}

	@Test
	public void deleteStandingAloneById_Test() {
		Mockito.doAnswer(new Answer<Address>() {

			@Override
			public Address answer(InvocationOnMock invocation) throws Throwable {
				listAdresses.remove(0);
				return null;
			}
		}).when(mockedAddressService).deleteById(Mockito.anyLong());
		
		addressController.deleteStandingAloneById(222L);
		
		assertNotNull(listAdresses);
		assertEquals(0, listAdresses.size());
	}

}
