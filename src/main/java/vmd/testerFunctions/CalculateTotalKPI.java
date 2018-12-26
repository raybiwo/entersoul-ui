package vmd.testerFunctions;

public class CalculateTotalKPI {
	public String[][] calculateKPI(String[][][] rules, String[][] valCriteria, String[][] agileStream, String[] testerName){
		/* rules >> 
		 * 			agile[array[criteria][value %]]
		 * 			non agile[array[criteria][value %]]
		*/
		
		/*valCriteria >>
		 * 	name, valueCriteria1, valueCriteria2, valueCriteria.., valueCriteria(n)
		 * 
		 */
		
		/*agileStream >>
		 * 	agileName, testerName
		 * 
		 */
		
		String[][] agilePIC = new String[valCriteria.length][2];
		String[][] result = new String[valCriteria.length][valCriteria[0].length+1];
		
		System.out.println("\nCALCULATE TOTAL");
		
		for (int i = 0; i < valCriteria.length; i++) {
			agilePIC[i][0] = valCriteria[i][0];
			for (int j = 0; j < agileStream.length; j++) {
//				System.out.println(agileStream[j][1]);
				if(agilePIC[i][0].equals(agileStream[j][1])) {
					agilePIC[i][1] = "AGILE";
				}
			}
		}
		
		for (int i = 0; i < agilePIC.length; i++) {
			if (agilePIC[i][1] == null) {
				agilePIC[i][1] = "";
			}
			System.out.print(agilePIC[i][0]+", "+agilePIC[i][1]);
			System.out.println();
		}
		
		for (int i = 0; i < agilePIC.length; i++) {
			if(agilePIC[i][1].equals("") || agilePIC[i][1] == null) {
				agilePIC[i][1] = "NON AGILE";
			}
		}
		
		
		//COUNT TOTAL
//		for (int i = 0; i < result.length; i++) {
//			result[i][0] = agilePIC[i][0];
////			for (int j = 0; j < rules.length; j++) {
//				//get value criteria / rule (%)
//				if(agilePIC[i][1].equals("AGILE")) {
//					for (int j2 = 0; j2 < valCriteria.length; j2++) {
//						// find name to get value per criteria
//						if(result[i][0].equals(valCriteria[j2][0])) {
//							int temp1 = Integer.parseInt(valCriteria[j2][1]) * Integer.parseInt(rules[0][0][1]);
//							int temp2 = Integer.parseInt(valCriteria[j2][2]) * Integer.parseInt(rules[0][1][1]);
//							int temp3 = Integer.parseInt(valCriteria[j2][3]) * Integer.parseInt(rules[0][2][1]);
////							int temp3 = Integer.parseInt(valCriteria[j2][3]) * Integer.parseInt(rules[0][2][1]);
//						}
//					}
//				}
////			}
//		}
		
		
		//ADD rules percentage into every author
		for (int i = 0; i < valCriteria.length; i++) {
			for (int j = 0; j < agilePIC.length; j++) {
				
				if(valCriteria[i][0].equals(agilePIC[j][0])) {
					System.out.println("if(valCriteria[i][0].equals(agilePIC[j][0]))");
					
					if (agilePIC[j][1].equals("AGILE")) {
						System.out.println("if (agilePIC[j][1].equals(\"AGILE\"))");
						
						for (int j2 = 1; j2 < valCriteria[i].length; j2++) {
							valCriteria[i][j2] = valCriteria[i][j2] + ";" + rules[0][j2-1][1];
							System.out.println(valCriteria[i][j2]);
						}
					}else {
						for (int j2 = 1; j2 < valCriteria[i].length; j2++) {
							valCriteria[i][j2] = valCriteria[i][j2] + ";" + rules[1][j2-1][1];
							System.out.println(valCriteria[i][j2]);
						}
					}
				}
			}
		}
		
		for (int i = 0; i < valCriteria.length; i++) {
			for (int j = 0; j < valCriteria[i].length; j++) {
				System.out.print(valCriteria[i][j]+", ");
			}
			System.out.println();
		}
		
		//calculate value * percentage
		for (int i = 0; i < valCriteria.length; i++) {
			result[i][0] = valCriteria[i][0];
			double temp[] = new double[valCriteria[i].length];
			double total = 0.0;
			for (int j = 1; j < valCriteria[i].length; j++) {
				System.out.println("valCriteria["+i+"]["+j+"] : "+valCriteria[i][j]);
				//copy valCriteria value into result >> exclude total
				result[i][j] = valCriteria[i][j];
				
				//count criteria value
				String tempSplit[] = valCriteria[i][j].split(";");
				double counter = 0.0;
//				String value0 = tempSplit[0];
				System.out.println("tempSplit[0]"+tempSplit[0]);  
				System.out.println("tempSplit[1]"+tempSplit[1]);
				if(tempSplit[0].equals("")) {
					//System.out.println("int tempPercent = "+Integer.parseInt(tempSplit[1])+"/ 100 * "+ Integer.parseInt(tempSplit[0]));
					tempSplit[0] = "0";
					counter = Double.parseDouble(tempSplit[0]);
				}else {
					counter = Double.parseDouble(tempSplit[0]);
				}
				double tempPercent = Double.parseDouble(tempSplit[1]);
				double percentValue = tempPercent/100.0;
				System.out.println("tempPercent : "+percentValue*counter);
				double assign = percentValue*counter;
				temp[j] = percentValue*counter;
				
				//count total
				
				
			}
			for (int k = 0; k < temp.length; k++) {
				total = total + temp[k];
			}
			total = Math.round(total);
			result[i][valCriteria[i].length] = total+"";
			
		}
		
		
		return result;
	}
}
