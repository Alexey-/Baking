package com.example.baking.model;

import android.content.Context;
import android.support.annotation.StringRes;

import com.example.baking.R;

public class Measure {

    public enum Unit {
        CUP(R.string.measure_unit_cup),
        TBLSP(R.string.measure_unit_table_spoon),
        TSP(R.string.measure_unit_tea_spoon),
        K(R.string.measure_unit_kg),
        G(R.string.measure_unit_gram),
        OZ(R.string.measure_unit_oz),
        UNIT(R.string.measure_unit_unit),
        UNKNOWN(0);

        @StringRes
        private int mTextResourceId;

        private Unit(@StringRes int text) {
            this.mTextResourceId = text;
        }

        public static Unit fromString(String value) {
            for (Unit unit : values()) {
                if (unit.toString().equalsIgnoreCase(value)) {
                    return unit;
                }
            }
            return UNKNOWN;
        }
    }

    private Unit mUnit;
    private String mRawValue;

    public Measure(String rawValue) {
        mUnit = Unit.fromString(rawValue);
        mRawValue = rawValue;
    }

    public Unit getUnit() {
        return mUnit;
    }

    public String getText(Context context) {
        if (mUnit != Unit.UNKNOWN) {
            return context.getString(mUnit.mTextResourceId);
        } else {
            return mRawValue;
        }
    }

}
