package com.m_landalex.employee_user.mapper;

import java.util.List;

import com.m_landalex.employee_user.data.AbstractObject;
import com.m_landalex.employee_user.domain.AbstractEntity;

public interface Mapper<S extends AbstractEntity,D extends AbstractObject> {
	
	S toEntity(D object);
	D toObject(S entity);
	List<D> toObjectList(List<S> entityList);

}
