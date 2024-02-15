package peoplehere.peoplehere.common.response.status;

public interface ResponseStatus {

    boolean getSuccess();
    int getCode();
    int getStatus();
    String getMessage();
}
