package com.redhat.management.approval;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.HashMap;
import org.apache.commons.lang3.text.StrSubstitutor;

/**
 * This class was automatically generated by the data modeler tool.
 */

public class ApproveEmailBody implements java.io.Serializable {

	static final long serialVersionUID = 1L;

	private com.redhat.management.approval.Request request;
	private com.redhat.management.approval.Approver approver;
	private String templateFile = "EmailContent.html";

	private com.redhat.management.approval.Group group;

	private java.util.ArrayList<com.redhat.management.approval.Stage> stages;

	public java.lang.String getEmailTemplate() {
		java.lang.String template = System
				.getProperty("jboss.server.config.dir") + "/" + templateFile;
		System.out.println("template path: " + template);

		java.lang.String content = "";
		try {
			content = new String(Files.readAllBytes(Paths.get(template)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	public String getEmailBody() {
		String template = getEmailTemplate();
		HashMap<String, String> values = getRequestParameters();

		StrSubstitutor sub = new StrSubstitutor(values);
		return sub.replace(template);
	}

	public HashMap<String, String> getRequestParameters() {
	    Stage currentStage = EmailDispatcher.getCurrentStage(group, stages);
		HashMap<String, Object> request_content = request.getContent();
		HashMap<String, String> values = new HashMap<String, String>();
		values.put("approver_name",
				approver.getFirstName() + " " + approver.getLastName());
		values.put("requester_name", (String) request.getRequester());
		values.put("product_name", (String) request_content.get("product"));
		values.put("portfolio_name", (String) request_content.get("portfolio"));
		values.put("order_id", (String) request_content.get("order_id"));

		HashMap<String, String> params = (HashMap<String, String>) request_content
				.get("params");

		System.out.println("request content params: " + params);
		values.put("params", getParamsTable(params));
		values.put("current_stage", String.valueOf(stages.indexOf(currentStage)+1));
		values.put("total_stages", String.valueOf(stages.size()));

		return values;
	}

	public String getParamsTable(HashMap<String, String> params) {
		StringBuilder paramsTable = new StringBuilder(
				"<tbody><tr><td><strong>Key</strong></td><td><strong>Value<strong></td></tr>\n");
		
		for(HashMap.Entry<String, String> entry: params.entrySet()) {
			String param = "<tr><td>" + entry.getKey() + "</td><td>" + entry.getValue() + "</td></tr>\n";
			paramsTable.append(param);

		};
		paramsTable.append("</tbody>");
		return paramsTable.toString();
	}
	
	public Stage getCurrentStage() {
	    for (Stage stage : stages) {
	        if (stage.getGroupId().equals(group.getUuid()))
	            return stage;
	    }
	    return null; //TODO Exception handler
	}

	public com.redhat.management.approval.Request getRequest() {
		return this.request;
	}

	public void setRequest(com.redhat.management.approval.Request request) {
		this.request = request;
	}

	public com.redhat.management.approval.Approver getApprover() {
		return this.approver;
	}

	public void setApprover(com.redhat.management.approval.Approver approver) {
		this.approver = approver;
	}

	public com.redhat.management.approval.Group getGroup() {
		return this.group;
	}

	public void setGroup(com.redhat.management.approval.Group group) {
		this.group = group;
	}

	public java.util.ArrayList<com.redhat.management.approval.Stage> getStages() {
		return this.stages;
	}

	public void setStages(
			java.util.ArrayList<com.redhat.management.approval.Stage> stages) {
		this.stages = stages;
	}

	public ApproveEmailBody(com.redhat.management.approval.Request request,
			com.redhat.management.approval.Approver approver,
			com.redhat.management.approval.Group group,
			java.util.ArrayList<com.redhat.management.approval.Stage> stages) {
		this.request = request;
		this.approver = approver;
		this.group = group;
		this.stages = stages;
	}

}