package com.guava.judelaw.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

@Slf4j
public class RestUtil {

    private RestUtil() {}

    private static final String CONTENT_TYPE = "Content-Type";

    private static RestTemplate restTemplate;
    private static boolean isInitialized = false;
    private static final String HTTP_METHOD_GET = "GET";
    private static final String HTTP_METHOD_POST = "POST";
    private static final String HTTP_METHOD_PUT = "PUT";
    private static final String HTTP_METHOD_DELETE = "DELETE";

    private static void init() {
        if(isInitialized) return;

        restTemplate = new RestTemplate();
        HttpMessageConverter<?> stringHttpMessageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        for(int i = 0;  i < messageConverters.size(); i++) {
            HttpMessageConverter<?> httpMessageConverter = messageConverters.get(i);
            if(httpMessageConverter instanceof StringHttpMessageConverter) {
                messageConverters.set(i, stringHttpMessageConverter);
                break;
            }
        }

        restTemplate.setMessageConverters(messageConverters);

        isInitialized = true;
    }

    private static HttpEntity<Object> getRequestEntity(Map<String, String> headerMap) {
        return new HttpEntity<>(getHeader(headerMap));
    }

    private static HttpEntity<Object> getRequestEntity(Map<String, String> headerMap, Map<String, Object> bodyMap) {
        Gson gson = new Gson();
        return new HttpEntity<>(gson.toJson(bodyMap), getHeader(headerMap));
    }

    private static MultiValueMap<String, String> getHeader(Map<String, String> headerMap) {
        if(!headerMap.containsKey(CONTENT_TYPE)) headerMap.put(CONTENT_TYPE, MediaType.APPLICATION_JSON);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        for(Map.Entry<String, String> element : headerMap.entrySet()) {
            headers.add(element.getKey(), element.getValue());
        }
        return headers;
    }

    private static String getQueryUrl(String url, Map<String, String> queryMap) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        for(Map.Entry<String, String> element : queryMap.entrySet()) {
            if(element.getValue() != null) builder.queryParam(element.getKey(), element.getValue());
        }
        return builder.buildAndExpand().encode(StandardCharsets.UTF_8).toUriString();
    }

    @SuppressWarnings("unchecked")
    private static JSONObject getJSONObjectResult(String method, String url, ResponseEntity<String> response) {
        if(response == null || !response.hasBody() || (response.getBody() != null && response.getBody().isEmpty())) {
            return null;
        }

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = new JSONObject();

        try {
            Object parsedBodyObject = jsonParser.parse(response.getBody());
            jsonObject = (JSONObject) parsedBodyObject;
        }
        catch (ParseException e) {
            jsonObject.put("response", response.getBody());
        }

        loggingApiResponse(method, url, jsonObject);
        return jsonObject;
    }

    public static JSONObject get(String url, Map<String, String> headerMap) throws Exception {
        init();
        HttpEntity<Object> requestEntity = getRequestEntity(headerMap);

        loggingApiRequest(HTTP_METHOD_GET, url, headerMap.toString());
        ResponseEntity<String> response = restTemplate.exchange(new URI(url), HttpMethod.GET, requestEntity, String.class);

        return getJSONObjectResult(HTTP_METHOD_GET, url, response);
    }
    public static JSONObject get(String url, Map<String, String> headerMap, Map<String, String> queryMap) throws Exception {
        String queryUrl = getQueryUrl(url, queryMap);
        return get(queryUrl, headerMap);
    }

    public static JSONObject post(String url, Map<String, String> headerMap, Map<String, Object> bodyMap) {
        HttpEntity<Object> requestEntity = getRequestEntity(headerMap, bodyMap);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        return getJSONObjectResult(HTTP_METHOD_POST, url, response);
    }

    public static JSONObject put(String url, Map<String, String> headerMap, Map<String, Object> bodyMap) {
        HttpEntity<Object> requestEntity = getRequestEntity(headerMap, bodyMap);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

        return getJSONObjectResult(HTTP_METHOD_PUT, url, response);
    }

    public static JSONObject delete(String url, Map<String, String> headerMap) {
        init();
        HttpEntity<Object> requestEntity = getRequestEntity(headerMap);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);

        return getJSONObjectResult(HTTP_METHOD_DELETE, url, response);
    }

    private static void loggingApiRequest(String method, String url, String headerMap, Object... object) {
        log.info("*[API Request >>>>>] {} [{}]", url, method);
        log.debug("  - Header : {}", headerMap);
        log.debug("  - Request Body : {}", object);
    }

    private static void loggingApiResponse(String method, String url, Object object) {
        log.info("*[API Response <<<<<] {} [{}]", url, method);
        log.debug("  - Response Body : {}", object);
    }
}
