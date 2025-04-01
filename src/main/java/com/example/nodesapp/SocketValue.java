package com.example.nodesapp;

public class SocketValue {
    public enum ValueType {
        INTEGER,
        STRING,
        BOOLEAN,
    }

    private ValueType type;
    private Object value;
    private Object defaultValue;

    // Constructors for different types
    public static SocketValue createInteger(Integer value, Integer defaultValue) {
        return new SocketValue(ValueType.INTEGER, value, defaultValue);
    }

    public static SocketValue createString(String value, String defaultValue) {
        return new SocketValue(ValueType.STRING, value, defaultValue);
    }

    public static SocketValue createBoolean(Boolean value, Boolean defaultValue) {
        return new SocketValue(ValueType.BOOLEAN, value, defaultValue);
    }

    private SocketValue(ValueType type, Object value, Object defaultValue) {
        this.type = type;
        this.value = value;
        this.defaultValue = defaultValue;
    }

    // getters

    public ValueType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public Object getDefaultValue() {
        return value;
    }

    // setters

    public void setValue(Integer value) {
        this.type = ValueType.INTEGER;
        this.value = value;
    }

    public void setValue(String value) {
        this.type = ValueType.STRING;
        this.value = value;
    }

    public void setValue(Boolean value) {
        this.type = ValueType.BOOLEAN;
        this.value = value;
    }

    // Type-specific getters with proper casting

    public Integer getAsInteger() {
        if (type != ValueType.INTEGER) {
            throw new IllegalStateException("Value is not an integer");
        }
        return (Integer) value;
    }

    // Type-specific getters with proper casting
    public Integer getDefaultAsInteger() {
        if (type != ValueType.INTEGER) {
            throw new IllegalStateException("Value is not an integer");
        }
        return (Integer) defaultValue;
    }

    public String getAsString() {
        if (type != ValueType.STRING) {
            throw new IllegalStateException("Value is not a string");
        }
        return (String) value;
    }

    public String getDefaultAsString() {
        if (type != ValueType.STRING) {
            throw new IllegalStateException("Value is not a string");
        }
        return (String) defaultValue;
    }

    public Boolean getAsBoolean() {
        if (type != ValueType.BOOLEAN) {
            throw new IllegalStateException("Value is not a boolean");
        }
        return (Boolean) value;
    }

    public Boolean getDefaultAsBoolean() {
        if (type != ValueType.BOOLEAN) {
            throw new IllegalStateException("Value is not a boolean");
        }
        return (Boolean) defaultValue;
    }

    // Type checking methods
    public boolean isInteger() {
        return type == ValueType.INTEGER;
    }

    public boolean isString() {
        return type == ValueType.STRING;
    }

    public boolean isBoolean() {
        return type == ValueType.BOOLEAN;
    }

    @Override
    public String toString() {
        return "SocketValue{type=" + type + ", value=" + value + "}";
    }
}
