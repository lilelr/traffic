import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuxiao on 4/9/16.
 */
public class VehicleNumAndType {

    public VehicleNumAndType() {
    }

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


    public static void main(String[] args) throws UnsupportedEncodingException {
//        String filedirPath = "/Users/yuxiao/项目/expriment/data/";
//        String filedirPath = args[0];
        String filedirPath = "/home/high_way_lsy/lwlk_data/code/result_sort/20140430/";
//        String allVehicleInfoPath = "/Users/yuxiao/项目/expriment/result/vehicInfo.csv";
        String allVehicleInfoPath = "/home/high_way_lsy/lwlk_data/code/result_sort/vehicleResult.csv";
//        String allVehicleInfoPath = args[2];
        File fileDir = new File(filedirPath);
        File[] dataFiles = fileDir.listFiles();
        Map<String, String> numsAndTypeMap = new HashMap<>();
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
                while ((line = reader.readLine()) != null) {
                    String[] tmpVehicleinfo = line.split(",");
                    if (tmpVehicleinfo.length >= 4) {

                        String vehType = tmpVehicleinfo[3];
                        if (!numsAndTypeMap.containsKey(vehNum)) {
                            if (preDataMap.containsKey(vehNum)) {
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
                                    numsAndTypeMap.put(vehNum, vehType + "," + preDataLineItem[2] + "," + manageArea);
                                }
                            } else {
                                numsAndTypeMap.put(vehNum, vehType);
                            }


                        }
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
