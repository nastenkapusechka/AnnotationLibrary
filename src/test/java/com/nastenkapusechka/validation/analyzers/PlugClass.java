package com.nastenkapusechka.validation.analyzers;

import com.nastenkapusechka.validation.annotaions.*;
import com.nastenkapusechka.validation.util.ThreadTarget;
import com.nastenkapusechka.validation.util.MapTarget;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlugClass {

    @PhoneNumber
    @Password
    @Email
    @CardNumber
    @NotNull
    @ThreadSafe
    String string;

    @CardNumber
    @NotNull
    @Email
    @PhoneNumber
    @Password
    @ThreadSafe
    volatile String[] strings;

    @PhoneNumber
    @Password
    @Email
    @CardNumber
    @NotNull
    @ThreadSafe(threadTarget = ThreadTarget.ALL)
    volatile List<?> list;

    @PhoneNumber
    @Password
    @Email
    @CardNumber
    @NotNull
    @ThreadSafe
    Set<?> set;

    @PhoneNumber(mapTarget = MapTarget.VALUES)
    @Password(mapTarget = MapTarget.VALUES)
    @Email(mapTarget = MapTarget.VALUES)
    @CardNumber(mapTarget = MapTarget.VALUES)
    @NotNull(mapTarget = MapTarget.VALUES)
    @ThreadSafe
    Map<Integer, String> map;

    @PhoneNumber
    @Password
    @Email
    @CardNumber
    @NotNull
    Object[] objects;

    @Validate
    InnerClass innerClass;

    static class InnerClass {

        @PhoneNumber
        @Password
        @Email
        @CardNumber
        @NotNull
        String string;

        public InnerClass(String s) {
            this.string = s;
        }
    }

    static class Builder {

        PlugClass cls;

        public Builder() {
            this.cls = new PlugClass();
        }

        public Builder withString(String string) {
            cls.string = string;
            return this;
        }

        public Builder withStrings(String... strings) {
            cls.strings = strings;
            return this;
        }

        public Builder withList(List<?> list) {
            cls.list = list;
            return this;
        }

        public Builder withSet(Set<?> set) {
            cls.set = set;
            return this;
        }

        public Builder withMap(Map<Integer, String> map) {
            cls.map = map;
            return this;
        }

        public Builder withObjects(Object... objects) {
            cls.objects = objects;
            return this;
        }

        public Builder withInnerClass(String s) {
            cls.innerClass = new InnerClass(s);
            return this;
        }

        public PlugClass build() {
            return cls;
        }
    }
}
