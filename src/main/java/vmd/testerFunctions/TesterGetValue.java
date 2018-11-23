package vmd.testerFunctions;


import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TesterGetValue {
	//Read file
    public String getData(String filename){
        FileInputStream finput = null;
        int data;
        String datafile = "";
        
        try{
            finput = new FileInputStream(filename);
        }catch(IOException e){
            System.out.println("File not found!");
        }
        
        
        try{
            //read data file
            while((data = finput.read())!=-1){
                datafile = datafile+(char)data;
//                System.out.print("Y");
            }
        }catch(Exception e){
            
        }
        
        return datafile;
    }
    
    //hitung status per assignee dengan input issue.csv
    public String[][] getTestingStatus(String datafile){
        String[][] percentageValue = null;
        int[] totalCount=null;
//        countStatus = new ArrayList<Integer>();
        FileInputStream finput = null;
        int data;
        String[] row;
        String[][] cell;
        int startDate_idx=0;
        int dueDate_idx=0;
        int actualStartDate_idx=0;
        int actualEndDate_idx=0;
        int[] assignee_idx = new int[36];
        
        String estimatedStartDate[];
        String estimatedEndDate[];
        String actualStartDate[];
        String actualEndDate[];
        int diffCount[][];
        String[][] statusVal;
        
//        String datafile = "";
//        //Getting file
//        try{
//            finput = new FileInputStream(filename);
//        }catch(IOException e){
//            System.out.println("File not found!");
//        }
//        
        
        try{
//            //read data file
//            while((data = finput.read())!=-1){
//                datafile = datafile+(char)data;
////                System.out.print("Y");
//            }
            
            //get row data
            row = datafile.split("\n");
            cell = new String[row.length][];
            
            //get column data
            for(int i=0; i<row.length; i++){
                cell[i] = row[i].split(",");
            }
            
            System.out.println();
            if(row.length > 0){
                //get index extimated start date 
                for(int i=0; i<cell[0].length; i++){
                    if(cell[0][i].equals("Start date")){
                        startDate_idx = i;
                        System.out.println("Start date : "+startDate_idx);
                        break;
                    }
                }
                
                //get index of Due date
                for(int i=0; i<cell[0].length; i++){
                    if(cell[0][i].equals("Due date")){
                        dueDate_idx = i;
                        System.out.println("Due Date : "+dueDate_idx);
                        break;
                    }
                }
                
                //get index of actual start date
                for(int i=0; i<cell[0].length; i++){
                    if(cell[0][i].equals("Actual Start Date")){
                        actualStartDate_idx = i;
                        System.out.println("Actual Start Date : "+actualStartDate_idx);
                        break;
                    }
                }
                
                for(int i=0; i<cell[0].length; i++){
                    if(cell[0][i].equals("Actual End Date")){
                        actualEndDate_idx = i;
                        System.out.println("Actual End Date : "+actualEndDate_idx);
                        break;
                    }
                }
                 
                //get assignee index on header
                int idxAssignee = 0;
                assignee_idx = new int[cell.length];
                for(int j=0; j<cell[0].length; j++){
                    if(cell[0][j].equals("Assignee")){
                        idxAssignee = j;
                        System.out.println("Assignee : "+idxAssignee);
                        break;
                    }
                }
                
                //get index column for any row
                for(int i=1; i<cell.length; i++){
                    int deduct = cell[i].length - cell[0].length;
                    //System.out.print(cell[i].length - cell[0].length);
                    assignee_idx[i-1] = idxAssignee+deduct;
                    
                    //System.out.println("Assignee : 8+"+(cell[i].length - cell[0].length)+" = "+assignee_idx[i-1]);
                }
                
            }
            
            //initialize variable for date (eStart, eEnd, aStart, aEnd)
            estimatedStartDate = new String[cell.length];
            estimatedEndDate = new String[cell.length];
            actualStartDate = new String[cell.length];
            actualEndDate = new String[cell.length];
            
            //fill every date from file
            for(int i=1; i<cell.length; i++){
                estimatedStartDate[i-1] = cell[i][startDate_idx];
                estimatedEndDate[i-1] = cell[i][dueDate_idx];
                actualStartDate[i-1] = cell[i][actualStartDate_idx];
                actualEndDate[i-1] = cell[i][actualEndDate_idx];
            }
            diffCount = this.countStatus(estimatedStartDate, estimatedEndDate, actualStartDate, actualEndDate);
            statusVal = new String[cell.length][2];
            
            //generate status per assignee
            for(int i=1; i<cell.length; i++){
                statusVal[i-1][0] = cell[i][assignee_idx[i-1]];
                //check diff end date
                if(diffCount[1][i-1]>=0){
                    statusVal[i-1][1] = "OnTrack";
                }else{
                    statusVal[i-1][1] = "Delay";
                }
                
                System.out.print(i+". "+statusVal[i-1][0]+" : ");
                System.out.println(statusVal[i-1][1]);
            }
            //GET RESUME. Sample Result : Nurmisari W Muhammad, On Track:5, Delay:4
            String[][] resume = this.getTotal(statusVal);
            
            System.out.println("\n");
            
            //GET PERCENTAGE. Sample Result : Nurul Dini Indriyani : 80%
            percentageValue = this.percentageValue(resume);
            
        }catch(Exception e){
            System.out.println("Error : "+e);
        }
        
        return percentageValue;
    }
    
    //hitung jumlah ontrack / delay
    public int[][] countStatus(String[] estimatedStartDate, String[] estimatedEndDate, String[] actualStartDate, String[] actualEndDate){
        int[] diffStartDate = new int[estimatedStartDate.length];
        int[] diffEndDate = new int[estimatedEndDate.length];
        int[][] count = new int[2][];//status[startDate, endDate][]
        
        //Count diff date
        for(int i=0; i<estimatedStartDate.length; i++){
            diffStartDate[i] = (int)this.getDifference(actualStartDate[i], estimatedStartDate[i]);
            diffEndDate[i] = (int)this.getDifference(actualEndDate[i], estimatedEndDate[i]);
        }
        count[0] = diffStartDate;
        count[1] = diffEndDate;
        
        return count;
    }
        
    //hitung selisih tanggal
    public long getDifference(String actualDate, String estimatedDate){
        long diffDate = 0;
        try{
            String aDate = "";
            String eDate = "";
            
            if(actualDate != null){
                String[] a = actualDate.split("/");
                if(a.length==3){
                    aDate = a[1]+"/"+a[0]+"/"+a[2];
                }
            }
            if(estimatedDate != null){
                String[] e = estimatedDate.split("/");
                if(e.length == 3){
                    eDate = e[1]+"/"+e[0]+"/"+e[2];
                }
            }
            
            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
//            if(!aDate.equals("") && eDate.equals("")){
                Date actual = (Date)date.parse(aDate);
                Date estimated = (Date)date.parse(eDate);
                diffDate = (actual.getTime() - estimated.getTime())/ (1000 * 60 * 60 * 24);
//            }
            
        }catch(ParseException e){
            //System.out.println(e);
        }
        
        return diffDate;
    }
        
    //Hitung delay / onTrack tiap orang
    public String[][] getTotal(String[][] subVal){
        ArrayList assignee = new ArrayList<>();
        String[][] resume;
        
        //GET ASSIGNEE COUNT
        for(int i=0; i<subVal.length-1; i++){
            if(assignee.isEmpty()){
//                System.out.println("subVal"+i+" : "+subVal[i][0]);
                System.out.println("empty assignee");
                assignee.add(subVal[i][0]);
            }else{
//                System.out.println("subVal"+i+" : "+subVal[i][0]);
                int temp = 0;
                for(int j=0; j<assignee.size(); j++){
                    if(subVal[i][0].equals(assignee.get(j))){
                        temp=1;
                    }
                }
                if(temp == 0){
                    assignee.add(subVal[i][0]);
                }
            }
        }
        
//        for(int i=0; i<assignee.size(); i++){
//            System.out.println(i+". "+assignee.get(i));
//        }
        
        //GET STATUS RESUME FOR ASSIGNEE. Sample Output :  VITHA RISTA POSTALIA, On Track:4, Delay:0
        resume=new String[assignee.size()][3];
        
        for(int i=0; i<assignee.size(); i++){
            int onTrack = 0;
            int delay = 0;
            for(int j=0; j<subVal.length; j++){
                if(assignee.get(i).equals(subVal[j][0])){
                    if(subVal[j][1].equals("OnTrack")){
                        onTrack++;
                    }else{
                        delay++;
                    }
                }
            }
            try{
                resume[i][0] = String.valueOf(assignee.get(i)); 
                resume[i][1] = "On Track:"+String.valueOf(onTrack);
                resume[i][2] = "Delay:"+String.valueOf(delay);
//                System.out.println("");
//                System.out.print((i+1)+". "+resume[i][0]+", "+resume[i][1]+", "+resume[i][2]);
            }catch(Exception e){
                System.out.println(e);
            }
        }
        System.out.println("");
        return resume;
    }
    
    
    //Hitung persentase OnTrack
    public String[][] percentageValue(String[][] statusValue){
        String[][] percentage = new String[statusValue.length][2];
        String[][] totalProject = new String[statusValue.length][2];
        for(int i=0; i<statusValue.length; i++){
//            System.out.println("statusValue-1 = "+statusValue[i][1]);
            String[] tOnTrack = statusValue[i][1].split(":");
            int onTrack = Integer.parseInt(tOnTrack[1]);
            String[] tDelay = statusValue[i][2].split(":");
            int delay = Integer.parseInt(tDelay[1]);
            totalProject[i][0] = statusValue[i][0];
            totalProject[i][1] = String.valueOf(onTrack+delay);
            percentage[i][0] = statusValue[i][0];
            percentage[i][1] = String.valueOf((onTrack*100)/(onTrack+delay));
//            System.out.println((i+1)+". "+percentage[i][0]+" : "+percentage[i][1]+"%");
        }
        
        
        return percentage;
    }
    
    public String[][] getRejectedDefect(String datafile){
        String[][] result = null;
        int getCountRejected = 0;
        int status_idx = 0;
        int author_idx = 0;
        //int count
        List<ArrayList> rejectionCount = new ArrayList<>();
        
        
        String[] row = datafile.split("\n");
        String[][] cell = new String[row.length][];
        for(int i=0; i<row.length; i++){
            cell[i] = row[i].split(",");
        }
        
        for(int i=0; i<cell[0].length; i++){
            if(cell[0][i].equals("Status"));
            status_idx = i;
            break;
        }
        
        for(int i=0; i<cell[0].length; i++){
            if(cell[0][i].equals("Author"));
            author_idx = i;
            break;
        }
        
        //GET ASSIGNEE COUNT
        ArrayList author = new ArrayList<>();
        for(int i=1; i<cell.length; i++){
            if(cell[i][status_idx].equals("Rejected")){
                author.add(cell[i+1][author_idx]);
                System.out.println("Rejected: "+cell[i+1][author_idx]);
            }
            
        }
        System.out.println("author.size : "+author.size());
        ArrayList<String> rejectionAuthor = new ArrayList<>();
        for(int i=0; i<cell.length; i++){
            if(rejectionAuthor.isEmpty()){
                rejectionAuthor.add(cell[i][author_idx]+",1");
            }else{
                int temp = 0;
                for(int j=0; j<rejectionAuthor.size(); j++){
                    String[] tempSplit = rejectionAuthor.get(j).split(",");
                    if(cell[i][author_idx].equals(tempSplit[0])){
                        tempSplit[1] = ""+Integer.parseInt((tempSplit[1])+1);
                        rejectionAuthor.set(j, tempSplit[0]+","+tempSplit[1]);
                        temp=1;
                    }
                }
                if(temp == 0){
                    rejectionAuthor.add(cell[i][author_idx]+",1");
                }
            }
        }
        
        result = new String[rejectionAuthor.size()][2];
        
        for(int i=0; i<result.length; i++){
            result[i] = rejectionAuthor.get(i).split(",");
        }
        return result;
        
    }
}
