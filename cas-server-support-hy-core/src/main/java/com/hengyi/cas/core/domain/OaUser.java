package com.hengyi.cas.core.domain;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import java.nio.charset.StandardCharsets;

/**
 * 描述： 对应 AD 中的 OA 员工，无员工号或用 oaId 在进行登录
 *
 * @author jzb 2018-01-30
 */
@Entry(objectClasses = {"contact", "organizationalPerson", "person", "top"}, base = "ou=oa")
public class OaUser {
    @Id
    private Name id;
    @Attribute(name = "cn")
    private String oaId;
    @Attribute(name = "employeeid")
    private String hrId;
    @Attribute(name = "userPassword")
    private byte[] userPassword;
    @Attribute(name = "displayName")
    private String displayName;
    @Attribute(name = "company")
    private String company;
    @Attribute(name = "department")
    private String department;

    public boolean checkPassword(final String originalPassword) {
        String md5Pass = DigestUtils.md5Hex(originalPassword);
        String s = new String(userPassword, StandardCharsets.UTF_8);
        return StringUtils.equalsIgnoreCase(md5Pass, s);
    }

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

    public String getOaId() {
        return oaId;
    }

    public void setOaId(String oaId) {
        this.oaId = oaId;
    }

    public byte[] getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(byte[] userPassword) {
        this.userPassword = userPassword;
    }
}
