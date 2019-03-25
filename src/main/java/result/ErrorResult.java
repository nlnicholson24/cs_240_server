package result;

public class ErrorResult {

    private String message;

    public ErrorResult() {}

    public ErrorResult(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
