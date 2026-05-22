package example.save2.cli.dto;

import example.save2.cli.enums.Operation;

public class CommandLineParameters {

    private Operation operation;
    private String firstFilePathString;
    private String secondFilePathString;
    private boolean parallel = false;

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getFirstFilePathString() {
        return firstFilePathString;
    }

    public void setFirstFilePathString(String firstFilePathString) {
        this.firstFilePathString = firstFilePathString;
    }

    public String getSecondFilePathString() {
        return secondFilePathString;
    }

    public void setSecondFilePathString(String secondFilePathString) {
        this.secondFilePathString = secondFilePathString;
    }

    public boolean isParallel() {
        return parallel;
    }

    public void setParallel(boolean parallel) {
        this.parallel = parallel;
    }
}
