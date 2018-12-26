package vmd.testerFunctions;

public class TesterKPICount {
	public String[][] getResult(String[] tstr, String testExec){
        String result[][];
        TesterGetValue obj = new TesterGetValue();
        CalculateRejection objCR = new CalculateRejection();
        CalculateRIT objCRIT = new CalculateRIT();
        CalculateSharingSession objCSS = new CalculateSharingSession();
        CalculateAgileInvolvement objAI = new CalculateAgileInvolvement();
        
        String[] testerList = {"AYUNI UCHA","NELY HUDA DINI","SINARINO WICAKSONO","GILANG RIZKI GINANJAR","ANGGITA PRETTYSARI","MUHAMMAD ARDI YUDHAPRAWIRA","MUHAMMAD BAIHAQI","ANY LUTHFATUNNISA","HANIFAH TRI ASTUTI","AHMAD PRABAWA","NURUL DINI INDRIYANI","VITHA RISTA POSTALIA","REINHART BINSAR HAMONANGAN","RYAN ILHAM","PUTRI WIDIASTUTI"};
        String[][] agileList = {{"AXISNET","NELY HUDA DINI"},{"PREPAID SONIC","PUTRI WIDIASTUTI"},{"PREPAID BATMAN","SINARINO WICAKSONO"},{"XWG","ANGGITA PRETTYSARI"},{"JUMANJI","MUHAMMAD ARDI YUDHAPRAWIRA"},{"TOKO XL","MUHAMMAD BAIHAQI"},{"ENTERPRISE","ANY LUTHFATUNNISA"},{"POSTPAID","HANIFAH TRI ASTUTI"},{"AXIS","NURUL DINI INDRIYANI"}};
        String[][][] rules = { {{"TS","10"},{"SD","20"},{"RD","20"},{"RIT","20"},{"SS","10"},{"FTR","10"},{"AI","10"}}, {{"TS","20"},{"SD","20"},{"RD","20"},{"RIT","20"},{"SS","10"},{"FTR","10"},{"AI","0"}} };
        
        String datafileTestExec = obj.getData("D:\\NNH - XL\\Hackaton\\KPI July 2017\\Pre-prod Testing issues (Jan-June).csv");
        String[][] testExecution = obj.getTestingStatus(datafileTestExec);
        System.out.println(testExecution.length);
        String datafileScenarioDesign = obj.getData("D:\\NNH - XL\\Hackaton\\KPI July 2017\\Scenario Design Jan-June 2018.csv");
        String[][] scenarioDesign = obj.getTestingStatus(datafileScenarioDesign);
        
        String datafileRejection = obj.getData("D:\\NNH - XL\\Hackaton\\KPI July 2017\\Defect tracker Jan-June'18 selected column.csv");
        String[] authorRejection = objCR.getAuthor(datafileRejection);
        String[][] rejectionResult = objCR.getRejectedCount(authorRejection, datafileRejection);
        
        String datafileRIT = obj.getData("D:\\NNH - XL\\Hackaton\\SOURCE\\RIT Report Tester.csv");
        String[] authorRIT = objCRIT.getAuthor(datafileRIT);
        String[][] ritResult = objCRIT.getRejectedCount(authorRIT, datafileRIT);
        
        String datafileSharing = obj.getData("D:\\NNH - XL\\Hackaton\\SOURCE\\Sharing Session Report Tester.csv");
        String[] authorSharing = objCSS.getAuthor(datafileSharing);
        String[][] sharingResult = objCSS.getSharingCount(authorSharing, datafileSharing);
        
        String datafileAgile = obj.getData("D:\\NNH - XL\\Hackaton\\SOURCE\\Agile Involvement.csv");
        String[][] agileResult = objAI.getDataAgile(agileList, datafileAgile);
        result = new String[testerList.length][7];
        
        for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				result[i][j] = "";
			}
		}
        
        //add test execution percentage for any assignee
//        for(int i=0; i<testExecution.length; i++){
//            for(int j=0; j<testExecution[i].length; j++){
//                result[i][j] = testExecution[i][j];
//            }
//        }
        
        for (int i = 0; i < result.length; i++) {
			result[i][0] = testerList[i];        	
		}
        
        for (int i = 0; i < testExecution.length; i++) {
        	for(int j=0; j<result.length; j++){
                if(result[j][0].equals(testExecution[i][0].toUpperCase())){
                    result[j][1] = testExecution[i][1];
                }
            }
		}
        
//        System.out.println();
//        System.out.println();
//        //add scenario design count(%) into any assignee
//        for (int i = 0; i < scenarioDesign.length; i++) {
//        	System.out.println(scenarioDesign[i][0] +" >> "+scenarioDesign[i][1]);
//		}
        
        //ADD SCENARIO DESIGN VALUE
        for(int i=0; i<scenarioDesign.length; i++){
            for(int j=0; j<result.length; j++){
                if(result[j][0].equals(scenarioDesign[i][0].toUpperCase())){
                    result[j][2] = scenarioDesign[i][1];
                }
                
            }
            
        }
        
        //ADD REJECTION DEFECT VALUE
        for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < rejectionResult.length; j++) {
				if(result[i][0].equals(rejectionResult[j][0].toUpperCase())) {
					result[i][3] = rejectionResult[j][1];
				}
			}
		}
        
      //ADD RIT VALUE
        for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < ritResult.length; j++) {
				if(result[i][0].equals(ritResult[j][0].toUpperCase())) {
					result[i][4] = ritResult[j][1];
				}
			}
		}
        
      //ADD SHARING SESSION VALUE
        for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < sharingResult.length; j++) {
				if(result[i][0].equals(sharingResult[j][0].toUpperCase())) {
					result[i][5] = sharingResult[j][1];
				}
			}
		}
        
      //ADD AGILE INVOLVEMENT VALUE
        for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < agileResult.length; j++) {
				if(result[i][0].equals(agileResult[j][0].toUpperCase())) {
					result[i][6] = agileResult[j][1];
				}
			}
		}
        
        
        
        System.out.println("------------REJECTION----------------");
        			
        //PRINT REJECTION RESULT
        if (authorRejection != null) {
//	        for(int i=0; i<authorRejection.length; i++){
//	        	System.out.println(authorRejection[i]);
//	        }
	        
	        for (int i = 0; i < rejectionResult.length; i++) {
	        	System.out.print(i+". ");
				for (int j = 0; j < rejectionResult[i].length; j++) {
					System.out.print(rejectionResult[i][j]+"\t");
				}
				System.out.println();
			}
        }else {
        	System.out.println("NO REJECTION");
        }
        
        System.out.println("------------AGILE----------------");
		
        //PRINT AGILE INVOLVEMENT
        if (agileResult != null) {
//	        for(int i=0; i<authorRejection.length; i++){
//	        	System.out.println(authorRejection[i]);
//	        }
	        
	        for (int i = 0; i < agileResult.length; i++) {
	        	System.out.print(i+". ");
				for (int j = 0; j < agileResult[i].length; j++) {
					System.out.print(agileResult[i][j]+"\t");
				}
				System.out.println();
			}
        }else {
        	System.out.println("NO AGILE RESULT");
        }
        
        
        //PRINT RESULT
        System.out.println("");
        System.out.println("No, Assignee\t\t, Test Execution(10%), Scenario Design(20%), Inaccurate or Rejected Defect(20%), Test Automation - RIT (10%)");
        for(int i=0; i<result.length; i++){
            System.out.print((i+1)+". ");
            for(int j=0; j<result[i].length; j++){
                System.out.print(result[i][j]);
                if(j==0) {
	                if(result[i][0].length()<=10) {
	                	System.out.print("\t\t\t");
	                }else if(result[i][0].length()>20){
	                	System.out.print("\t");
	                }else {
	                	System.out.print("\t\t");
	                }
                }else {
                	System.out.print("\t\t");
                }
            }
            System.out.println("");
        }
        
        return result;
    }
    
    
    public static void main(String[] args){
        TesterKPICount objTC = new TesterKPICount();
//        objTC.getResult();
    }
}
