package vmd.testerFunctions;

import java.util.ArrayList;

public class CalculateRIT {
	private String[][] getCellData(String datafile) {
		String[] row = datafile.split("\n");
        String[][] cell = new String[row.length][];
        for(int i=0; i<row.length; i++){
            cell[i] = row[i].split(",");
//            for (int j = 0; j < cell[i].length; j++) {
//            	System.out.print(cell[i][j]);				
//			}
            
        }
        
        return cell;
	}
	
	
	private int getIndex(String[][] cell, String keywordCol) {
		int index=0;
		System.out.println("cell.length : "+cell[0].length);
		for(int i=0; i<cell[0].length; i++){
            if(cell[0][i].equals(keywordCol)){
                index = i;
//                System.out.println("Status idx : "+status_idx);
                break;
            }
        }
		
		return index;
	}
	
	public String[] getAuthor(String datafile){
        String[] resultAllAuthor;
        String[][] cell = this.getCellData(datafile);
        
        int author_idx = this.getIndex(cell, "Author");       
        
      //GET Author COUNT -- Sort name
        String[] author;
        
        //GET AUTHOR FROM FILR
        author = new String[cell.length-1];
        ArrayList<String> authorList = new ArrayList<>();
        for(int i=1; i<cell.length; i++){
        	for (int j = 0; j < cell[i].length; j++) {
				if(j == author_idx) {
					author[i-1] = cell[i][j];
				}
			}
        }
        
        
        //GET ASSIGNEE COUNT
        for(int i=0; i<author.length; i++){
//        	System.out.println("assignee sorting "+author[i]);
            if(authorList.size() == 0){
//                System.out.println("subVal"+i+" : "+subVal[i][0]);
                System.out.println("empty assignee");
                authorList.add(author[0]);
//                System.out.println("authorList-"+i+" >> "+authorList.get(i));
//                System.out.println(authorList.size()+" "+authorList.get(authorList.size()-1));
            }else{
//                System.out.println("subVal"+i+" : "+subVal[i][0]);
                if(authorList.size()>0) {
                	int temp = 0;
                	for(int j=0; j<authorList.size(); j++){
                        if(author[i].contains((authorList.get(j).toString()))){
//                        	System.out.println("author-"+i+" : "+author[i]+" <<contains>> "+authorList.get(j));
                            temp=1;
                        }
                    }
                    if(temp == 0){
                        authorList.add(author[i]);
                    }
//                    System.out.println(authorList.size()+" "+authorList.get(authorList.size()-1));
                }
            }
//            System.out.println("AuthorList-"+authorList.size()+" >> "+authorList.get(authorList.size()-1));
        }
        
        resultAllAuthor = new String[authorList.size()];
        for(int i = 0; i<resultAllAuthor.length; i++) {
        	resultAllAuthor[i] = authorList.get(i).toString();
        }
        return resultAllAuthor;
        
    }
	
	
	public String[][] getRejectedCount(String[] author, String datafile){
		String[][] cellDatafile = this.getCellData(datafile);
		String[][] resultCount = new String[author.length][3];
		String[][] finalResult = new String[author.length][2];
		
		int authorIdx = this.getIndex(cellDatafile, "Author");
		int statusIdx = this.getIndex(cellDatafile, "RIT");
		System.out.println("authorIdx : "+authorIdx);
		System.out.println("statusIdx : "+statusIdx);
		
		for (int i = 0; i < author.length; i++) {
			resultCount[i][0] = author[i];
			resultCount[i][1] = "";
			resultCount[i][2] = "";		
		}
		
		
		for (int i = 0; i < resultCount.length; i++) {
			for (int j = 0; j < cellDatafile.length; j++) {
				//System.out.println(cellDatafile[j][authorIdx]+", "+cellDatafile[j][statusIdx]);
				//get total count per author
				if(cellDatafile[j][authorIdx].equals(resultCount[i][0])) {
					if(resultCount[i][1].equals("")) {
						resultCount[i][1] = ""+1;
					}else {
						int temp = Integer.parseInt(resultCount[i][1]);
						int tempCountTotal = temp+1;  
						resultCount[i][1] = ""+tempCountTotal;
					}
					
					//get RIT count author
					if(cellDatafile[j][statusIdx].equals("N")) {
						if(!resultCount[i][2].equals("")) {
							int tempCountRIT= Integer.parseInt(resultCount[i][2])+1;  
							resultCount[i][2] = ""+tempCountRIT;
						}else {
							resultCount[i][2] = ""+1;
						}
					}
				}
			}
		}
		
		
		for (int i = 0; i < resultCount.length; i++) {
			for (int j = 0; j < resultCount[i].length; j++) {
				System.out.print(resultCount[i][j]+",\t");
			}
			System.out.println();
		}
		
		for (int i = 0; i < resultCount.length; i++) {
			if(resultCount[i][1].equals("")) {
				resultCount[i][1] = ""+0;
			}
			if(resultCount[i][2].equals("")) {
				resultCount[i][2] = ""+0;
			}
		}
		
		for (int i = 0; i < resultCount.length; i++) {
			finalResult[i][0] = resultCount[i][0];
			int tempPercentage = (Integer.parseInt(resultCount[i][1]) - Integer.parseInt(resultCount[i][2]))*100 / Integer.parseInt(resultCount[i][1]);
			finalResult[i][1] = ""+tempPercentage;
			
			
		}
		
		return finalResult;
		
	}
	
	
}
