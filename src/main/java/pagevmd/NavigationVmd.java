package pagevmd;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;

import pageservice.SidebarPage;

public class NavigationVmd {
	private String includeSrc = "/transaksi/users/users.zul";

	@GlobalCommand("onNavigate")
	@NotifyChange("includeSrc")
	public void onNavigate(@BindingParam("page") SidebarPage page) {
		String locationUri = page.getUri();
		String name = page.getName();
		
		//redirect current url to new location
		if(locationUri.startsWith("http")){
			//open a new browser tab
			Executions.getCurrent().sendRedirect(locationUri);
		} else {
			includeSrc = locationUri;
			
			//advance bookmark control, 
			//bookmark with a prefix
			if(name!=null){
				Executions.getCurrent().getDesktop().setBookmark("p_"+name);
			}
		}
	}

	public String getIncludeSrc() {
		System.out.println("includeSrc = "+includeSrc);
		return includeSrc;
	}
}
