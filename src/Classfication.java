import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuxiao on 16/4/11.
 */
public class Classfication {

    // 根据经营范围划分四种类型
    public static int isTheFourTypeJudgedByManage(String manageCode) {
        if (manageCode.indexOf("01") == 0 || manageCode.indexOf("05201") == 0 || manageCode.indexOf("071") == 0 || manageCode.indexOf("08") == 0 || manageCode.indexOf("091") == 0) {
            return 1;
        } else if (manageCode.indexOf("02") == 0 || manageCode.indexOf("05202") == 0 || manageCode.indexOf("07200") == 0 || manageCode.indexOf("07201") == 0 || manageCode.indexOf("092") == 0 || manageCode.indexOf("13000") == 0) {
            return 2;
        } else if (manageCode.indexOf("03") == 0 || manageCode.indexOf("05203") == 0 || manageCode.indexOf("07202") == 0 || manageCode.indexOf("11") == 0) {
            return 3;
        } else {
            return 4;
        }
    }

    // 根据自带类型划分四种类型
    public static int isTheFourTypeJudgedByType(String str) {
        if (str.indexOf("1") == 0) {
            return 1;
        } else if (str.indexOf("2") == 0) {
            return 2;
        } else if (str.indexOf("3") == 0) {
            return 3;
        } else if (str.indexOf("999") == 0) {
            return 999;
        } else {
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
        int count = 0;
        int typeCount = 0;
        int typeErrorCount = 0;


        int sumOfTheSameType = 0;
        int sumOfDiffType = 0;
        int sumOfTypeIsNine =0 ; //类型自带为999的数量
        try {
            File dataFile = new File(vehicleInfoPath);
            InputStreamReader readinput = new InputStreamReader(new FileInputStream(dataFile), "UTF-8");
            BufferedReader reader = new BufferedReader(readinput);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineItems = line.split(",");
                //过滤江苏的车辆
                if (lineItems[0].indexOf("苏") != 0) continue;

                count++;
                if (lineItems.length >= 4) {
                    typeCount++;
                    String manageArea = "";
                    int[] manageCodeArr = new int[5];//一辆车所属的经营范围分类数组
                    for (int i = 3; i < lineItems.length; i++) {
                        if (i == 3) {
                            String manageCode = lineItems[3];
                            manageCode = manageCode.substring(1);
                            manageCodeArr[isTheFourTypeJudgedByManage(manageCode)]++;
                        } else {
                            manageCodeArr[isTheFourTypeJudgedByManage(lineItems[i])]++;
                        }
                        manageArea += lineItems[i] + ",";
                    }

                    int maxManageCode = 0;
                    int maxManageCodePos = 0; //一辆车由经营范围确定的唯一类别
                    for (int i = 1; i <= 4; i++) {
                        if (manageCodeArr[i] > maxManageCode) {
                            maxManageCode = manageCodeArr[i];
                            maxManageCodePos = i;
                        }
                    }

                    //自带类型判定，除999外
                    int typeSelf = isTheFourTypeJudgedByType(lineItems[1]);
                    if (typeSelf!=999) { //判别除999外
                        if(typeSelf == maxManageCodePos){
                            //自带类型与经营范围划分的类型一致
                            sumOfTheSameType++;
                        }else{
                            if(typeSelf!=4){
                                //自带类型与经营范围划分的类型严格不同，属于"其他"的车辆不列入统计
                                sumOfDiffType++;
                            }

                        }
                    } else{
                        sumOfTypeIsNine++;
                    }
                    guestDataMap.put(lineItems[0], lineItems[1] + "," + typeSelf + "," + lineItems[2] + "," + manageArea + maxManageCodePos);
                } else {
//                    不能分类的车牌
                    typeErrorCount++;
                    typeErrorMap.put(lineItems[0], line);
                }
            }
            System.out.println("江苏车辆总共数量：" + count);
            System.out.println("能分类数量：" + typeCount);
            System.out.println("不能分类数量：" + typeErrorCount);
            System.out.println("自带类型与经营范围划分的类型一致的数量:"+sumOfTheSameType);
            System.out.println("自带类型与经营范围划分的类型不同的数量:"+sumOfDiffType);
            System.out.println("自带类型为999的车辆数量:"+sumOfTypeIsNine);
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
            for(Map.Entry<String, String> entry: typeErrorMap.entrySet()){
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
