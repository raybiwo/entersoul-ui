package vmd.testerFunctions;

import java.util.ArrayList;

public class CalculateSharingSession {
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
		System.out.println("cell.length : "+cell[0].length);
		for(int i=0; i<cell[0].length; i++){
            if(cell[0][i].equals(keywordCol)){
                index = i; 
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
	
	
	public String[][] getSharingCount(String[] author, String datafile){
		String[][] cellDatafile = this.getCellData(datafile);
		String[][] resultCount = new String[author.length][3];
		String[][] finalResult = new String[author.length][2];
		
		for (int i = 0; i < finalResult.length; i++) {
			for (int j = 0; j < finalResult[i].length; j++) {
				finalResult[i][j] = "";
			}
		}
		
		int authorIdx = this.getIndex(cellDatafile, "Author");
		int statusIdx = this.getIndex(cellDatafile, "Topic");
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
				}
			}
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
			int tempPercentage = 0;
			finalResult[i][0] = resultCount[i][0];
			
			if (Integer.parseInt(resultCount[i][1]) > 10) {
				tempPercentage = 100;
			} else {
				tempPercentage = Integer.parseInt(resultCount[i][1])*10 ;
			}
			
			resultCount[i][2] = ""+tempPercentage;
			finalResult[i][1] = ""+tempPercentage;
			
			
		}
		
		System.out.println("SHARING SESSION");
		for (int i = 0; i < resultCount.length; i++) {
			for (int j = 0; j < resultCount[i].length; j++) {
				System.out.print(resultCount[i][j]+",\t");
			}
			System.out.println();
		}
		
		return finalResult;
		
	}
}
