import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuxiao on 4/9/16.
 */
public class VehicleNumAndType {

    public VehicleNumAndType(){}

    public static void preprocess(Map<String,String> preDataMap){
//        String vehicleInfoPath = "/Users/yuxiao/项目/expriment/vehicle_info.txt";
        String vehicleInfoPath = "/root/lsy/highway/data_ditu/vehicle_info.txt";

        try{
            File dataFile = new File(vehicleInfoPath);
            InputStreamReader readinput = new InputStreamReader(new FileInputStream(dataFile),"GB2312");
            BufferedReader reader = new BufferedReader(readinput);
            String line;
            while ((line=reader.readLine()) !=null){
                String[] lineItems =line.split(",");
                if(!preDataMap.containsKey(lineItems[0])){
                    preDataMap.put(lineItems[0],line);
                }
            }
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        String filedirPath = "/Users/yuxiao/项目/expriment/data/";
//        String filedirPath = "/home/high_way_lsy/lwlk_data/code/result_sort/20140430/";
        String allVehicleInfoPath = "/Users/yuxiao/项目/expriment/result/allInfo.csv";
        File fileDir = new File(filedirPath);
        File[] dataFiles = fileDir.listFiles();
        Map<String,String> numsAndTypeMap = new HashMap<String,String>();
        Map<String,String> preDataMap = new HashMap<String,String>();
        preprocess(preDataMap);

        for( File itemFile: dataFiles){
            if(itemFile.isDirectory()) continue;

            String vehNum;
            vehNum = itemFile.getName();
            vehNum = vehNum.substring(0,vehNum.indexOf("."));
            BufferedReader reader=null;
            try{
                InputStreamReader read = new InputStreamReader (new FileInputStream(itemFile));
                reader=new BufferedReader(read);
            }catch (FileNotFoundException e){
                continue;
            } catch (IOException ex){
                continue;
            }
            String line;

            try {
                while ((line=reader.readLine())!=null){
                    String[] tmpVehicleinfo = line.split(",");
                    if(tmpVehicleinfo.length>=4){

                        String vehType = tmpVehicleinfo[3];
//                        System.out.println((vehNum+","+vehType));
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
//        System.out.println("fe");
    }
}
