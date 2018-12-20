package vmd.testerFunctions;

public class CalculateAgileInvolvement {
	private String[][] getCellData(String datafile) {
		String[] row = datafile.split("\n");
        String[][] cell = new String[row.length][];
        for(int i=0; i<row.length; i++){
            cell[i] = row[i].split(",");
        }
        
        return cell;
	}
	
	
	private int getIndex(String[][] cell, String keywordCol) {
		int index=0;
		for(int i=0; i<cell[0].length; i++){
            if(cell[0][i].equals(keywordCol)){
                index = i;
//                System.out.println("Status idx : "+status_idx);
                break;
            }
        }
		
		return index;
	}
	
	public String[][] getDataAgile(String[][] agileStreamTester, String datafile) {
		String[][] resultAgile = new String[agileStreamTester.length][2];
		String[][] cellData = this.getCellData(datafile);
		
		for (int i = 0; i < resultAgile.length; i++) {
			for (int j = 0; j < resultAgile[i].length; j++) {
				resultAgile[i][j] = "";
			}
		}
		
		int scrumIdx = this.getIndex(cellData, "Project Name");
		int totalIdx = this.getIndex(cellData, "Total");
		
		for (int i = 0; i < agileStreamTester.length; i++) {
			resultAgile[i][0] = agileStreamTester[i][1];
		}
		
		for (int k = 0; k < resultAgile.length; k++) {
			for (int i = 0; i < agileStreamTester.length; i++) {
				for (int j = 0; j < cellData.length; j++) {
					//if agileStreamTester.projectname = cellData.projectname
					if (agileStreamTester[i][0].equals(cellData[j][scrumIdx])) {
						//if resultAgile.name = agileStreamTester.name
						if (resultAgile[k][0].equals(agileStreamTester[i][1])) {
							//resultAgile.value = cellData.total
							resultAgile[k][1] = cellData[j][totalIdx];
						}
					}
				}
			}
		}
		
		
		return resultAgile;
	}
}
