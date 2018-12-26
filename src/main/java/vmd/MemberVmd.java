package vmd;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;

import core.dto.MemberDto;
import util.BaseUri;
import util.JsonUtil;
import util.RestResponse;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MemberVmd {
	
	private static Logger logger = LoggerFactory.getLogger(BaseVmd.class);
	private BaseUri uri = new BaseUri();
	private final String WS_URL = uri.BaseUriParam();
	
	private MemberDto memberDto = new MemberDto();
	private List<MemberDto> listMemberDto = new ArrayList<>();
	
	private boolean statusPopUp = false;private int indexHdr=0;
	
	@Init
	public void load(){
		String uri = WS_URL + "/member/all";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
		
		try {
			restResponse = bs.callWs(uri,null,HttpMethod.GET);
			listMemberDto = JsonUtil.mapJsonToListObject(restResponse.getContents(), MemberDto.class);
		} catch (Exception e) {
			System.out.println("Unable to convert JSON!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		//setIndexHdr(listMemberDto.size()+1);
	}
	
	public List<MemberDto> getAll(){
		String uri = WS_URL + "/member/all";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
		
		try {
			restResponse = bs.callWs(uri,null,HttpMethod.GET);
			listMemberDto = JsonUtil.mapJsonToListObject(restResponse.getContents(), MemberDto.class);
		} catch (Exception e) {
			System.out.println("Unable to convert JSON!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		//setIndexHdr(listMemberDto.size()+1);
		return this.listMemberDto;
	}
	
	
	@Command("add")
	@NotifyChange({ "statusPopUp", "memberDto"})
	public void add(){
		setStatusPopUp(true);
		memberDto = new MemberDto();
//		Sessions.getCurrent().setAttribute("member", memberDto);
//		Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
//		inc.setSrc("/member/memberedit.zul");
	}

	@Command("cancel")
	@NotifyChange("statusPopUp")
	public void backDetail() {
		setStatusPopUp(false);
	}
	
	@Command("save")
	@NotifyChange({ "listMemberDto", "statusPopUp", "memberDto" })
	public void save() {
//		Messagebox.show("check data " +deploymentDto.getDepName());
		
		String uriSave = WS_URL + "/member";
//		String uriAll = WS_URL + "/deployment/all";
		RestResponse restResponse = new RestResponse();
		BaseVmd bs = new BaseVmd();
		
		restResponse = bs.callWs(uriSave, memberDto, HttpMethod.POST);
		listMemberDto.add(memberDto);
		
		Clients.showNotification(restResponse.getMessage(),
				Clients.NOTIFICATION_TYPE_INFO, null, null, 1500);
		
		setStatusPopUp(false);
//		Include inc = (Include) Executions.getCurrent().getDesktop()
//				.getPage("index").getFellow("mainInclude");
//		inc.setSrc("/deployment/deployment.zul");
//		try {
//			restResponse = bs.callWs(uriAll,null,HttpMethod.GET);
//			listDeploymentDto =  JsonUtil.mapJsonToListObject(restResponse.getContents(), DeploymentDto.class);
//		} catch (Exception e) {
//			System.out.println("Unable to convert JSON!");
//			logger.error(e.getMessage());
//			e.printStackTrace();
//		}
		
		setIndexHdr(listMemberDto.size()+1);
	}
	
	@Command("edit")
	@NotifyChange({ "statusPopUp", "memberDto"})
	public void edit() {
		if (memberDto.getUsername() == null) {
			Messagebox.show("Pilih data yang akan di edit");
		} else {
			setStatusPopUp(true);
			Sessions.getCurrent().setAttribute("obj", memberDto);
//			Include inc = (Include) Executions.getCurrent().getDesktop()
//					.getPage("index").getFellow("mainInclude");
//			inc.setSrc("/deployment/deploymentedit.zul");
		}
	}
	
	
	@Command("delete")
	@NotifyChange({"includeSrc","p"})
	public void delete(){
		if (memberDto.getUsername()==null) {
			Messagebox.show("Pilih data yang akan dihapus");
		} else {
			String uriDelete = WS_URL + "/member/delete/"+memberDto.getId();
			RestResponse restResponse = new RestResponse();
			BaseVmd bs = new BaseVmd();
			
			restResponse = bs.callWs(uriDelete, null, HttpMethod.DELETE);
			listMemberDto.remove(memberDto);
			BindUtils.postNotifyChange(null, null, MemberVmd.this, "listMemberDto");
			
			Clients.showNotification(restResponse.getMessage(), Clients.NOTIFICATION_TYPE_INFO, null, null, 2000);
			Sessions.getCurrent().setAttribute("header", memberDto);
			Include inc = (Include) Executions.getCurrent().getDesktop().getPage("index").getFellow("mainInclude");
			inc.setSrc("/member/member.zul");
		}
	}
	
	
	
	
	public MemberDto getMemberDto() {
		return memberDto;
	}

	public void setMemberDto(MemberDto memberDto) {
		this.memberDto = memberDto;
	}

	public List<MemberDto> getListMemberDto() {
		return listMemberDto;
	}

	public void setListMemberDto(List<MemberDto> listMemberDto) {
		this.listMemberDto = listMemberDto;
	}

	public int getIndexHdr() {
		return indexHdr;
	}

	public void setIndexHdr(int indexHdr) {
		this.indexHdr = indexHdr;
	}
	
	public boolean isStatusPopUp() {
		return statusPopUp;
	}

	public void setStatusPopUp(boolean statusPopUp) {
		this.statusPopUp = statusPopUp;
	}
}
