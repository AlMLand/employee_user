package com.m_landalex.employee_user.service;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.m_landalex.employee_user.data.Employee;
import com.m_landalex.employee_user.domain.AddressEntity;
import com.m_landalex.employee_user.domain.EmailEntity;
import com.m_landalex.employee_user.domain.EmployeeEntity;
import com.m_landalex.employee_user.domain.UserEntity;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.mapper.AddressMapper;
import com.m_landalex.employee_user.mapper.EmailMapper;
import com.m_landalex.employee_user.mapper.EmployeeMapper;
import com.m_landalex.employee_user.mapper.UserMapper;
import com.m_landalex.employee_user.persistence.AddressRepository;
import com.m_landalex.employee_user.persistence.EmailRepository;
import com.m_landalex.employee_user.persistence.EmployeeRepository;
import com.m_landalex.employee_user.persistence.UserRepository;

@Transactional
@Service
public class EmployeeService {

	@Autowired 	private EmployeeRepository employeeRepository;
	@Autowired 	private AddressRepository addressRepository;
	@Autowired 	private EmailRepository emailRepository;
	@Autowired 	private UserRepository userRepository;
	@Autowired 	private EmployeeMapper employeeMapper;
	@Autowired 	private AddressMapper addressMapper;
	@Autowired 	private EmailMapper emailMapper;
	@Autowired 	private UserMapper userMapper;
	@Autowired 	private JmsTemplate jmsTemplate;
	@Autowired private RabbitTemplate rabbitTemplate;
	@Value("${rabbitmq.queueName}") private String queueName;

	public Employee save(Employee employee) throws AsyncXAResourcesException {
		if (employee == null) {
			rabbitTemplate.convertAndSend(queueName, "error");
			throw new AsyncXAResourcesException("Symulation going wrong");
		}
		employeeRepository.save(employeeMapper.toEntity(employee));
		rabbitTemplate.convertAndSend(queueName, "succesful");
		jmsTemplate.convertAndSend("employees",
				"-->Employee with first name " + employee.getFirstName() + ", last name " + employee.getLastName()
						+ " and username " + employee.getUserData().getUsername() + " is saved");
		return employee;
	}

	public Employee update(Employee employee) {
		EmployeeEntity returnedEmployeeEntity = employeeRepository.findById(employee.getId()).get();
		returnedEmployeeEntity.setFirstName(employeeMapper.toEntity(employee).getFirstName());
		returnedEmployeeEntity.setLastName(employeeMapper.toEntity(employee).getLastName());
		returnedEmployeeEntity.setAge(employeeMapper.toEntity(employee).getAge());
		returnedEmployeeEntity.setSalary(employeeMapper.toEntity(employee).getSalary());

		EmailEntity returnedEmailEntity = emailRepository.findById(returnedEmployeeEntity.getEmail().getId()).get();
		returnedEmailEntity.setEmail(emailMapper.toEntity(employee.getEmail()).getEmail());
		returnedEmployeeEntity.setEmail(returnedEmailEntity);

		AddressEntity returendAddressEntity = addressRepository.findById(returnedEmployeeEntity.getAddressData().getId()).get();
		returendAddressEntity.setStreet(addressMapper.toEntity(employee.getAddressData()).getStreet());
		returendAddressEntity.setHouseNumber(addressMapper.toEntity(employee.getAddressData()).getHouseNumber());
		returendAddressEntity.setCity(addressMapper.toEntity(employee.getAddressData()).getCity());
		returendAddressEntity.setPostCode(addressMapper.toEntity(employee.getAddressData()).getPostCode());
		returnedEmployeeEntity.setAddressData(returendAddressEntity);

		UserEntity returnedUserEntity = userRepository.findById(returnedEmployeeEntity.getUserData().getId()).get();
		returnedUserEntity.setUsername(userMapper.toEntity(employee.getUserData()).getUsername());
		returnedUserEntity.setPassword(userMapper.toEntity(employee.getUserData()).getPassword());
		returnedUserEntity.setUserRole(userMapper.toEntity(employee.getUserData()).getUserRole());
		returnedEmployeeEntity.setUserData(returnedUserEntity);

		employeeRepository.save(returnedEmployeeEntity);
		return employee;
	}

	@Transactional(propagation = Propagation.NEVER)
	public long countAllEmployees() {
		return employeeRepository.count();
	}

	@Transactional(readOnly = true)
	public List<Employee> fetchAll() {
		return employeeMapper.toObjectList(employeeRepository.findAll());
	}

	@Transactional(readOnly = true)
	public Employee fetchById(Long id) {
		return employeeMapper.toObject(employeeRepository.findById(id).get());
	}

	@Transactional(readOnly = true)
	public List<Employee> fetchByFirstName(String firstName) {
		return employeeMapper.toObjectList(employeeRepository.findByFirstName(firstName));
	}

	@Transactional(readOnly = true)
	public List<Employee> fetchByLastName(String lastName) {
		return employeeMapper.toObjectList(employeeRepository.findByLastName(lastName));
	}

	public void deleteById(Long id) {
		employeeRepository.delete(employeeRepository.findById(id).get());
	}

	public void deleteAll() {
		employeeRepository.deleteAll();
	}

}
