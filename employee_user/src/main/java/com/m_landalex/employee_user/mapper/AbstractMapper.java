package com.m_landalex.employee_user.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;

import com.m_landalex.employee_user.data.AbstractObject;
import com.m_landalex.employee_user.domain.AbstractEntity;

public abstract class AbstractMapper<S extends AbstractEntity, D extends AbstractObject> implements Mapper<S, D> {

	private static ModelMapper modelMapper;
	
	static {
		modelMapper = new ModelMapper();
		modelMapper.getConfiguration()
		.setMatchingStrategy(MatchingStrategies.STRICT)
		.setFieldMatchingEnabled(true)
		.setSkipNullEnabled(true)
		.setFieldAccessLevel(AccessLevel.PRIVATE);
	}
	
	private Class<S> entityClass;
	private Class<D> dtoClass;

	public AbstractMapper(Class<S> entityClass, Class<D> dtoClass) {
		super();
		this.entityClass = entityClass;
		this.dtoClass = dtoClass;
	}

	public S toEntity(D object) {
		return Objects.isNull(object) ? null : modelMapper.map(object, entityClass);
	}

	public D toObject(S entity) {
		return Objects.isNull(entity) ? null : modelMapper.map(entity, dtoClass);
	}

	public List<D> toObjectList(List<S> entityList) {
		return Objects.isNull(entityList) ? new ArrayList<>() : entityList.stream()
				.map(entity -> modelMapper.map(entity, dtoClass))
				.collect(Collectors.toList());
	}

	protected Converter<D, S> converterToEntity(){
		return context -> {
			D source = context.getSource();
			S destination = context.getDestination();
			mapSpecificFields(source, destination);
			return context.getDestination();
		};
	}
	
	protected void mapSpecificFields(S source, D destination) {}

	protected Converter<S, D> converterToDto(){
		return context -> {
			S source = context.getSource();
			D destination = context.getDestination();
			mapSpecificFields(source, destination);
			return context.getDestination();
		};
	}
	
	protected void mapSpecificFields(D source, S destination) {}
	
}
