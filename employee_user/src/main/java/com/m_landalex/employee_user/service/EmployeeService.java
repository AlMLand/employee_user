package com.m_landalex.employee_user.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Optional;

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
		Employee newEmployee = mapper.toObject(repository.save(mapper.toEntity(employee)));
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
		return mapper.toObjectList(repository.findAll());
	}

	@Transactional(readOnly = true)
	public Employee fetchById(Long id) {
		return mapper.toObject(repository.findById(id).get());
	}

	@Transactional(readOnly = true)
	public Collection<Employee> fetchByLastName(String lastName) {
		return mapper.toObjectList(repository.findByLastName(lastName));
	}

	public void deleteById(Long id) {
		repository.delete(repository.findById(id).get());
	}

	public void deleteAll() {
		repository.deleteAll();
	}
	
	@Scheduled(cron = "* 1 * * * *")
	public void autoUpdateAge() {
		Optional<Collection<Employee>> optional = Optional.of(fetchAll());
		if (optional.isPresent()) {
			optional.stream().flatMap(collection -> collection.stream()).forEach(
					employee -> employee.setAge(Period.between(employee.getBirthDate(), LocalDate.now()).getYears()));
		}
		optional.stream().flatMap(collection -> collection.stream()).forEach(employee -> {
			try {
				save(employee);
			} catch (AsyncXAResourcesException e) {
				e.printStackTrace();
			}
		});
	}

}
