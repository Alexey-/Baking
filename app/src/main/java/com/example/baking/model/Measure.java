package com.example.baking.model;

import android.arch.persistence.room.Ignore;
import android.content.Context;
import android.support.annotation.PluralsRes;

import com.example.baking.R;

public class Measure {

    public enum Unit {
        CUP(R.plurals.measure_unit_cup),
        TBLSP(R.plurals.measure_unit_table_spoon),
        TSP(R.plurals.measure_unit_tea_spoon),
        K(R.plurals.measure_unit_kg),
        G(R.plurals.measure_unit_gram),
        OZ(R.plurals.measure_unit_oz),
        UNIT(R.plurals.measure_unit_unit),
        UNKNOWN(0);

        @PluralsRes
        private int mTextResourceId;

        private Unit(@PluralsRes int text) {
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

    @Ignore
    private Unit mUnit;
    private String mRawValue;

    public Measure(String rawValue) {
        mUnit = Unit.fromString(rawValue);
        mRawValue = rawValue;
    }

    public Unit getUnit() {
        return mUnit;
    }

    public String getText(Context context, double quantity) {
        if (mUnit != Unit.UNKNOWN) {
            int roundedQuantity = (int) Math.round(quantity);
            if (quantity != roundedQuantity) {
                return context.getResources().getQuantityString(mUnit.mTextResourceId, 5);
            } else {
                return context.getResources().getQuantityString(mUnit.mTextResourceId, roundedQuantity);
            }
        } else {
            return mRawValue;
        }
    }

    public String getRawValue() {
        return mRawValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Measure measure = (Measure) o;

        return mRawValue != null ? mRawValue.equals(measure.mRawValue) : measure.mRawValue == null;
    }

    @Override
    public int hashCode() {
        return mRawValue != null ? mRawValue.hashCode() : 0;
    }
}
