import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuxiao on 4/9/16.
 */
public class VehicleNumAndType {

    public VehicleNumAndType(){}

    public static void preprocess(String path,Map<String,String> preDataMap){
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

//
//            File outFile = new File(outputPath);
//            Writer writer = new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8");
//
//            for(Map.Entry<String,String> entry: preDataMap.entrySet()){
////                System.out.println(entry.getKey()+entry.getValue());
//                writer.write(entry.getValue()+"\n");
//            }
//            //            numsAndTypeMap.clear();
//            writer.close();

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void fileNames(String path,String[] fileNames){
        try {
            File outFile = new File(path);
            FileWriter fw = new FileWriter(outFile);

            for(String itemString: fileNames){
                fw.write(itemString+"\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        String filedirPath = "/Users/yuxiao/项目/expriment/data/";
//        String filedirPath = args[0];
          String filedirPath = "/home/high_way_lsy/lwlk_data/code/result_sort/20140430/";
          String fileNamesPath = "/home/high_way_lsy/lwlk_data/code/result_sort/filenames.csv";
//        String allVehicleInfoPath = "/Users/yuxiao/项目/expriment/result/vehicInfo.csv";
          String allVehicleInfoPath = "/home/high_way_lsy/lwlk_data/code/result_sort/vehicInfo.csv";
//        String allVehicleInfoPath = args[2];
        File fileDir = new File(filedirPath);
        File[] dataFiles = fileDir.listFiles();
        Map<String,String> numsAndTypeMap = new HashMap<String,String>();
        Map<String,String> preDataMap = new HashMap<String,String>();
//        String vehicInfoPath = args[1];
//        String vehicInfoPath = "/Users/yuxiao/项目/expriment/vehicle_info.txt";
        String vehicInfoPath = "/root/lsy/highway/data_ditu/vehicle_info.txt";
        preprocess(vehicInfoPath,preDataMap);
//        String[] fileNames = fileDir.list();


        for( File itemFile: dataFiles){
            if(itemFile.isDirectory()) continue;

            String vehNum;
            vehNum = itemFile.getName();
            System.out.println(vehNum);
//            System.out.println(new String(itemFile.getName().getBytes(),"UTF-8"));
            vehNum = vehNum.substring(0, vehNum.indexOf("."));
            System.out.println(vehNum);
            BufferedReader reader=null;
            try{
                InputStreamReader read = new InputStreamReader (new FileInputStream(itemFile));
                reader=new BufferedReader(read);
            }catch (FileNotFoundException e){
                continue;
            }
            String line;

            try {
                while ((line=reader.readLine())!=null){
//                    line = new String(line.getBytes("GB2312"),"UTF-8");
                    String[] tmpVehicleinfo = line.split(",");
                    if(tmpVehicleinfo.length>=4){

                        String vehType = tmpVehicleinfo[3];
//                        vehNum = tmpVehicleinfo[0];
//                        System.out.println((tmpVehicleinfo[0]+","+vehType));

                        if(!numsAndTypeMap.containsKey(vehNum) && preDataMap.containsKey(vehNum)){
                            String preDataLine = preDataMap.get(vehNum);
                            String[] preDataLineItem = preDataLine.split(",");
                            if(preDataLineItem.length>=4){
                                numsAndTypeMap.put(vehNum,vehType+","+preDataLineItem[2]+","+preDataLineItem[3]);
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

//        fileNames(fileNamesPath,fileNames);


        try {
            File outFile = new File(allVehicleInfoPath);
            Writer writer = new OutputStreamWriter(new FileOutputStream(outFile));

            for(Map.Entry<String,String> entry: numsAndTypeMap.entrySet()){
//                System.out.println(entry.getKey()+entry.getValue());
                writer.write(entry.getKey() +"," +entry.getValue()+"\n");
            }
            //            numsAndTypeMap.clear();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ok");
    }


}
