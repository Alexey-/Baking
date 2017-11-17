package com.example.baking.utils;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MockHttpInterceptor implements Interceptor {

    private static final MediaType MEDIA_JSON = MediaType.parse("application/json");
    private Map<String, Integer> mMockStatuses = new TreeMap<>();
    private Map<String, String> mMockFiles = new TreeMap<>();

    public MockHttpInterceptor() {

    }

    public void addHttpCodeResponse(String path, int statusCode) {
        mMockStatuses.put(path, statusCode);
    }

    public void addJsonResponse(String path, String localResourcesFile) {
        mMockFiles.put(path, localResourcesFile);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        for (Map.Entry<String, Integer> mockStatus : mMockStatuses.entrySet()) {
            if (mockStatus.getKey().equalsIgnoreCase(chain.request().url().encodedPath())) {
                return new Response.Builder()
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_1)
                        .code(mockStatus.getValue())
                        .build();
            }
        }

        for (Map.Entry<String, String> mockFile : mMockFiles.entrySet()) {
            if (mockFile.getKey().equalsIgnoreCase(chain.request().url().encodedPath())) {
                String response = FakeData.readJsonStringFromAssets(mockFile.getValue());
                return new Response.Builder()
                        .request(chain.request())
                        .body(ResponseBody.create(MEDIA_JSON, response))
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .message("OK")
                        .build();
            }
        }

        return chain.proceed(chain.request());
    }



}
