package pagevmd;

import java.util.List;

import pageservice.SidebarPage;
import pageservice.SidebarPageConfig;
import pageservice.SidebarPageConfigAjaxBasedImpl;

public class SidebarAjaxBasedVmd {


	private SidebarPageConfig pageConfig = new SidebarPageConfigAjaxBasedImpl();
	
	public List<SidebarPage> getSidebarPages() {
		return pageConfig.getPages();
	}
}
