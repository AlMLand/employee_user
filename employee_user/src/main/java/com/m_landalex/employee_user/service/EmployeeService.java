package com.m_landalex.employee_user.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.m_landalex.employee_user.data.Employee;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.mapper.EmployeeMapper;
import com.m_landalex.employee_user.persistence.EmployeeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class EmployeeService {

	@Autowired private EmployeeMapper mapper;
	@Autowired private EmployeeRepository repository;
	@Autowired private JmsTemplate jmsTemplate;
	@Autowired private RabbitTemplate rabbitTemplate;
	@Value("${rabbitmq.queueName}") private String queueName;

	public Employee save(Employee employee) throws AsyncXAResourcesException {
		if (employee == null) {
			log.error("Error by save from employee object");
			rabbitTemplate.convertAndSend(queueName, "error");
			throw new AsyncXAResourcesException("Employee object is null -> method save");
		}
		assert employee != null : "EmployeeService.class, method save employee is null";
		Employee newEmployee = mapper.toObject(repository.save(mapper.toEntity(employee)));
		assert newEmployee != null : "EmployeeService.class, method save newEmployee is null";
		rabbitTemplate.convertAndSend(queueName, "succesful");
		jmsTemplate.convertAndSend("employees",
				"-->Employee with first name " + employee.getFirstName() + ", last name " + employee.getLastName()
						+ " and username " + employee.getUserData().getUsername() + " is saved");
		return newEmployee;
	}

	@Transactional(propagation = Propagation.NEVER)
	public long countAll() {
		return repository.count();
	}

	@Transactional(readOnly = true)
	public Collection<Employee> fetchAll() {
		Collection<Employee> arrayList = new ArrayList<>();
		Optional<Collection<Employee>> optional = Optional.of(mapper.toObjectList(repository.findAll()));
		if(optional.isPresent()) {
			arrayList = optional.stream()
					.flatMap(collection -> collection.stream()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
			assert arrayList != null && arrayList.size() > 0 : "EmployeeService.class, method fetchAll arrayList is null or empty";;
		}
		return arrayList;
	}

	@Transactional(readOnly = true)
	public Employee fetchById(Long id) {
		assert id != null : "EmployeeService.class, method fetchById id is null";
		if(id != null) {
			Optional<Employee> optional = Optional.of(mapper.toObject(repository.findById(id).get()));
			if(optional.isPresent()) {
				return optional.get();
			}
		}
		return new Employee();
	}

	@Transactional(readOnly = true)
	public Collection<Employee> fetchByLastName(String lastName) {
		assert lastName != null : "EmployeeService.class, method fetchByLastName lastName is null";
		Optional<Collection<Employee>> optional = Optional.of(mapper.toObjectList(repository.findByLastName(lastName)));
		if (optional.isPresent()) {
			if (optional.stream().flatMap(collection -> collection.stream())
					.allMatch(employee -> employee.getLastName().compareToIgnoreCase(lastName) == 0)) {
				return optional.stream().flatMap(collection -> collection.stream()).collect(Collectors.toList());
			}
		}
		return new ArrayList<>();
	}

	public void deleteById(Long id) {
		assert id != null : "EmployeeService.class, method deleteById id is null";
		if (id != null) {
			repository.delete(repository.findById(id).get());
		}
	}

	public void deleteAll() {
		repository.deleteAll();
	}
	
	@Scheduled(cron = "* 1 * * * *")
	public void autoUpdateAge() {
		Optional<Collection<Employee>> optional = Optional.of(fetchAll());
		assert optional != null : "EmployeeService.class, method autoUpdateAge optional is null";
		if (optional.isPresent()) {
			optional.stream().flatMap(collection -> collection.stream()).map(employee -> {
				employee.setAge(Period.between(employee.getBirthDate(), LocalDate.now()).getYears());
				return employee;
			}).forEach(employee -> {
				try {
					save(employee);
				} catch (AsyncXAResourcesException e) {
					e.printStackTrace();
				}
			});
		}
	}

}
