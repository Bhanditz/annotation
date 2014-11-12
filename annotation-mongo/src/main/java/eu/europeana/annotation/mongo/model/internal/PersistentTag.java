package eu.europeana.annotation.mongo.model.internal;

import org.bson.types.ObjectId;

import eu.europeana.annotation.definitions.model.resource.TagResource;
import eu.europeana.annotation.definitions.model.vocabulary.TagTypes;
import eu.europeana.corelib.db.entity.nosql.abstracts.NoSqlEntity;

public interface PersistentTag extends TagResource, NoSqlEntity{

		public static final String FIELD_LABEL = "value";
		public static final String FIELD_HTTPURI = "httpUri";
		public static final String FIELD_TAG_TYPE = "tagType";
		
		public abstract ObjectId getId();
		public abstract String getLabel();
		public abstract void setLabel(String label);
		public abstract void setTagType(TagTypes tagType);
		public abstract void setTagType(String tagType);
		public abstract String getTagType();
		
}