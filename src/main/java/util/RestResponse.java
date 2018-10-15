package util;


import java.io.Serializable;

/**
 * Template converter from web service json response to java class and vice versa.
 * @author Leo Sendra
 *
 */
public class RestResponse implements Serializable {
	private static final long serialVersionUID = 4210265933052852138L;
	private int status;	//status flag based on CommonConstants
	private String message; //message description
	private long totalRecords; //total records data
	private Object contents; //real data content
	
	public RestResponse(){
	}
	
	public RestResponse(int status, String message, Object contents){
		this.status = status;
		this.message = message;
		this.contents = contents;
	}
	
	public RestResponse(int status, String message, Object contents, long totalRecords){
		this.status = status;
		this.message = message;
		this.contents = contents;
		this.totalRecords = totalRecords;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public Object getContents() {
		return contents;
	}

	public void setContents(Object contents) {
		this.contents = contents;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}
}