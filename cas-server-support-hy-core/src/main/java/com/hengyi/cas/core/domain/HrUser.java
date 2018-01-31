package com.hengyi.cas.core.domain;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

/**
 * 描述： 对应 AD 中的员工
 *
 * @author jzb 2018-01-30
 */
@Entry(objectClasses = {"user", "organizationalPerson", "person", "top"}, base = "ou=hr")
public class HrUser {
    @Id
    private Name id;
    @Attribute(name = "cn")
    private String hrId;
    @Attribute(name = "displayName")
    private String displayName;
    @Attribute(name = "company")
    private String company;
    @Attribute(name = "department")
    private String department;

    public Name getId() {
        return id;
    }

    public void setId(Name id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getHrId() {
        return hrId;
    }

    public void setHrId(String hrId) {
        this.hrId = hrId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
