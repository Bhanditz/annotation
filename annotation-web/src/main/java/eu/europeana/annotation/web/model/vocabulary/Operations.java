package eu.europeana.annotation.web.model.vocabulary;

public interface Operations {

	//search is also retrieve
	//crud operations
	public static final String RETRIEVE = "retrieve";
	public static final String CREATE = "create";
	public static final String UPDATE = "update";
	public static final String DELETE = "delete";
	
	//feedback
	public static final String REPORT = "report";
	
	//admin
	public static final String ADMIN_ALL = "admin_all";
	public static final String ADMIN_UNLOCK = "admin_unlock";
	public static final String ADMIN_REINDEX = "admin_reindex"; 

	//provider
	public static final String PROVIDER_CREATE = "provider_create";
	public static final String PROVIDER_DELETE = "provider_delete";
	
	//user
	public static final String USER_CREATE = "user_create";
	public static final String USER_DELETE = "user_delete";
	
	//moderation
	public static final String MODERATION_ALL = "moderation_all";
	
	//whitelist
	public static final String WHITELIST_ALL = "whitelist_all";
	public static final String WHITELIST_RETRIEVE = "whitelist_retrieve";
	public static final String WHITELIST_CREATE = "whitelist_create";
	public static final String WHITELIST_UPDATE = "whitelist_update";
	public static final String WHITELIST_DELETE = "whitelist_delete";
	
}
