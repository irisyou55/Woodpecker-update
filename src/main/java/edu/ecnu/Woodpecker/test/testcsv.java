package edu.ecnu.Woodpecker.test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class testcsv {
    public static void main(String[] args) {
        txtToCsv();
    }
    public static void txtToCsv() {
        String readFile = "./sysbench_results/oltp_result(1)_2021-12-6-15-6-40.txt";
        File file = new File(readFile);
        String line = "";
        String[] resultRows = null;

        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("new.csv"), "UTF-8"));

            out.write("time,thds,tps,qps,read_ope,write_ope,other_ope,lat(ms 95%),err/s,reconn/s"+"\n");
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            while ((line = br.readLine()) != null) {
               if(line.startsWith("[")){
                   resultRows = line.replaceAll(" ",",").split(",");
                   StringBuilder stringBuilder = new StringBuilder();
                   stringBuilder.append(resultRows[1].split("s")[0]);//time
                   stringBuilder.append(",");
                   stringBuilder.append(resultRows[4]);//thds
                   stringBuilder.append(",");
                   stringBuilder.append(resultRows[6]);//tps
                   stringBuilder.append(",");
                   stringBuilder.append(resultRows[8]);//qps
                   stringBuilder.append(",");
                   stringBuilder.append(resultRows[10].split("/")[0]);//read_ope
                   stringBuilder.append(",");
                   stringBuilder.append(resultRows[10].split("/")[1]);//write_ope
                   stringBuilder.append(",");
                   stringBuilder.append((resultRows[10].split("/")[2]).split("[)]")[0]);//other_ope
                   stringBuilder.append(",");
                   stringBuilder.append(resultRows[14]);//lat
                   stringBuilder.append(",");
                   stringBuilder.append(resultRows[16]);//err
                   stringBuilder.append(",");
                   stringBuilder.append(resultRows[18]);//reconn
                   stringBuilder.append("\n");
                   out.write(stringBuilder.toString());
                   out.flush();
                }
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
