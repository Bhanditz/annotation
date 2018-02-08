/*
 * Copyright 2007-2012 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 * 
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under
 *  the Licence.
 */
package eu.europeana.annotation.web.service.impl;

import static org.junit.Assert.assertTrue;

// assymetric keys generation
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.annotation.config.AnnotationConfiguration;
import eu.europeana.annotation.web.exception.authentication.ApplicationAuthenticationException;
import eu.europeana.annotation.web.service.AdminService;


/**
 * Unit test for the Web Annotation private keys validation service.
 * 
 * @author GrafR
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/annotation-web-context.xml"
	})
public class AnnotationPrivateKeyValidationTest {

	@Resource
	private AdminService adminService;

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}
	
	@Resource
	AnnotationConfiguration configuration;
	
	Logger logger = Logger.getLogger(getClass());
	
	private final static String TEST_PRIVATE_KEY = 
			"HqHRSeD3j";
			//"MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIFQ8e163HaUfd60R68HGAkifhBJTzF1azxoUor8sOFSLToVIyqLH2e5xQyGATN+REHx4kBt1hec0l31/alI93mtscHdtkI+pG+dsFTOUyRI7Z5vASneer74yVgI3RM7xOFzRbEkMfBzGW2du3dK6nZWxx5wF7QVaWWiWlnQ9mM7AgMBAAECgYAzo1kYFamXiHBC0AaX8+M7MpTYeA2xmEu8EYR0j7cqIqCAwxQJ1wgxaVMwTwqHv3IOS0Knq7SENVEjyCkEwmhfjsuPjt7ULahcZYWcY12smzmmvQi+l/Weh3wJ49BptXXfKq4OUtIFSJFg0rr1YP/cXP6tyGkYxhkhLNB597VYgQJBAOfiGTU0KyM5LNiPtz2VjpKedS+nKfrZL8xmt3XyIrtF2CGkzVqVgFzO1ug038iur1R4mh0lZK4goUvKcIACNtsCQQCOw/63vrrHK2Hv7yhYeQgOo9ELZMN8rmODshCU2gVb2zK2MnBjYkCZOrVYidGiiq/RHJS6VfCkNWzEu41IlkMhAkA58t0N2L23DcDRVgkbENcSeRscD5CEKeayFDFg/mQDnQ7ISaEwNBBiISa/3QbX5h/W4lTRV9jzUyA8CplWxDblAkBCOkW1c3Mt5cAfD/DFahAEypW1FV9iPXvIohfzFVibDgpuXuOoFvIkowlxMy3emzuIZBvSfP+PDYGRmfemjp5BAkEAvckqdXMVxE8vn9bpxqZvZYq6uvP18Blyua2L21BoN22pqNf6AyBW2NbQ/DAqeNovG148Nfp5nWQ5Pbo7vFBuhg==";
	private final static String TEST_PUBLIC_KEY = 
			"oUGG6LMJm";
//			"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBUPHtetx2lH3etEevBxgJIn4QSU8xdWs8aFKK/LDhUi06FSMqix9nucUMhgEzfkRB8eJAbdYXnNJd9f2pSPd5rbHB3bZCPqRvnbBUzlMkSO2ebwEp3nq++MlYCN0TO8Thc0WxJDHwcxltnbt3Sup2VscecBe0FWllolpZ0PZjOwIDAQAB";
	
	private final static String NOT_EXISTING_KEY = "NotExistingApiKey";
	
	
	@Test
	public void testValidatePrivateApiKey() 
			throws MalformedURLException, IOException, ApplicationAuthenticationException {
						
        boolean validationRes = validatePrivateKey(TEST_PRIVATE_KEY);

		assertTrue(validationRes);	
	}

	
//	@Test
	public void testValidateNotExistingPrivateApiKey() 
			throws MalformedURLException, IOException, ApplicationAuthenticationException {
								  
		boolean validationRes = validatePrivateKey(NOT_EXISTING_KEY);

		assertTrue(!validationRes);	
	}

	
	private boolean validatePrivateKey(String privateKey) throws ApplicationAuthenticationException {
   	    
		String validationString = configuration.getValidationString();   	
		boolean validationRes = getAdminService().validatePrivateKey(
				TEST_PUBLIC_KEY, privateKey, validationString);		
		return validationRes;
	}

	
//    @Test
    public void testGeneratePublicPrivateKeyPair() throws ApplicationAuthenticationException {
  	  	
		try {
			int KEY_SIZE = 1024;
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(KEY_SIZE);
			KeyPair pair = keyGen.generateKeyPair();
			PrivateKey privateKey = pair.getPrivate();
			PublicKey publicKey = pair.getPublic();
			byte[] privateKeyBytes = privateKey.getEncoded();
			byte[] publicKeyBytes = publicKey.getEncoded();
			// create Base64 string to transfer it via HTTP request
			String privateKeyStr= new String(Base64.getEncoder().encodeToString(privateKeyBytes));
			String publicKeyStr= new String(Base64.getEncoder().encodeToString(publicKeyBytes));
//			String publicKeyStr = new String(publicKeyBytes, "UTF-8"); 
			logger.info("private key:" + privateKeyStr);
			logger.info("public key:" + publicKeyStr);
  			assertTrue(privateKeyStr != null && publicKeyStr != null);	
			writeToFile("KeyPair/publicKey", publicKeyBytes);
			writeToFile("KeyPair/privateKey", privateKeyBytes);  			
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
    }
	
    
	/**
	 * This method stores generated assimetric keys in given directory
	 * @param path
	 * @param key
	 * @throws IOException
	 */
	public void writeToFile(String path, byte[] key) throws IOException {
		File f = new File(path);
		f.getParentFile().mkdirs();

		FileOutputStream fos = new FileOutputStream(f);
		fos.write(key);
		fos.flush();
		fos.close();
	}    
		
}
