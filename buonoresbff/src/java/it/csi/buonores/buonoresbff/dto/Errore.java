/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.ws.rs.core.Response;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Errore {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private Integer status = null;
	private String code = null;
	private String title = null;
	private List<ErroreDettaglio> detail = new ArrayList<ErroreDettaglio>();
	private List<String> links = new ArrayList<String>();

	public Errore() {

	}

	public Errore(Integer status, String code, String title, List<ErroreDettaglio> detail) {
		super();
		this.status = status;
		this.code = code;
		this.title = title;
		this.detail = detail;
	}

	@JsonProperty("status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * Codice univoco di errore interno
	 **/

	@JsonProperty("code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Testo dell&#39;errore da mostrare al cittadino
	 **/

	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty("detail")
	public List<ErroreDettaglio> getDetail() {
		return detail;
	}

	public void setDetail(List<ErroreDettaglio> detail) {
		this.detail = detail;
	}

	@JsonProperty("links")
	public List<String> getLinks() {
		return links;
	}

	public void setLinks(List<String> links) {
		this.links = links;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Errore errore = (Errore) o;
		return Objects.equals(status, errore.status) && Objects.equals(code, errore.code)
				&& Objects.equals(title, errore.title) && Objects.equals(detail, errore.detail)
				&& Objects.equals(links, errore.links);
	}

	@Override
	public int hashCode() {
		return Objects.hash(status, code, title, detail, links);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Errore {\n");

		sb.append("    status: ").append(toIndentedString(status)).append("\n");
		sb.append("    code: ").append(toIndentedString(code)).append("\n");
		sb.append("    title: ").append(toIndentedString(title)).append("\n");
		sb.append("    detail: ").append(toIndentedString(detail)).append("\n");
		sb.append("    links: ").append(toIndentedString(links)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

	public static Errore generateErrore(HttpStatus request, List<ErroreDettaglio> error) {
		return new Errore(request.value(), request.getReasonPhrase(), request.getReasonPhrase(), error);
	}

	public Response generateResponseError() {
		return Response.status(this.getStatus() != null ? this.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR.value())
				.entity(this).build();
	}
}
