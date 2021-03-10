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
import com.m_landalex.employee_user.service.AddressService;
import com.m_landalex.employee_user.view.controller.AddressController;

@ExtendWith(MockitoExtension.class)
public class AddressControllerTest {

	@Mock
	private AddressService mockedAddressService;
	private AddressController addressController;
	private ExtendedModelMap extendedModelMap;
	private List<Address> listAdresses;

	@BeforeEach
	public void setUp() {
		addressController = new AddressController();
		extendedModelMap = new ExtendedModelMap();
		listAdresses = new ArrayList<>();
		Address address = Address.builder().street("TEST_street_1").houseNumber(10).city("TEST_city_1")
				.postCode("11111").build();
		address.setId(1L);
		listAdresses.add(address);
	}

	@Test
	public void createAddressTest() {
		Address newAddress = Address.builder().street("TEST_street_2").houseNumber(20).city("TEST_city_2")
				.postCode("22222").build();
		Mockito.when(mockedAddressService.save(newAddress)).thenAnswer(new Answer<Address>() {

			@Override
			public Address answer(InvocationOnMock invocation) throws Throwable {
				listAdresses.add(newAddress);
				return newAddress;
			}
		});
		ReflectionTestUtils.setField(addressController, "addressService", mockedAddressService);
		extendedModelMap.addAttribute("createAddress", addressController.createAddress(newAddress));
		Address returnedAddress = (Address) extendedModelMap.get("createAddress");
		
		assertNotNull(returnedAddress);
		assertEquals(2, listAdresses.size());
		assertEquals("TEST_street_2", returnedAddress.getStreet());
	}

	@Test
	public void createAddressShouldThrowRuntimeExceptionTest() {
		Address newAddress = null;
		Mockito.when(mockedAddressService.save(newAddress)).thenThrow(RuntimeException.class);
		ReflectionTestUtils.setField(addressController, "addressService", mockedAddressService);
		
		assertThrows(RuntimeException.class, () -> {
			addressController.createAddress(newAddress);
		});
	}

	@Test
	public void fetchAllAddressesTest() {
		Mockito.when(mockedAddressService.fetchAll()).thenReturn(listAdresses);
		ReflectionTestUtils.setField(addressController, "addressService", mockedAddressService);
		extendedModelMap.addAttribute("fetchAllAddresses", addressController.fetchAllAddresses());
		@SuppressWarnings("unchecked")
		List<Address> returnList = (List<Address>) extendedModelMap.get("fetchAllAddresses");
		
		assertNotNull(returnList);
		assertEquals(1, returnList.size());
	}

	@Test
	public void updateAddressByIdTest() {
		Address newAddress = Address.builder().street("TEST_street_2").houseNumber(20).city("TEST_city_2")
				.postCode("22222").build();
		Mockito.when(mockedAddressService.update(newAddress)).thenAnswer(new Answer<Address>() {

			@Override
			public Address answer(InvocationOnMock invocation) throws Throwable {
				Address returnedAddress = listAdresses.get(0);
				returnedAddress.setCity(newAddress.getCity());
				returnedAddress.setHouseNumber(newAddress.getHouseNumber());
				returnedAddress.setPostCode(newAddress.getPostCode());
				returnedAddress.setStreet(newAddress.getStreet());
				listAdresses.remove(0);
				listAdresses.add(returnedAddress);
				return returnedAddress;
			}
		});
		ReflectionTestUtils.setField(addressController, "addressService", mockedAddressService);
		extendedModelMap.addAttribute("updateAddress", addressController.updateAddressById(1L, newAddress));
		Address updatedAddress = (Address) extendedModelMap.get("updateAddress");
		
		assertNotNull(updatedAddress);
		assertEquals(1, listAdresses.size());
		assertEquals("TEST_city_2", updatedAddress.getCity());
	}

	@Test
	public void fetchAddressByIdTest() {
		Mockito.when(mockedAddressService.fetchById(Mockito.anyLong())).thenReturn(listAdresses.get(0));
		ReflectionTestUtils.setField(addressController, "addressService", mockedAddressService);
		extendedModelMap.addAttribute("fetchAddressById", addressController.fetchAddressById(111L));
		Address returnedAddress = (Address) extendedModelMap.get("fetchAddressById");
		
		assertNotNull(returnedAddress);
		assertEquals("TEST_city_1", returnedAddress.getCity());
		assertEquals(Integer.valueOf(10), returnedAddress.getHouseNumber());
		assertEquals("11111", returnedAddress.getPostCode());
		assertEquals("TEST_street_1", returnedAddress.getStreet());
	}

	@Test
	public void deleteStandingAloneAddressByIdTest() {
		Mockito.doAnswer(new Answer<Address>() {

			@Override
			public Address answer(InvocationOnMock invocation) throws Throwable {
				listAdresses.remove(0);
				return null;
			}
		}).when(mockedAddressService).deleteById(Mockito.anyLong());
		ReflectionTestUtils.setField(addressController, "addressService", mockedAddressService);
		
		addressController.deleteStandingAloneAddressById(222L);
		
		assertNotNull(listAdresses);
		assertEquals(0, listAdresses.size());
	}

}
