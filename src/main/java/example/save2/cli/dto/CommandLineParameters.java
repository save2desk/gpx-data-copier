package example.save2.cli.dto;

import example.save2.cli.enums.Operation;

public class CommandLineParameters {

    private Operation operation;
    private String firstInputFilePathString;
    private String secondInputFilePathString;
    private String outputFilePathString;
    private boolean parallel = false;

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getFirstInputFilePathString() {
        return firstInputFilePathString;
    }

    public void setFirstInputFilePathString(String firstInputFilePathString) {
        this.firstInputFilePathString = firstInputFilePathString;
    }

    public String getSecondInputFilePathString() {
        return secondInputFilePathString;
    }

    public void setSecondInputFilePathString(String secondInputFilePathString) {
        this.secondInputFilePathString = secondInputFilePathString;
    }

    public String getOutputFilePathString() {
        return outputFilePathString;
    }

    public void setOutputFilePathString(String outputFilePathString) {
        this.outputFilePathString = outputFilePathString;
    }

    public boolean isParallel() {
        return parallel;
    }

    public void setParallel(boolean parallel) {
        this.parallel = parallel;
    }
}
