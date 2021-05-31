package com.porfiriopartida.timetracking.screen;

import java.util.Objects;

public class WindowScreen {
    private String fullPath;
    private String command;
    private long value;
    private boolean isCommandFound;

    @Override
    public String toString() {
        return "WindowScreen{" +
                "fullPath='" + fullPath + '\'' +
                ", command='" + command + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WindowScreen that = (WindowScreen) o;
        if(this.command == null || that.command == null) return false;
        if (this.command.equals(that.getCommand())) return true;

        return value == that.value &&
                isCommandFound == that.isCommandFound &&
                Objects.equals(fullPath, that.fullPath) &&
                Objects.equals(command, that.command);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullPath, command, value, isCommandFound);
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public void setCommandFound(boolean commandFound) {
        this.isCommandFound = commandFound;
    }

    public boolean isCommandFound() {
        return isCommandFound;
    }
}
