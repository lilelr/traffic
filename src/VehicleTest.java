import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuxiao on 4/11/16.
 */
public class VehicleTest {

    public static void process(String path,String outputPath,Map<String,String> preDataMap){
//        String vehicleInfoPath = "/Users/yuxiao/项目/expriment/vehicle_info.txt";
//        String vehicleInfoPath = "/root/lsy/highway/data_ditu/vehicle_info.txt";
        String vehicleInfoPath = path;

        try{
            File dataFile = new File(vehicleInfoPath);
            InputStreamReader readinput = new InputStreamReader(new FileInputStream(dataFile),"GB2312");
            BufferedReader reader = new BufferedReader(readinput);
            String line;
//            int count=0;
            while ((line=reader.readLine()) !=null){
//                line = new String(line.getBytes("GB2312"),"UTF-8");
                String[] lineItems =line.split(",");
//                System.out.println(new String(line.getBytes("GB2312"), "UTF-8"));
                if(!preDataMap.containsKey(lineItems[0])){
                    preDataMap.put(lineItems[0], line);
//                    if(count>=5){
//                        break;
//                    }
//                    count++;
                }
            }
            reader.close();


            File outFile = new File(outputPath);
            Writer writer = new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8");

            for(Map.Entry<String,String> entry: preDataMap.entrySet()){
//                System.out.println(entry.getKey()+entry.getValue());
                writer.write(entry.getValue()+"\n");
            }
            //            numsAndTypeMap.clear();
            writer.close();

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void preprocess(String path,String outputPath,Map<String,String> preDataMap){
//        String vehicleInfoPath = "/Users/yuxiao/项目/expriment/vehicle_info.txt";
//        String vehicleInfoPath = "/root/lsy/highway/data_ditu/vehicle_info.txt";
        String vehicleInfoPath = path;

        try{
            File dataFile = new File(vehicleInfoPath);
            InputStreamReader readinput = new InputStreamReader(new FileInputStream(dataFile),"GB2312");
            BufferedReader reader = new BufferedReader(readinput);
            String line;
//            int count=0;
            while ((line=reader.readLine()) !=null){
                String[] lineItems =line.split(",");
//                System.out.println(new String(line.getBytes("GB2312"), "UTF-8"));
                if(!preDataMap.containsKey(lineItems[0])){
                    preDataMap.put(lineItems[0], line);
//                    if(count>=5){
//                        break;
//                    }
//                    count++;
                }
            }
            reader.close();


            File outFile = new File(outputPath);
            Writer writer = new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8");

            for(Map.Entry<String,String> entry: preDataMap.entrySet()){
//                System.out.println(entry.getKey()+entry.getValue());
                writer.write(entry.getValue()+"\n");
            }
            //            numsAndTypeMap.clear();
            writer.close();

        } catch (IOException e){
            e.printStackTrace();
        }

    }


    public static void main(String[] args){
        Map<String,String> dataMap = new HashMap<>();
//        String vehicInfoPath = "/Users/yuxiao/项目/expriment/data/冀E2652P.csv";
        String vehicInfoOutPath = "/Users/yuxiao/项目/expriment/data/copydata/c冀E2652P.csv";
//        String vehicInfoPath = "/root/lsy/highway/data_ditu/vehicle_info.txt";
        String vehicInfoPath = "/Users/yuxiao/项目/expriment/vehicle_info.txt";
        String vehicInfoOutputPath = "/Users/yuxiao/项目/expriment/result/vehicle_info.csv";

        preprocess(vehicInfoPath,vehicInfoOutputPath,dataMap);

//        process(vehicInfoPath,vehicInfoOutPath,dataMap);
    }
}
