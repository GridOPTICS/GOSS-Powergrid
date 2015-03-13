package pnnl.goss.powergrid.itests;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import pnnl.goss.core.security.GossRealm;

@Component
public class BasicFakeRealm extends AuthorizingRealm implements GossRealm {
	
	private final Map<String, SimpleAccount> builtAccounts = new ConcurrentHashMap<>();
	
	public BasicFakeRealm() {
		SimpleAccount acnt = new SimpleAccount("system", "manager", getName());
		acnt.addStringPermission("*");
		builtAccounts.put("system", acnt);
		
		acnt = new SimpleAccount("reader", "reader", getName());
		acnt.addStringPermission("topic:*");
		acnt.addStringPermission("queue:*");
		acnt.addStringPermission("temp-queue:*");
		
		builtAccounts.put("reader", acnt);
		
		acnt = new SimpleAccount("writer", "writer", getName());
		acnt.addStringPermission("topic:*");
		acnt.addStringPermission("queue:*");
		acnt.addStringPermission("temp-queu:*");
		
		builtAccounts.put("writer", acnt);
	}
	
	public SimpleAccount getAccount(String username){
		return builtAccounts.get(username);
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		//we can safely cast to a UsernamePasswordToken here, because this class 'supports' UsernamePasswordToken
        //objects.  See the Realm.supports() method if your application will use a different type of token.
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        
        String uname = upToken.getUsername();
        if(builtAccounts.containsKey(uname)){
        	return builtAccounts.get(uname);
        }
        return null;
	}

	@Override
	public Set<String> getPermissions(String identifier) {
		Set<String> hashSet = new HashSet<>();
		if (builtAccounts.containsKey(identifier)){
			hashSet.addAll(builtAccounts.get(identifier).getStringPermissions());
		}
		
		return hashSet;
	}

	@Override
	public boolean hasIdentifier(String identifier) {
		return builtAccounts.containsKey(identifier);
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		//get the principal this realm cares about:
        String username = (String) getAvailablePrincipal(principals);
        if (username != null){
        	return builtAccounts.get(username);
        }
        return null;
	}
	
}