import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays; 
/**
Maciej Nowaczyk - clustering algorithm for DHL (logistic context)
 */
public class Clustering_DHL {
    private static BufferedReader in = null;
    private static int rows = 0;
    private static int columns = 0;
    private static double[][] matrix = null;
    public static void main(String []args) throws Exception {

        try {
            String filepath = args[0];
            int lineNum = 0;
            int row=0;
            in = new BufferedReader(new FileReader(filepath));
            String line = null;
            while((line=in.readLine()) !=null) {
                lineNum++;
                if(lineNum==1) {
                    rows = Integer.parseInt(line);
                } else if(lineNum==2) {
                    columns = Integer.parseInt(line);
                    matrix = new double[rows][columns];
                } else {
                    String [] tokens = line.split(";");
                    for (int j=0; j<tokens.length; j++) {
                        matrix[row][j] = Double.parseDouble(tokens[j]);
                    }
                    row++;
                }
            }
        } catch (Exception ex) {
            System.out.println("The code throws an exception");
            System.out.println(ex.getMessage());
        } finally {
            if (in!=null) in.close();
        }
        //
        int question=Integer.parseInt(args[1]);         
         if (question==4){
        int subquestion=Integer.parseInt(args[2]);
        if(subquestion==1){
         resoricluster(matrix);
        }
        else if(subquestion==2){
          resdescluster(matrix);
        }
        else{
        System.out.println("please input 1 or 2: 1 for original cluster, 2 for destination cluster.");
        }
         }
         else if(question==5){
         int k = Integer.parseInt(args[2]);
          if(k>2){
         reskmeansdis(matrix,k);
         }
         else{
         System.out.println("please input k > 2");
         }
         }
          else if(question==6){
         int k = Integer.parseInt(args[2]);
         if(k>2){
         reskmeansflow(matrix,k);
         }
         else{
         System.out.println("please input k > 2");
         }
         }
          else{
        System.out.println("Clustering_DHL:I can only solve question 4 to 6");
          }
    }
//question 4
//resoricluster is combined all method to get the answers for original clusters 
    public static void resoricluster(double[][] matrix){
      double[][] sheettwo=new double[2918][7];//transfer part 2 chart to a matrix
      sheettwo = createst(matrix);
      double[][] oricluster=new double[1026][7];//create a 1026*7 matrix to save original clusters value, named oricluster
      oricluster = createoc(sheettwo);
      setnum(matrix,oricluster,8);//match the numeric cluster with shipments for original clusters in oricluster
      totalwvs(matrix,oricluster,8);//input total weight, volumn, shipunits for original clusters in oricluster
      totald(matrix,oricluster,8);//input distance for original clusters in oricluster
      output(oricluster);
    }
 //resdescluster is combined all method to get the answers for destination clusters    
    public static void resdescluster(double[][] matrix){
      double[][] sheettwo=new double[2918][7];//transfer part 2 chart to a matrix
      sheettwo = createst(matrix);
      double[][] oricluster=new double[1026][7];//create a 1026*7 matrix to save destination clusters value, named oricluster
      oricluster = createoc(sheettwo);
      setnum(matrix,oricluster,11);//match the numeric cluster with shipments for destination clusters in oricluster
      totalwvs(matrix,oricluster,11);//input total weight, volumn, shipunits for destination clusters in oricluster
      totald(matrix,oricluster,11);//input distance for destination clusters in oricluster
      output(oricluster);
    }


//1.create a matrix[2918][3] to read sheet2, which contains the information between 2918 locations and 1026 clusters.
   public static double[][] createst(double[][] matrix){
    double[][] sheettwo=new double[2918][7];
 for (int i=0;i<2918;i++){
        sheettwo[i][0]=matrix[i][12];//12=lentitude
        sheettwo[i][1]=matrix[i][13];//13=longitude
        sheettwo[i][2]=matrix[i][14];//14=numeric cluster      
    }
    return sheettwo;
    }
//2.create a new matrix[1026][7] to save the value of 1026 clusters
   //for columns:
   //1=latitude;2=longtitude;3=numeric of clusters;4=weight;5=volumn;6=Nb of shipunits;7=distance
    public static double[][] createoc(double[][] sheettwo){
    double[][] oricluster=new double[1026][7];
    int m=0;
    for (int i=0;i<sheettwo.length;i++){      
        if(i==sheettwo.length-1){
        oricluster[m][0]=sheettwo[i][0];
        oricluster[m][1]=sheettwo[i][1];
        oricluster[m][2]=sheettwo[i][2];
        break;
        }
        else{
        //remove the repetition of clusters  
          if(sheettwo[i][2]!=sheettwo[i+1][2]){
        oricluster[m][0]=sheettwo[i][0];
        oricluster[m][1]=sheettwo[i][1];
        oricluster[m][2]=sheettwo[i][2];
        m++;          
        }      
    }
    }
    return oricluster;
    }
    
    
//3.set numeric cluster to each locations
    //e.g. if a shipment is delieverd to original cluster550, then we imput with num=8(numeric original cluster)
    //set 550 to column 8 of this shipment  
   public static void setnum(double[][] matrix,double[][] oricluster,int num){
  for (int i=0;i<matrix.length;i++){
      for(int j=0;j<oricluster.length;j++){
        if(matrix[i][num-2]==oricluster[j][0]){
          if(matrix[i][num-1]==oricluster[j][1]){
            matrix[i][num]=oricluster[j][2];
          }
        }
      }
  }
  }

//4.calculate the total value of each category from each location to each clusters
    public static double[][] totalwvs(double[][] matrix,double[][] clusterll,int num){    
      for(int j=0;j<clusterll.length;j++){
    for (int i=0;i<matrix.length;i++){
     
        if (matrix[i][num]==clusterll[j][2]){
          //we need value of weight,volumn,shipunits(from column 4 to 6)
           for(int c=3;c<6;c++){
        clusterll[j][c]=clusterll[j][c]+matrix[i][c];        
        }       
        }
        }
      }
    return clusterll;
  }
 //5.calculate the total value of each category from each location to each clusters
    //input the result to column 7
public static double[][] totald(double[][] matrix,double[][] clusterll,int num){   
 for(int j=0;j<clusterll.length;j++){
    for (int i=0;i<matrix.length;i++){      
        if (matrix[i][num]==clusterll[j][2]){          
          clusterll[j][6]=haversine(matrix[i][0],matrix[i][1],clusterll[j][0],clusterll[j][1])+clusterll[j][6];   
        }
      }
      }
    return clusterll;
  }
    //6.output: output the answers of given clusters
    public static void output(double[][] clusterll){
      String[] name=new String[]{"Lens","Long","num","weight","volumn","shipment","distance"};
      int pos=7;
      //output weight, volumn, shipment, distance respectively
      for(int i=0;i<pos-3;i++){
      int num=i+3;
      double avew=sum(clusterll,num)/clusterll.length;
      sorthtl(clusterll,num);
      System.out.println("=====================================");
      System.out.println("the three highest"+" "+name[num]+" "+"are :");
      System.out.println("cluster "+clusterll[0][2]+" with value "+clusterll[0][num]);
      System.out.println("cluster "+clusterll[1][2]+" with value "+clusterll[1][num]);
      System.out.println("cluster "+clusterll[2][2]+" with value "+clusterll[2][num]);
      System.out.println("-------------------------------------");
      System.out.println("the three lowest"+" "+name[num]+" "+"are :");
      System.out.println("cluster "+clusterll[clusterll.length-1][2]+" with value "+clusterll[clusterll.length-1][num]);
      System.out.println("cluster "+clusterll[clusterll.length-2][2]+" with value "+clusterll[clusterll.length-2][num]);
      System.out.println("cluster "+clusterll[clusterll.length-3][2]+" with value "+clusterll[clusterll.length-3][num]);
      System.out.println("-------------------------------------");
      System.out.println("the average"+" "+name[num]+" "+"is :");
      System.out.println(avew);
      System.out.println("=====================================");
     
      }
    }
    
//method to calculate the distance between 2 location with latitude and longtitude by harversine fomula
  public static double haversine(double lat1, double long1,double lat2,double long2){
  final int R = 6371;//R is earth mean radian = 6371 km
  
  double dlat = Math.toRadians((lat2-lat1));//difference of latitude in radian
  double dlong = Math.toRadians((long2-long1));//difference of longitude in radian
  
  double rdlat1=Math.toRadians(lat1);//lat1 in radian
  double rdlat2=Math.toRadians(lat2);//lat2 in radian
  
  double a = Math.pow(Math.sin(dlat/2),2)+Math.cos(rdlat1)*Math.cos(rdlat2)*Math.pow(Math.sin(dlong/2),2);
  double c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
  double distance = R*c;
  return distance;
  }

//method to sort a matrix by a given column from high to low
  public static void sorthtl (double[][] T,int c){
    double temp=0;
  for (int i=0;i<T.length;i++){
    for (int j=i+1;j<T.length;j++){
      if(T[i][c]<T[j][c]){
        for(int m=0;m<T[i].length;m++){
         temp=T[i][m];
        T[i][m]=T[j][m];
        T[j][m]=temp;
        }
      }
    }
  }
}   

//method to aggregate the data to have only one observation per cluster
public static double sum(double[][] clusterll,int n){
double sumall=0;
for (int i=0;i<clusterll.length;i++){
  sumall=sumall+clusterll[i][n];
}
return sumall;
}

//question 5
//combine the methods and output the three highest(lowest) and average value
   public static void reskmeansdis(double[][] matrix,int k){
      //kmeansdis(matrix,k);
      double[][] oricluster=new double[k][7];
      oricluster = kmeansdis(matrix,k);
      setnum(matrix,oricluster,17);//input value to column 18
     
      totalwvs(matrix,oricluster,17);//calculate the total value of new optimal clusters
      totald(matrix,oricluster,17);//distance
      output(oricluster);
    }
   
     //combine the methods to return an optimal cluster set 
public static double[][] kmeansdis(double matrix[][], int k){
      double[][] matrixdis=matrixdis(matrix);     
      double[][] newclusters=new double[k][7];
      newclusters = newrandomcluster(matrixdis,k);//new random clusters
      
      testdistance(matrixdis,newclusters);      
      double[][] clustercopy=new double[k][7];
      clustercopy=copy(newclusters);//copy this matrix to compare with the new one
      resetmatrix(newclusters);    
      optimalcluster(matrixdis,newclusters);
      finalcluster(newclusters,clustercopy,matrixdis);
      
      matchnum(matrix,matrixdis,17);//match the location from matrixdis to original matrix
      return newclusters;
}


//method to match the distance matrix with original matrix   
   public static void matchnum(double[][] matrix,double[][] oricluster,int num){
  for (int i=0;i<matrix.length;i++){
      for(int j=0;j<oricluster.length;j++){
        if(matrix[i][0]==oricluster[j][0]){
          if(matrix[i][1]==oricluster[j][1]){
            matrix[i][num]=oricluster[j][17];//numeric
            matrix[i][num-1]=oricluster[j][16];//longitude
            matrix[i][num-2]=oricluster[j][15];//latitude
          }
        }
      }
  }
  } 

 //method to create a 2918*19 distance matrix  
   public static double[][] matrixdis(double[][] matrix){
   double[][] matrixdis=new double[2918][19];
   //input part2 in chart to the 2918*19 matrix, col 0=lentitude, col 1= longitude
       for (int i=0;i<2918;i++){      
        matrixdis[i][0]=matrix[i][12];//latitude
        matrixdis[i][1]=matrix[i][13];//longitude        
     }
       return matrixdis;
   }
 




//1.give a set of random cluster with given k
  public static double[][] newrandomcluster(double matrix[][], int k){
  double newclusters[][]= new double[k][7];
  for(int i=0;i<k;i++){
    //our locations lat bound is(36.42,56.45),long bound is (-8.54,14.88)
  newclusters[i][0]=Math.random()*21+36;//get a random number in(36,57)
  newclusters[i][1]=15-Math.random()*24;//get a random number in(-9,15)
  newclusters[i][2]=i+1;
  }
  return newclusters;
  }

  //2.find the distance from locations to clusters
  public static double[][] testdistance (double[][] matrix,double[][] newclusters){
    //calculate the distance from each location to each cluster and return the input shortest distance 
    //firstly, we suppose each location to cluster 1 is with shortest distance
    for (int i=0;i<matrix.length;i++){
        matrix[i][18]=haversine(matrix[i][0],matrix[i][1],newclusters[0][0],newclusters[0][1]);
        matrix[i][15]=newclusters[0][0];//lat
        matrix[i][16]=newclusters[0][1];//long
        matrix[i][17]=newclusters[0][2];//num
     for (int j=0;j<newclusters.length;j++){
        //for j cluster, if the distance for j cluster is shorter than the distance what we have now
        //than we take this cluster as more efficient cluster
        double distance=haversine(matrix[i][0],matrix[i][1],newclusters[j][0],newclusters[j][1]);
        if(matrix[i][18]>distance){
        matrix[i][18]=haversine(matrix[i][0],matrix[i][1],newclusters[j][0],newclusters[j][1]);
        matrix[i][15]=newclusters[j][0];//lat
        matrix[i][16]=newclusters[j][1];//long
        matrix[i][17]=newclusters[j][2];//num
        }      
      }
    }
    return matrix;
  }
  //3.copy and reset
  //copy the newclusters to a new matrix
  public static double[][] copy(double newclusters[][]){
  double clustercopy[][]=(double[][]) newclusters.clone();
  return clustercopy;
  }
  
  //reset the lens and long of newclusters to 0
  public static double[][] resetmatrix(double [][] newclusters){
  for (int i=0;i<newclusters.length;i++){
   for (int j=0;j<2;j++){
     newclusters[i][j]=0;
   }
  }
  return newclusters;
  }
    
  //4.optimal a new clusters set  
  public static double[][] optimalcluster(double[][] matrix,double[][] newclusters){  
   int m=1;   
   for (int j=0;j<newclusters.length;j++){
    for (int i=0;i<matrix.length;i++){
     if(matrix[i][17]==newclusters[j][2]){
        newclusters[j][0]=matrix[i][15]+newclusters[j][0];
        newclusters[j][1]=matrix[i][16]+newclusters[j][1];
        m++;
     }
   }
    newclusters[j][0]=newclusters[j][0]/m;
    newclusters[j][1]=newclusters[j][1]/m;
     m=1;           
  }   
   return newclusters;
  }
  
   //5.if the newclusters changed, then we redo above part until the newclusters equal to the old one
  public static double[][] finalcluster(double[][] newclusters,double[][] clustercopy,double[][] matrix){
    //do this loop with max times
    int max = 0;
    while(checkequal(newclusters, clustercopy)==false){
    testdistance(matrix,newclusters);
    copy(newclusters);
    resetmatrix(newclusters);
    optimalcluster( matrix,newclusters);
    max++;
      if(max==20000){
    break;
    }
    
    }
    return newclusters;
  }
 

 //method to check the newclusters is equal the old one or not  
 public static boolean checkequal(double[][] newclusters,double[][] clustercopy){
 return Arrays.equals(newclusters,clustercopy);
  }
 //question 6
 //the process is very similar to question 5, 
 //but in this question we consider all shipments flow instead of location
 //combine the methods and output the three highest(lowest) and average value
    public static void reskmeansflow(double[][] matrix,int k){
      //kmeansflow(matrix,k);
      double[][] oricluster=new double[k][7];
      oricluster = kmeansflow(matrix,k);
      setnum(matrix,oricluster,17);
      
      totalwvs(matrix,oricluster,17);
      totald(matrix,oricluster,17);
      output(oricluster);
    }
  //combine the methods to return an optimal cluster set    
    public static double[][] kmeansflow(double matrix[][], int k){
      double[][] newclusters=new double[k][7];
      newclusters = newrandomcluster(matrix,k);
      testdistance(matrix,newclusters);
  
      double[][] clustercopy=new double[k][7];
      clustercopy=copy(newclusters);
      resetmatrix(newclusters);      
      optimalcluster(matrix,newclusters);  
      finalcluster(newclusters,clustercopy,matrix);

      return newclusters;
   
}
 }