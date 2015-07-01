package pnnl.goss.powergrid.web;

import java.util.List;

public class WebServiceException extends Exception {

	private static final long serialVersionUID = 7785630159779275588L;

	private List<String> messages;

    public WebServiceException(List<String> messages) {
        super();
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }

}
