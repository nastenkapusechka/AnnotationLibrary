package org.example.validation.analyzers.notNull;

import org.example.validation.annotaions.NotNull;

import java.util.List;
import java.util.Set;

/**
 * This will be a stub class for tests.
 * We will use its fields to pass to methods
 */

public class PlugClass {

    @NotNull
    int intDigits;

    @NotNull
    long longDigits;

    @NotNull
    long[] arrayDigits;

    @NotNull
    String string;

    @NotNull
    String[] strings;

    @NotNull
    Object object;

    @NotNull
    List<?> list;

    @NotNull
    Set<?> set;

    public static class Builder {

        PlugClass cls;

        public Builder() {

            this.cls = new PlugClass();

        }

        public Builder withIntDigits(int val) {
            cls.intDigits = val;
            return this;
        }

        public Builder withLongDigits(long val) {
            cls.longDigits = val;
            return this;
        }

        public Builder withArrayDigits(long ... array) {
            cls.arrayDigits = array;
            return this;
        }

        public Builder withString(String val) {
            cls.string = val;
            return this;
        }

        public Builder withStrings(String ... val) {
            cls.strings = val;
            return this;
        }

        public Builder withObject(Object val) {
            cls.object = val;
            return this;
        }

        public Builder withList(List<?> val) {
            cls.list = val;
            return this;
        }

        public Builder withSet(Set<?> val) {
            cls.set = val;
            return this;
        }

        public PlugClass build() {
            return cls;
        }
    }
}
