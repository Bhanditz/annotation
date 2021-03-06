package eu.europeana.annotation.config;

public interface AnnotationConfiguration {

	public static final String ANNOTATION_INDEXING_ENABLED = "annotation.indexing.enabled";
	public static final String ANNOTATION_ENVIRONMENT = "annotation.environment";
	
	public static final String SUFFIX_BASEURL = "baseUrl";
	public static final String VALUE_ENVIRONMENT_PRODUCTION = "production";
	public static final String VALUE_ENVIRONMENT_TEST = "test";
	public static final String VALUE_ENVIRONMENT_DEVELOPMENT = "development";
	
	public static final String DEFAULT_WHITELIST_RESOURCE_PATH = "annotation.whitelist.default";
	
	public static final String PREFIX_MAX_PAGE_SIZE = "annotation.search.maxpagesize.";
	
	public static final String VALIDATION_API = "api";
	public static final String VALIDATION_ADMIN_API_KEY = "adminapikey";
	public static final String VALIDATION_ADMIN_SECRET_KEY = "adminsecretkey";

	public static final String API_KEY_CACHING_TIME = "annotation.apikey.caching.time";
	
	public String getComponentName();
	
	/**
	 * uses annotation.indexing.enabled property
	 */
	public boolean isIndexingEnabled();
	
	/**
	 * checks annotation.environment=production property
	 */
	public boolean isProductionEnvironment();
	
	/**
	 * uses annotation.environment property
	 */
	public String getEnvironment();
	
	
	/**
	 * uses annotation.environment.{$environment}.baseUrl property
	 */
	public String getAnnotationBaseUrl();
	
	/**
	 * uses annotation.whitelist.default property
	 */
	public String getDefaultWhitelistResourcePath();
	
	public int getMaxPageSize(String profile);
	
	public String getValidationApi();

	public String getValidationAdminApiKey();

	public String getValidationAdminSecretKey();
	
	public long getApiKeyCachingTime();
	
}
