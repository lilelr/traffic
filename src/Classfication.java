import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuxiao on 16/4/11.
 */
public class Classfication {


    public static boolean isTheFourTypeJudgedByManage(String str){
        return str.indexOf("04")==1||str.indexOf("05")==1||str.indexOf("06")==1||str.indexOf("07")==1
                ||str.indexOf("08")==1||str.indexOf("09")==1||str.indexOf("10")==1||str.indexOf("11")==1
                ||str.indexOf("12")==1||str.indexOf("13")==1;
    }

    public static boolean isTheFourTypeJudgedByType(String str){
        return !(str.indexOf("1")==0||str.indexOf("2")==0||str.indexOf("3")==0||str.indexOf("4")==0);
    }

    public static void main(String[] args) {
        String vehicleInfoPath="/Users/yuxiao/项目/expriment/result/vehicleResult.csv";
        String outputPath="/Users/yuxiao/项目/expriment/result/classfication.csv";
        Map<String,String> guestDataMap = new HashMap<String,String>();
        Map<String,String> truckDataMap = new HashMap<String,String>();
        Map<String,String> dangerDataMap = new HashMap<String,String>();
        Map<String,String> maintainDataMap = new HashMap<String,String>();
        Map<String,String> errorDataMap = new HashMap<>();

        try{
            File dataFile = new File(vehicleInfoPath);
            InputStreamReader readinput = new InputStreamReader(new FileInputStream(dataFile),"UTF-8");
            BufferedReader reader = new BufferedReader(readinput);
            String line;
//            int count=0;
            while ((line=reader.readLine()) !=null){
                String[] lineItems =line.split(",");
//                System.out.println(new String(line.getBytes("GB2312"), "UTF-8"));
                if(lineItems.length>=4){
                     String manageCode = lineItems[3];
                    String manageArea = "";
                    for (int i = 3; i < lineItems.length; i++) {
                        if (i != lineItems.length - 1) {
                            manageArea += lineItems[i] + ",";
                        } else {
                            manageArea += lineItems[i];
                        }
                    }
                     if(manageCode.indexOf("01") == 1 && lineItems[1].indexOf("1") == 0){
                         guestDataMap.put(lineItems[0],lineItems[1]+","+lineItems[2]+","+manageArea+",1");
                     } else if(manageCode.indexOf("02") == 1 && lineItems[1].indexOf("2") == 0){
                         truckDataMap.put(lineItems[0],lineItems[1]+","+lineItems[2]+","+manageArea+",2");
                     } else if(manageCode.indexOf("03") == 1 && lineItems[1].indexOf("3") == 0){
                         dangerDataMap.put(lineItems[0],lineItems[1]+","+lineItems[2]+","+manageArea+",3");
                     } else if(isTheFourTypeJudgedByManage(manageCode) && isTheFourTypeJudgedByType(lineItems[1])){
                         maintainDataMap.put(lineItems[0],lineItems[1]+","+lineItems[2]+","+manageArea+",4");
                     } else{
                         errorDataMap.put(lineItems[0],lineItems[1]+","+lineItems[2]+","+manageArea+",5");
                     }

                }
            }
            reader.close();


            File outFile = new File(outputPath);
            Writer writer = new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8");

            for(Map.Entry<String,String> entry: guestDataMap.entrySet()){
                writer.write(entry.getKey()+","+entry.getValue() +"\n");
            }
            for(Map.Entry<String,String> entry: truckDataMap.entrySet()){
                writer.write(entry.getKey()+","+entry.getValue()+"\n");
            }
            for(Map.Entry<String,String> entry: dangerDataMap.entrySet()){
                writer.write(entry.getKey()+","+entry.getValue()+"\n");
            }
            for(Map.Entry<String,String> entry: maintainDataMap.entrySet()){
                writer.write(entry.getKey()+","+entry.getValue()+"\n");
            }
            for(Map.Entry<String,String> entry: errorDataMap.entrySet()){
                writer.write(entry.getKey()+","+entry.getValue()+"\n");
            }
            //            numsAndTypeMap.clear();
            writer.close();

        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
