package vmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.zkoss.util.resource.Labels;

import util.RestResponse;

public class BaseVmd {
	
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	
	private final String WS_URL = Labels.getLabel("wsUrl");
	
		
	public RestResponse callWs(String uri, Object sentObject,
			HttpMethod httpMethod) {

		return executeWebService(uri, sentObject, httpMethod);
	}
	
	
	
	private RestResponse executeWebService(String url, Object sentObject,
			HttpMethod httpMethod) {
		url = url.replace("\\", "");

		logger.info("Invoke web service with URL : {}", url);

		RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		final HttpEntity<Object> requestEntity = new HttpEntity<Object>(
				sentObject, headers);
		final ResponseEntity<RestResponse> reponseEntity = restTemplate
				.exchange(url, httpMethod, requestEntity, RestResponse.class);

		return reponseEntity.getBody();
	}
}
