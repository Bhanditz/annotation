package eu.europeana.annotation.mongo.dao;

import java.io.Serializable;

import org.mongodb.morphia.Datastore;

import eu.europeana.annotation.mongo.model.internal.PersistentClient;
import eu.europeana.api.commons.nosql.dao.impl.NosqlDaoImpl;

public class PersistentClientDaoImpl <E extends PersistentClient, T extends Serializable>
extends NosqlDaoImpl<E, T> implements PersistentClientDao<PersistentClient, String>{

	public PersistentClientDaoImpl(Class<E> clazz, Datastore datastore) {
		super(datastore, clazz);
	}

}
