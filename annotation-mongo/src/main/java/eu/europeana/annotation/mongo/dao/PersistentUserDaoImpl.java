package eu.europeana.annotation.mongo.dao;

import java.io.Serializable;

import org.mongodb.morphia.Datastore;

import eu.europeana.annotation.mongo.model.authentication.internal.PersistentUser;
import eu.europeana.api.commons.nosql.dao.impl.NosqlDaoImpl;

public class PersistentUserDaoImpl <E extends PersistentUser, T extends Serializable>
extends NosqlDaoImpl<E, T> implements PersistentUserDao<PersistentUser, String>{

	public PersistentUserDaoImpl(Class<E> clazz, Datastore datastore) {
		super(datastore, clazz);
	}

}
