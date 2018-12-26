package pageservice;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SidebarPageConfigAjaxBasedImpl implements SidebarPageConfig{
	
	HashMap<String,SidebarPage> pageMap = new LinkedHashMap<String,SidebarPage>();
	public SidebarPageConfigAjaxBasedImpl(){
		pageMap.put("fn1",new SidebarPage("user","User","/imgs/fn.png","/member/member.zul"));
		pageMap.put("fn2",new SidebarPage("deployment","Deployment","/imgs/fn.png","/deployment/deployment.zul"));
		pageMap.put("fn3",new SidebarPage("tester","Generate KPI","/imgs/fn.png","/tester/tester.zul"));
		pageMap.put("fn4",new SidebarPage("testerList","Tester KPI","/imgs/fn.png","/tester/testerList.zul"));
		pageMap.put("fn5",new SidebarPage("attendance","Attendance","/imgs/fn.png","/attendance/attendance.zul"));

	}
	
	
	public List<SidebarPage> getPages(){
		return new ArrayList<SidebarPage>(pageMap.values());
	}
	
	public SidebarPage getPage(String name){
		return pageMap.get(name);
	}
	
}
