package core;

import com.hengyi.cas.core.domain.HrUser;
import com.hengyi.cas.core.domain.OaUser;
import com.hengyi.cas.core.domain.repository.HrUserRepository;
import com.hengyi.cas.core.domain.repository.OaUserRepository;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.authentication.principal.PrincipalResolver;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 描述：
 *
 * @author jzb 2018-01-30
 */
@ContextConfiguration("classpath:/applicationContext.xml")
public class CoreTest extends AbstractJUnit4SpringContextTests {
    private static final AttributesMapper<Attributes> mapper = t -> t;

    @Autowired
    @Qualifier("hyLdapTemplate")
    private LdapTemplate ldapTemplate;

    @Autowired
    private HrUserRepository hrUserRepository;

    @Autowired
    private OaUserRepository oaUserRepository;

    @Autowired
    @Qualifier("hyPrincipalResolver")
    private PrincipalResolver principalResolver;

    @Autowired
    @Qualifier("hyPrincipalFactory")
    private PrincipalFactory principalFactory;

    @Test
    public void test() {
        assertNotNull(ldapTemplate);
        assertNotNull(hrUserRepository);
        assertNotNull(oaUserRepository);
        assertNotNull(principalResolver);
    }

    @Test
    public void testOdm() {
        final HrUser hrUser = hrUserRepository.find("12000077");
        assertEquals("12000077", hrUser.getHrId());
        assertEquals("金赵波", hrUser.getDisplayName());

        OaUser oaUser = oaUserRepository.find("jzb");
        assertEquals("12000077", oaUser.getHrId());
        assertEquals("jzb", oaUser.getOaId());
        assertEquals("金赵波", oaUser.getDisplayName());

        oaUser = oaUserRepository.findByHrId("12000077");
        assertEquals("12000077", oaUser.getHrId());
        assertEquals("jzb", oaUser.getOaId());
        assertEquals("金赵波", oaUser.getDisplayName());
    }

    @Test
    public void testQuery() {
        LdapName ldapName = LdapUtils.newLdapName("ou=hr");
        LdapName pLdapName = LdapNameBuilder.newInstance(ldapName).add("cn", "12000077").build();
        Attributes lookup = ldapTemplate.lookup(pLdapName, mapper);
        System.out.println("hr=" + lookup);
        ldapName = LdapUtils.newLdapName("ou=oa");
        pLdapName = LdapNameBuilder.newInstance(ldapName).add("cn", "jzb").build();
        lookup = ldapTemplate.lookup(pLdapName, mapper);
        System.out.println("oa=" + lookup);
    }
}
