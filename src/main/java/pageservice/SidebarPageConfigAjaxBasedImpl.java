package pageservice;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SidebarPageConfigAjaxBasedImpl implements SidebarPageConfig{
	
	HashMap<String,SidebarPage> pageMap = new LinkedHashMap<String,SidebarPage>();
	public SidebarPageConfigAjaxBasedImpl(){
		pageMap.put("fn1",new SidebarPage("user","User","/imgs/fn.png","/transaksi/users/users.zul"));
		pageMap.put("fn2",new SidebarPage("deployment","Deployment","/imgs/fn.png","/deployment/deployment.zul"));
		pageMap.put("fn3",new SidebarPage("tester","Tester","/imgs/fn.png","/tester/tester.zul"));
//		pageMap.put("fn2",new SidebarPage("barang","Barang","/imgs/fn.png","/master/barang/barang.zul"));
//		pageMap.put("fn2",new SidebarPage("masuk","Transaksi Masuk","/imgs/fn.png","/transaksi/masuk/trmasuk.zul"));
		
//		pageMap.put("fn4",new SidebarPage("customer","Customer","/imgs/fn.png","/master/customer/customer.zul"));
//		pageMap.put("fn5",new SidebarPage("jenisbarang","Jenis Barang","/imgs/fn.png","/master/JenisBarang/jenisBarang.zul"));
//		pageMap.put("fn6",new SidebarPage("job","Job Karyawan","/imgs/fn.png","/master/job/job.zul"));
//		pageMap.put("fn7",new SidebarPage("karyawan","Karyawan","/imgs/fn.png","/master/karyawan/karyawan.zul"));
//		pageMap.put("fn8",new SidebarPage("merkbarang","Merk Barang","/imgs/fn.png","/master/merkBarang/merkBarang.zul"));
//		pageMap.put("fn9",new SidebarPage("supplier","Supplier","/imgs/fn.png","/master/supplier/supplier.zul"));
//		pageMap.put("fn10",new SidebarPage("toko","Toko","/imgs/fn.png","/master/toko/toko.zul"));
	}
	
	
	public List<SidebarPage> getPages(){
		return new ArrayList<SidebarPage>(pageMap.values());
	}
	
	public SidebarPage getPage(String name){
		return pageMap.get(name);
	}
	
}
