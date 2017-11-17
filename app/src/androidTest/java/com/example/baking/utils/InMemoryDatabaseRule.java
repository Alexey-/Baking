package com.example.baking.utils;

import android.support.test.InstrumentationRegistry;

import com.example.baking.model.db.BakingDatabase;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class InMemoryDatabaseRule implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                BakingDatabase db = BakingDatabase.getInMemoryDatabase(InstrumentationRegistry.getContext());
                BakingDatabase.setDefault(db);
                try {
                    base.evaluate();
                } finally {
                    db.close();
                    BakingDatabase.setDefault(null);
                }
            }
        };
    }

}
