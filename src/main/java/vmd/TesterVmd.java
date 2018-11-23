package vmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.plaf.FileChooserUI;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.util.media.Media;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zhtml.Fileupload;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Messagebox;

import vmd.testerFunctions.TesterGetValue;



public class TesterVmd {
	Media media;

	@Command
	public void doUpload(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException {
		TesterGetValue objtgv = new TesterGetValue();
		UploadEvent upEvent = null;
		Object objUploadEvent = ctx.getTriggerEvent();
		if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
			upEvent = (UploadEvent) objUploadEvent;
		}
		if (upEvent != null) {
//			media = Fileupload.get(); 
//			File f = new File(media.getName());
//			InputStream inputStream= media.getStreamData();
//	        OutputStream out;
//			out = new FileOutputStream(f);
//			byte buf[]=new byte[1024];
//		    int len;
//		    while((len=inputStream.read(buf))>0)
//		    out.write(buf,0,len);
//		    out.close();
//		    inputStream.close();
//	        
//
//	        String filepath = f.getAbsolutePath();
////	        mdbConverter mdb = new mdbConverter();
//
//	        Messagebox.show(filepath);
//	        }
			upEvent.getMedia();
			java.io.File myFile = new java.io.File(media.getName());
			
//			myFile.renameTo("D:\\NNH - XL\\Hackaton\\KPI July 2017\\"+media.getName());
			myFile.getPath();
			//get percentage of test execution status (Ontrack/Delay)
			String datafile = objtgv.getData("D:\\NNH - XL\\Hackaton\\KPI July 2017\\Pre-prod Testing issues (Jan-June).csv");
			String[][] testExecution = objtgv.getTestingStatus(datafile);
			String result = "";
			for(int i=0; i<testExecution.length; i++){
	            for(int j=0; j<2; j++){
	                result = result+testExecution[i][j];
	            }
	            result = result+"\n";

	        }
			Messagebox.show("File Uploaded: "+myFile.getAbsoluteFile());
			//Messagebox.show("File Uploaded: " + this.getData("D:\\NNH - XL\\Hackaton\\KPI July 2017\\Pre-prod Testing issues (Jan-June).csv"));
			
		}
	}
	
	
	@Command
	public void doDownload() {
//		if (media != null)
//			Filedownload.save(media);
//		else
//			Messagebox.show("First Drop Your File");
//		
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Save Log File as"); 
		chooser.setSelectedFile(new File("AttendenceBug.html"));

	}
}
