/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.sb.epsos.web.model;

import java.io.Serializable;

import org.apache.wicket.markup.html.WebPage;

public class BreadCrumbVO<T extends WebPage> implements Serializable {
	private static final long serialVersionUID = 9167150169743357989L;
	private String title;
    private T webPage;
    private Boolean clickable;
    
    public BreadCrumbVO(String title, T webPage ){
        this.title =  title;
        this.webPage = webPage;
        this.clickable = true;
    }
    
    public BreadCrumbVO(String title){
        this.title =  title;
        this.clickable = false;
    }

    public Boolean getClickable() {
        return clickable;
    }

    public void setClickable(Boolean clickable) {
        this.clickable = clickable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public T getWebPage() {
        return webPage;
    }

    public void setWebPage(T webPage) {
        this.webPage = webPage;
    }
    
}
