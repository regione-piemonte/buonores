/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.util;

import org.apache.commons.lang3.StringUtils;

public class UrlBuilder {
    String url;
    String path;
    String limitAndOffset;
    String query;

    public UrlBuilder(String url) {
        this.url = url;
    }

    public UrlBuilder path(String path) {

        if (this.path == null) {
            this.path = new String();
        }

        this.path += "/" + path;

        return this;
    }

    public UrlBuilder query(String paramName, String paramValue) {
        if (!StringUtils.isEmpty(paramName) && !StringUtils.isEmpty(paramValue)) {
            if (this.query == null) {
                this.query = new String("?" + paramName + "=" + paramValue);
            } else {
                this.query += new StringBuffer("&" + paramName + "=" + paramValue).toString();
            }

        }
        return this;
    }

    public UrlBuilder query(String query) {
        if (!StringUtils.isEmpty(query)) {
            if (this.query == null) {
                this.query = new String("?q=" + query);
            } else {
                this.query += " " + query;
            }

        }
        return this;
    }

    public String buildUrl() {
        if (!StringUtils.isEmpty(this.path)) {
            this.url += this.path;
        }
        if (!StringUtils.isEmpty(this.query)) {
            this.url += this.query;
        } else {
            this.url += "?";
        }
        if (!StringUtils.isEmpty(this.limitAndOffset)) {
            this.url += this.limitAndOffset;
        }
        return this.url;

    }
}