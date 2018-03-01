package com.example.baking.utils;

import com.example.baking.model.api.ApiFactory;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.HashMap;
import java.util.Map;

public class TestResponseRule implements TestRule {

    private Map<String, Object> mResponses = new HashMap<>();

    public TestResponseRule(Object... keyValuePairs) {
        if (keyValuePairs.length % 2 != 0) {
            throw new RuntimeException("Invalid number of parameters");
        }

        for (int i = 0; i < keyValuePairs.length; i += 2) {
            Object key = keyValuePairs[i];
            Object value = keyValuePairs[i + 1];

            if (!(key instanceof String)) {
                throw new RuntimeException("Key must be string");
            }

            if (!(value instanceof String) && !(value instanceof Integer)) {
                throw new RuntimeException("Value must be either String or Integer");
            }

            mResponses.put((String) key, value);
        }
    }


    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                MockHttpInterceptor interceptor = new MockHttpInterceptor();
                for (Map.Entry<String, Object> entry : mResponses.entrySet()) {
                    Object value = entry.getValue();
                    if (value instanceof String) {
                        interceptor.addJsonResponse(entry.getKey(), (String)value);
                    } else if (value instanceof Integer) {
                        interceptor.addHttpCodeResponse(entry.getKey(), (Integer)value);
                    } else {
                        throw new RuntimeException("Unknown value type: " + value.getClass().getSimpleName());
                    }
                }
                ApiFactory.setDefault(ApiFactory.getApi(interceptor));
                try {
                    base.evaluate();
                } finally {
                    ApiFactory.setDefault(null);
                }
            }
        };
    }
}
