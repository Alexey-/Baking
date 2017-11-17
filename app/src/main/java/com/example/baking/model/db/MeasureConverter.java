package com.example.baking.model.db;

import android.arch.persistence.room.TypeConverter;

import com.example.baking.model.Measure;

public class MeasureConverter {

    @TypeConverter
    public static Measure toMeasure(String value) {
        return new Measure(value);
    }

    @TypeConverter
    public static String toString(Measure measure) {
        return measure.getRawValue();
    }

}
