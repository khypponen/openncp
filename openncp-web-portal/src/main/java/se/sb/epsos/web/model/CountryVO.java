/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/
package se.sb.epsos.web.model;

import java.io.Serializable;

public class CountryVO implements Serializable, Comparable<CountryVO> {
    private static final long serialVersionUID = -1298650180518597757L;
    
    private String id;
    private String name;
    private String homeCommunityId;
    
    public CountryVO(String id){
        this.id = id;
    }

    public CountryVO(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public CountryVO(String id, String name, String homeCommunityId) {
        this.id = id;
        this.name = name;
        this.homeCommunityId = homeCommunityId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHomeCommunityId(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }

    public String getHomeCommunityId() {
        return homeCommunityId;
    }    
    
    @Override
    public int compareTo(CountryVO c) {
        return this.id.compareTo(c.getId());
    }
}
