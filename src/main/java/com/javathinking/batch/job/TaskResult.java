package com.javathinking.batch.job;

/**
 * @author paul
 */
public class TaskResult {
    public enum Result {SUCCESS, FAIL, ERROR}

    ;

    private Result result;
    private Object data;

    public TaskResult(boolean result, Object data) {
        this(result ? Result.SUCCESS : Result.FAIL, data);
    }


    public TaskResult(Result result, Object data) {
        this.result = result;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public Result getResult() {
        return result;
    }

}
