import domain.Vehicle;
import mysqlconnect.JdbcOperation;
import org.junit.Test;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuxiao on 4/9/16.
 */
public class VehicleNumAndType {

    public VehicleNumAndType() {
    }


    // 经营范围编码的数据预先读入到一个preDataMap里面
    public static void preprocess(String path, Map<String, String> preDataMap) {
        String vehicleInfoPath = path;

        try {
            File dataFile = new File(vehicleInfoPath);
            InputStreamReader readinput = new InputStreamReader(new FileInputStream(dataFile), "GB2312");
            BufferedReader reader = new BufferedReader(readinput);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineItems = line.split(",");
                if (!preDataMap.containsKey(lineItems[0])) {
                    preDataMap.put(lineItems[0], line);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

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

    // 车辆数据四种类型分类,并把分类结果写入远端MySql 数据库
    //  vehicleResult.csv  包含 自带类型和经营范围等内容的数据
    // classifacation.csv  测试用的,可
    @Test
    public  void classify() {
        String vehicleInfoPath = "/Users/yuxiao/项目/expriment/result/staticmergeData.csv";
//        String vehicleInfoPath = "/Users/yuxiao/项目/expriment/0419/vehicleResult.csv";
//        String outputPath = "/Users/yuxiao/项目/expriment/0419/classfication.csv";
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
//                if (lineItems[0].indexOf("苏") != 0) continue;

                count++;
                if (lineItems.length >= 9) {
                    //苏M07029,2,320000,999,321200,0,320000,,"03121,03122"
                    StringBuffer tempValBuffer = new StringBuffer();
                    for(int i=0;i<=6;i++){
                        tempValBuffer.append(lineItems[i]).append(",");
                    }

                    typeCount++;
                    String manageArea = "";
                    //一辆车所属的经营范围分类数组
                    int[] manageCodeArr = new int[5];
                    for (int i = 8; i < lineItems.length; i++) {
                        if (i == 8) {
                            String manageCode = lineItems[8];
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
                    int typeSelf = isTheFourTypeJudgedByType(lineItems[3]);
                    if (typeSelf!=999) {
                     //判别除999外
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
//                    guestDataMap.put(lineItems[0], tempValBuffer.toString()+ typeSelf + "," + manageArea + maxManageCodePos);
                    manageArea = manageArea.replaceAll(",","#");
                    guestDataMap.put(lineItems[0], tempValBuffer.toString()+ typeSelf + "," + maxManageCodePos+","+manageArea);
                } else {
                    //苏M07029,2,320000,999,321200,0,320000
                    StringBuffer tempValBuffer = new StringBuffer();
                    for(int i=0;i<=5;i++){
                        tempValBuffer.append(lineItems[i]).append(",");
                    }
                    tempValBuffer.append(lineItems[6]);

//                    不能分类的车牌
                    typeErrorCount++;
                    typeErrorMap.put(lineItems[0], tempValBuffer.toString());
                }
            }
//            System.out.println("江苏车辆总共数量：" + count);
//            System.out.println("能分类数量：" + typeCount);
//            System.out.println("不能分类数量：" + typeErrorCount);
//            System.out.println("自带类型与经营范围划分的类型一致的数量:"+sumOfTheSameType);
//            System.out.println("自带类型与经营范围划分的类型不同的数量:"+sumOfDiffType);
//            System.out.println("自带类型为999的车辆数量:"+sumOfTypeIsNine);
            reader.close();


//            File outFile = new File(outputPath);
//            Writer writer = new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8");

            Connection conn = JdbcOperation.getConn();
            //region 两种情况都能分类
            for (Map.Entry<String, String> entry : guestDataMap.entrySet()) {
                //苏JR1916,2,320000,11,320900,2137626623,320000,1,1
//                writer.write( entry.getValue() + "\n");

                Vehicle vehicle = new Vehicle();
                String[] lineItems = entry.getValue().split(",");
                // 信息 plate,plate_color,local_code,industry_code,xingzheng_code,yehu_code,now_local_code
                vehicle.setPlate(lineItems[0]);
                vehicle.setPlate_color(Integer.valueOf(lineItems[1]));
                vehicle.setLocal_code(Integer.valueOf(lineItems[2]));
                vehicle.setIndustry_code(Integer.valueOf(lineItems[3]));
                vehicle.setXingzheng_code(Integer.valueOf(lineItems[4]));
                vehicle.setYehu_code(Integer.valueOf(lineItems[5]));
                vehicle.setNow_local_code(Integer.valueOf(lineItems[6]));

                if(lineItems.length ==7){
                    vehicle.setTypeself_catalogue(0);
                    vehicle.setManage_catalogue(0);
                } else{
                    // 能够划分四种类型
//                    System.out.println(lineItems[7]);
                    if(lineItems[7].equals("999")){
                        //按照第二种方法划分
                        vehicle.setTypeself_catalogue(Integer.valueOf(lineItems[8]));
                    } else{
                        vehicle.setTypeself_catalogue(Integer.valueOf(lineItems[7]));
                    }
                    String tempManageArea = lineItems[9].replaceAll("#",",");
                    tempManageArea = tempManageArea.substring(0,tempManageArea.length()-1);
                    vehicle.setManage_area(tempManageArea);
                    vehicle.setManage_catalogue(Integer.valueOf(lineItems[8]));


                }

                if(!JdbcOperation.query(vehicle,conn)){
                    // 数据库中没有主键相同的数据,插入
                    JdbcOperation.insert(vehicle,conn);

                }
            }
            //endregion

//            for (Map.Entry<String, String> entry : truckDataMap.entrySet()) {
//                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
//            }
//            for (Map.Entry<String, String> entry : dangerDataMap.entrySet()) {
//                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
//            }
//            for (Map.Entry<String, String> entry : maintainDataMap.entrySet()) {
//                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
//            }
//            for (Map.Entry<String, String> entry : errorDataMap.entrySet()) {
//                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
//            }

            //不包含经营范围信息的车辆信息导入数据库
            for(Map.Entry<String, String> entry: typeErrorMap.entrySet()){
//                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
                Vehicle vehicle = new Vehicle();
                String[] lineItems = entry.getValue().split(",");
                // 信息 plate,plate_color,local_code,industry_code,xingzheng_code,yehu_code,now_local_code
                vehicle.setPlate(lineItems[0]);
                vehicle.setPlate_color(Integer.valueOf(lineItems[1]));
                vehicle.setLocal_code(Integer.valueOf(lineItems[2]));
                vehicle.setIndustry_code(Integer.valueOf(lineItems[3]));
                vehicle.setXingzheng_code(Integer.valueOf(lineItems[4]));
                vehicle.setYehu_code(Integer.valueOf(lineItems[5]));
                vehicle.setNow_local_code(Integer.valueOf(lineItems[6]));
                //仅仅按照自带类型划分4种类型
                vehicle.setTypeself_catalogue(isTheFourTypeJudgedByType(lineItems[3]));
                vehicle.setManage_catalogue(0);
                if(!JdbcOperation.query(vehicle,conn)){
                    // 数据库中没有主键相同的数据,插入
                    JdbcOperation.insert(vehicle,conn);

                }
            }

            conn.close();
//            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public  void readStaticDataAndMerge(){

        // key 车牌号 value 原始信息+经营范围（若有）
        Map<String, String> numsAndTypeMap = new HashMap<>();
        Map<String, String> preDataMap = new HashMap<>();
//        String vehicInfoPath = args[1];
     String vehicInfoPath = "/Users/yuxiao/项目/expriment/vehicle_info.txt";
//        String vehicInfoPath = "/root/lsy/highway/data_ditu/vehicle_info.txt";
        preprocess(vehicInfoPath, preDataMap);

        File staticDataFile = new File("/Users/yuxiao/项目/expriment/静态数据.csv");
        String allVehicleInfoPath = "/Users/yuxiao/项目/expriment/result/staticmergeData.csv";

        BufferedReader reader =null;
        Writer writer = null;
        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(staticDataFile));
            reader = new BufferedReader(read);
            File outFile = new File(allVehicleInfoPath);
            writer = new OutputStreamWriter(new FileOutputStream(outFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line;

        try {
            //逐条数据处理
            while ((line = reader.readLine()) != null) {
                String[] tmpVehicleinfo = line.split(",");
                if(tmpVehicleinfo.length >=7){
                    String val = "";
                    String plate = tmpVehicleinfo[0].trim();
                    if (preDataMap.containsKey(plate)) {
                        //vehiclo_info.txt 有相关车牌的经营范围信息
                        String preDataLine = preDataMap.get(plate);
                        String[] preDataLineItem = preDataLine.split(",");
                        if (preDataLineItem.length >= 4) {
                            // 包含经营范围编码信息
                            String manageArea = "";
                            for (int i = 3; i < preDataLineItem.length; i++) {
                                if (i != preDataLineItem.length - 1) {
                                    manageArea += preDataLineItem[i] + ",";
                                } else {
                                    manageArea += preDataLineItem[i];
                                }
                            }
                            val = line + ",,"+manageArea;
                        }

                    } else{
                        // 没有经营范围代码信息,保留原数据
                        val = line;
                    }

                    writer.write(val+"\n");

                }

            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) throws UnsupportedEncodingException {
//        String filedirPath = "/Users/yuxiao/项目/expriment/data/";
//        String filedirPath = args[0];
        String filedirPath = "/home/high_way_lsy/lwlk_data/code/result_sort/20140430/";
//        String allVehicleInfoPath = "/Users/yuxiao/项目/expriment/result/vehicInfo.csv";
        String allVehicleInfoPath = "/home/high_way_lsy/lwlk_data/code/result_sort/vehicleResult.csv";
//        String allVehicleInfoPath = args[2];
        File fileDir = new File(filedirPath);
        File[] dataFiles = fileDir.listFiles();
        // key 车牌号 value 原始信息+经营范围（若有）
        Map<String, String> numsAndTypeMap = new HashMap<>();
        //保存vehicle_info.txt 数据，key 为车牌号，value 每一行的信息
        Map<String, String> preDataMap = new HashMap<>();
//        String vehicInfoPath = args[1];
//        String vehicInfoPath = "/Users/yuxiao/项目/expriment/vehicle_info.txt";
        String vehicInfoPath = "/root/lsy/highway/data_ditu/vehicle_info.txt";
        preprocess(vehicInfoPath, preDataMap);


        for (File itemFile : dataFiles) {
            if (itemFile.isDirectory()) continue;

            String vehNum;
            vehNum = itemFile.getName();
//            System.out.println(vehNum);
            //提取车牌号
            vehNum = vehNum.substring(0, vehNum.indexOf(".")).trim();
//            System.out.println(vehNum);
            BufferedReader reader;
            try {
                InputStreamReader read = new InputStreamReader(new FileInputStream(itemFile));
                reader = new BufferedReader(read);
            } catch (FileNotFoundException e) {
                continue;
            }
            String line;

            try {
                //读取每个数据文件的第一行数据
                while ((line = reader.readLine()) != null) {
                    String[] tmpVehicleinfo = line.split(",");
                    // 确保所读数据有效 plate,plate_color,local_code,industry_code,xingzheng_code,yehu_code,now_local_code
                    if (tmpVehicleinfo.length >= 7) {
                        String plate_color = tmpVehicleinfo[1];
                        String local_code = tmpVehicleinfo[2];
                        String xingzheng_code = tmpVehicleinfo[4];
                        String yehu_code = tmpVehicleinfo[5];
                        String now_local_code = tmpVehicleinfo[6];
                        //提取自带类型
                        String vehType = tmpVehicleinfo[3];
                        //将要保存的value 值
                        String tmpVal = plate_color + ","+local_code +","+vehType+","+xingzheng_code
                                        +"," +yehu_code+","+now_local_code;

                        if (!numsAndTypeMap.containsKey(vehNum)) {

                            if (preDataMap.containsKey(vehNum)) {
                                //vehiclo_info.txt 有相关车牌的经营范围信息
                                String preDataLine = preDataMap.get(vehNum);
                                String[] preDataLineItem = preDataLine.split(",");
                                if (preDataLineItem.length >= 4) {
                                    String manageArea = "";
                                    for (int i = 3; i < preDataLineItem.length; i++) {
                                        if (i != preDataLineItem.length - 1) {
                                            manageArea += preDataLineItem[i] + ",";
                                        } else {
                                            manageArea += preDataLineItem[i];
                                        }
                                    }
                                    numsAndTypeMap.put(vehNum, tmpVal + "," + preDataLineItem[2] + "," + manageArea);
                                }
                            } else {
                                //vehiclo_info.txt 没有相关车牌的经营范围信息，保存原始数据
                                numsAndTypeMap.put(vehNum, tmpVal);
                            }
                        }
                        //只读取第一行
                        break;
                    }
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {
            File outFile = new File(allVehicleInfoPath);
            Writer writer = new OutputStreamWriter(new FileOutputStream(outFile));

            for (Map.Entry<String, String> entry : numsAndTypeMap.entrySet()) {
//                System.out.println(entry.getKey()+entry.getValue());
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ok");
    }


}
