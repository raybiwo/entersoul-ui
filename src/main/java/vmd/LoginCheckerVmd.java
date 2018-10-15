package vmd;

import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Initiator;


public class LoginCheckerVmd implements Initiator{
	public void doInit(Page arg0, Map<String, Object> arg1) throws Exception {
		Session sess = Sessions.getCurrent();
		
//		if (System.getProperty("debug") != null){
//			UserInfo obj = new UserInfo();
//			obj.setUserId(new Long(1));
//			obj.setUserAssgName("Demo User");
//			obj.setUserLocale("en");
//			
//			sess.setAttribute(UIConstants.SESS_LOGIN_ID, obj);
//			Executions.sendRedirect("/index.zul");
//			return;
//		}
		
		if (sess.hasAttribute("user")){
			Executions.sendRedirect("/index.zul");
			return;
		}
	}
}
