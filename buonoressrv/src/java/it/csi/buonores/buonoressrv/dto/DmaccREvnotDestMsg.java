/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.dto;

public class DmaccREvnotDestMsg {

	private String subject_mail;
	private String subject_push;
	private String subject_mex;
	private String msg_mail;
	private String msg_push;
	private String msg_mex;

	public String getMsg_mail() {
		return msg_mail;
	}

	public void setMsg_mail(String msg_mail) {
		this.msg_mail = msg_mail;
	}

	public String getSubject_mail() {
		return subject_mail;
	}

	public void setSubject_mail(String subject_mail) {
		this.subject_mail = subject_mail;
	}

	public String getSubject_push() {
		return subject_push;
	}

	public void setSubject_push(String subject_push) {
		this.subject_push = subject_push;
	}

	public String getSubject_mex() {
		return subject_mex;
	}

	public void setSubject_mex(String subject_mex) {
		this.subject_mex = subject_mex;
	}

	public String getMsg_push() {
		return msg_push;
	}

	public void setMsg_push(String msg_push) {
		this.msg_push = msg_push;
	}

	public String getMsg_mex() {
		return msg_mex;
	}

	public void setMsg_mex(String msg_mex) {
		this.msg_mex = msg_mex;
	}

	public DmaccREvnotDestMsg(String msg_mail,
			String msg_push, String msg_mex, String subject_mail, String subject_push, String subject_mex) {
		super();
		this.msg_mail = msg_mail;
		this.msg_push = msg_push;
		this.msg_mex = msg_mex;
		this.subject_mail = subject_mail;
		this.subject_mex = subject_mex;
		this.subject_push = subject_push;

	}

	public DmaccREvnotDestMsg() {
		super();
	}

}
