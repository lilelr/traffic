import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuxiao on 16/4/11.
 */
public class Classfication {


    public static int isTheFourTypeJudgedByManage(String manageCode) {
        if (manageCode.indexOf("01") == 0) {
            return 1;
        } else if (manageCode.indexOf("02") == 0) {
            return 2;
        } else if (manageCode.indexOf("03") == 0) {
            return 3;
        } else {
            return 4;
        }
    }

    public static int isTheFourTypeJudgedByType(String str) {
        if (str.indexOf("1") == 0) {
            return 1;
        } else if (str.indexOf("2") == 0) {
            return 2;
        } else if (str.indexOf("3") == 0) {
            return 3;
        } else if(str.indexOf("999") ==0){
            return 999;
        } else{
            return 4;
        }
    }

    public static void main(String[] args) {
        String vehicleInfoPath = "/Users/yuxiao/项目/expriment/result/vehicleResult.csv";
        String outputPath = "/Users/yuxiao/项目/expriment/result/classfication.csv";
        Map<String, String> guestDataMap = new HashMap<String, String>();
        Map<String, String> truckDataMap = new HashMap<String, String>();
        Map<String, String> dangerDataMap = new HashMap<String, String>();
        Map<String, String> maintainDataMap = new HashMap<String, String>();
        Map<String, String> errorDataMap = new HashMap<>();
        Map<String, String> typeErrorMap = new HashMap<>(); //不能分类的数据
        int count=0;
        int typeCount=0;
        int typeErrorCount=0;


        int sumOfTheSameType = 0;
        int sumOfDiffType = 0;
        try {
            File dataFile = new File(vehicleInfoPath);
            InputStreamReader readinput = new InputStreamReader(new FileInputStream(dataFile), "UTF-8");
            BufferedReader reader = new BufferedReader(readinput);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineItems = line.split(",");
                //过滤江苏的车辆
                if(lineItems[0].indexOf("苏") != 0) continue;

                   count++;
                if (lineItems.length >= 4) {
                    typeCount++;
                    String manageArea = "";
                    int[] manageCodeArr = new int[5];
                    for (int i = 3; i < lineItems.length; i++) {
                        if(i==3){
                            String manageCode = lineItems[3];
                            manageCode = manageCode.substring(1);
                            manageCodeArr[isTheFourTypeJudgedByManage(manageCode)]++;
                        } else{
                            manageCodeArr[isTheFourTypeJudgedByManage(lineItems[i])]++;
                        }
                            manageArea += lineItems[i] + ",";
                    }

                    int maxManageCode =0;
                    int maxManageCodePos =0;
                    for(int i=1;i<=4;i++){
                        if(manageCodeArr[i]>maxManageCode){
                            maxManageCode = manageCodeArr[i];
                            maxManageCodePos=i;
                        }
                    }
//                    int typeSelf = isTheFourTypeJudgedByType(lineItems[1]);
//                    String typeSelfStr ="";
//                    if(typeSelf==9){
//
//                    }
//
             guestDataMap.put(lineItems[0], lineItems[1] + "," + isTheFourTypeJudgedByType(lineItems[1]) + "," + lineItems[2] + "," + manageArea +maxManageCodePos);
                } else{
                    typeErrorCount++;
                    typeErrorMap.put(lineItems[0],line);
                }
            }
            System.out.println("江苏车辆总共数量："+ count);
            System.out.println("能分类数量："+ typeCount);
            System.out.println("不能分类数量："+ typeErrorCount);
            reader.close();


            File outFile = new File(outputPath);
            Writer writer = new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8");

            for (Map.Entry<String, String> entry : guestDataMap.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
            for (Map.Entry<String, String> entry : truckDataMap.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
            for (Map.Entry<String, String> entry : dangerDataMap.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
            for (Map.Entry<String, String> entry : maintainDataMap.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
            for (Map.Entry<String, String> entry : errorDataMap.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
            //            numsAndTypeMap.clear();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
