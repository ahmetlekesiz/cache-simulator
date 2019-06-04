package com.company;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;

import static java.lang.Math.pow;

public class Main {

    private static String[][][] L1I;
    private static String[][][] L1D;
    private static String[][][] L2;
    private static String addressHex;
    private static String addressBinary;
    private static int addressDecimal;
    private static int addressHextLength;
    private static int addressBinaryLength;
    private static int lengthDiff;
    private static int L1sParameter;
    private static int L1bParameter;
    private static int L2sParameter;
    private static int L2bParameter;
    private static int removeFromAddress;
    private static String tagL1;
    private static String setIndexBinary;
    private static int setIndexL1;
    private static String blockIndex;
    private static int removeFromAddressL2;
    private static String tagL2;
    private static String setIndexL2Binary;
    private static int setIndexL2;
    private static String blockIndexL2;
    private static int missL1I = 0;
    private static int hitL1I = 0;
    private static int evictionsL1I = 0;
    private static int missL1D = 0;
    private static int hitL1D = 0;
    private static int evictionsL1D = 0;
    private static int missL2 = 0;
    private static int hitL2 = 0;
    private static int evictionsL2 = 0;
    private static boolean L1Ihas;
    private static boolean L1Dhas;
    private static boolean L2has;
    private static String[] ram;
    private static String dataL1 = "";
    private static String dataL2 = "";
    private static int emptyLine;
    private static int timer=0;
    private static int lowestTimer = 0;

    public static void main(String[] args) throws IOException {

        File file2 = new File("ram.txt");
        BufferedReader br2 = new BufferedReader(new FileReader(file2));

        String st2;
        while ((st2 = br2.readLine()) != null) {
            ram = st2.split(" ");
        }
        System.out.println("ram size: " + ram.length);

        String traceFile = args[13];
        L1sParameter = Integer.parseInt(args[1]);
        int L1s = (int) pow(2, L1sParameter);
        int L1E = Integer.parseInt(args[3]);
        L1bParameter = Integer.parseInt(args[5]);
        int L1b = (int) pow(2, L1bParameter);

        L2sParameter = Integer.parseInt(args[7]);
        int L2s = (int) pow(2, L2sParameter);
        int L2E = Integer.parseInt(args[9]);
        L2bParameter = Integer.parseInt(args[11]);
        int L2b = (int) pow(2, L2bParameter);

        L1I = new String[L1s][L1E][4];
        L1D = new String[L1s][L1E][4];
        L2 = new String[L2s][L2E][4];

        for (int i = 0; i < L1s; i++) {
            for (int j = 0; j < L1E; j++) {
                for (int k = 0; k < 4; k++) {
                    L1I[i][j][k] = "";
                    L1D[i][j][k] = "";
                }
            }
        }

        for (int i = 0; i < L2s; i++) {
            for (int j = 0; j < L2E; j++) {
                for (int k = 0; k < 4; k++) {
                    L2[i][j][k] = "";
                }
            }
        }

        File file = new File("traces/" + traceFile);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null){
            System.out.println(st);
            getLine(st);
//            System.out.println("L1I and L1D");
//            System.out.println(addressBinary);
//            System.out.println(tagL1);
//            System.out.println("set index: " + setIndexL1);
//            System.out.println("block index: " + blockIndex);
//
//            System.out.println("L2");
//            System.out.println(tagL2);
//            System.out.println("set index: " + setIndexL2);
//            System.out.println("blok index: " + blockIndexL2);

            if(st.split(" ")[0].equals("I")){
                int lineOnL2 = 0;
                    for (int l = 0; l < L1E ; l++) {
//                        System.out.println(L1I[setIndexL1][l][0] + "********************");
                        if((L1I[setIndexL1][l][0]).equals(tagL1)){
                            hitL1I++;
                            L1Ihas = true;
                        }else{
                            missL1I++;
                            L1Ihas = false;
                        }
                    }

                for (int k = 0; k < L2E ; k++) {
                    if((L2[setIndexL2][k][0]).equals(tagL1)){
                        hitL2++;
                        L2has = true;
                        lineOnL2 = k;
                    }else{
                        missL2++;
                        L2has = false;
                    }
                }


                if(L1Ihas == false && L2has == true){
                    emptyLine = getFirstEmptyLine(L1I, setIndexL1, L1E);
                    if (emptyLine != -1){
                        L1I[setIndexL1][emptyLine][0] = L2[setIndexL2][lineOnL2][0];
                        L1I[setIndexL1][emptyLine][1] = L2[setIndexL2][lineOnL2][1];
                        L1I[setIndexL1][emptyLine][2] = L2[setIndexL2][lineOnL2][2];
                        timer++;
                        L1I[setIndexL1][emptyLine][3] = String.valueOf(timer);
                    }else{
                        lowestTimer = getLowestTime(L1I, setIndexL1, L1E);
                        L1I[setIndexL1][lowestTimer][0] = tagL1;
                        L1I[setIndexL1][lowestTimer][1] = "1";
                        L1I[setIndexL1][lowestTimer][2] = dataL1;
                        timer++;
                        L1I[setIndexL1][lowestTimer][3] = String.valueOf(timer);
                    }
                }else if(L1Ihas == false && L2has == false){
                    dataL1 = getDataFromRam(addressDecimal, L1b);
                    dataL2 = getDataFromRam(addressDecimal, L2b);

                    emptyLine = getFirstEmptyLine(L2, setIndexL2, L2E);
                    if (emptyLine!=-1){
                        L2[setIndexL2][emptyLine][0] = tagL2;
                        L2[setIndexL2][emptyLine][1] = "1";
                        L2[setIndexL2][emptyLine][2] = dataL2;
                        timer++;
                        L2[setIndexL2][emptyLine][3] = String.valueOf(timer);
                    }else{
                        lowestTimer = getLowestTime(L2, setIndexL2, L2E);
                        L2[setIndexL2][lowestTimer][0] = tagL2;
                        L2[setIndexL2][lowestTimer][1] = "1";
                        L2[setIndexL2][lowestTimer][2] = dataL2;
                        timer++;
                        L2[setIndexL2][lowestTimer][3] = String.valueOf(timer);
                    }

                    emptyLine = getFirstEmptyLine(L1I, setIndexL1, L1E);
                    if (emptyLine!=-1){
                        L1I[setIndexL1][emptyLine][0] = tagL1;
                        L1I[setIndexL1][emptyLine][1] = "1";
                        L1I[setIndexL1][emptyLine][2] = dataL1;
                        timer++;
                        L1I[setIndexL1][emptyLine][3] = String.valueOf(timer);
                        System.out.println("hop");
                    }else{
                        lowestTimer = getLowestTime(L1I, setIndexL1, L1E);
                        L1I[setIndexL1][lowestTimer][0] = tagL1;
                        L1I[setIndexL1][lowestTimer][1] = "1";
                        L1I[setIndexL1][lowestTimer][2] = dataL1;
                        timer++;
                        L1I[setIndexL1][lowestTimer][3] = String.valueOf(timer);
                    }
                }
            }

            else if(st.split(" ")[0].equals("L")){

            }else if(st.split(" ")[0].equals("S")){

            }else if(st.split(" ")[0].equals("M")){

            }
            printCache(L1I, L1E);

        }

        System.out.println("hitL1I: " + hitL1I);
        System.out.println("hitL1D: " + hitL1D);
        System.out.println("hitL2: " + hitL2 + "\n");

        System.out.println("missL1I: " + missL1I);
        System.out.println("missL1D: " + missL1D);
        System.out.println("missL2: " + missL2);

    }

    public static String removeLastChar(String s) {
        return (s == null || s.length() == 0)
                ? null
                : (s.substring(0, s.length() - 1));
    }

    public static void getLine(String st){
        addressHex = st.split(" ")[1];
        addressHex = removeLastChar(addressHex);
        addressHextLength = addressHex.length();
        addressBinary = new BigInteger(addressHex, 16).toString(2);
        addressDecimal = Integer.parseInt(addressHex,16);
        addressBinaryLength = addressBinary.length();
        lengthDiff = addressHextLength *4 - addressBinaryLength;
        if(lengthDiff!=0){
            String zeros = "";
            for (int i = 0; i < lengthDiff; i++){
                zeros = zeros + "0";
            }
            addressBinary = zeros + addressBinary;
        }
        blockIndex = addressBinary.substring(addressBinary.length()-L1bParameter);
        if(L1sParameter == 0){
            setIndexBinary = "0";
            setIndexL1 = 0;
        }else{
            setIndexBinary = addressBinary.substring(addressBinary.length() - L1bParameter - L1sParameter, addressBinary.length()-L1bParameter);
            setIndexL1 = Integer.parseInt(setIndexBinary, 2);
        }
        tagL1 = addressBinary;
        removeFromAddress = L1sParameter + L1bParameter;
        removeFromAddressL2 = L2sParameter + L2bParameter;
        for (int j = 0; j < removeFromAddress; j++){
            tagL1 = removeLastChar(tagL1);
        }

        tagL2 = addressBinary;
        for (int j = 0; j < removeFromAddressL2; j++) {
            tagL2 = removeLastChar(tagL2);
        }
        blockIndexL2 = addressBinary.substring(addressBinary.length()-L2bParameter);
        if(L2sParameter == 0){
            setIndexL2Binary = "0";
            setIndexL2 = 0;
        }else{
            setIndexL2Binary = addressBinary.substring(addressBinary.length() - L2bParameter - L2sParameter, addressBinary.length()-L2bParameter);
            setIndexL2 = Integer.parseInt(setIndexL2Binary,2);
        }
    }

    public static String getDataFromRam(int addressDecimal, int bSize){
        String data = "";
        if(addressDecimal !=0){
            addressDecimal = addressDecimal - 1;
        }
        for (int i = 0; i < bSize; i++) {
            data = data + ram[addressDecimal+i];
        }
        return data;
    }

    public static int getFirstEmptyLine(String[][][] cache, int setIndex, int lineSize){
            for (int j = 0; j <lineSize ; j++) {
                if (cache[setIndex][j][2] == ""){
                    return j;
                }
            }
            return -1;
    }

    public static int getLowestTime(String[][][] cache, int setIndex, int lineSize){
        int time = 0;
        int lowestLine = 0;
        for (int j = 0; j < lineSize ; j++) {
            if(cache[setIndex][j][3].equals("") == false){
                int deneme = Integer.parseInt(cache[setIndex][j][3]);
                if (time > Integer.parseInt(cache[setIndex][j][3])){
                    time = Integer.parseInt(cache[setIndex][j][3]);
                    lowestLine = j;
                }
            }
        }
        return lowestLine;
    }

    public static void printCache(String[][][] cache, int lineSize){
        for (int i = 0; i < cache.length; i++) {
            for (int j = 0; j < lineSize ; j++) {
                System.out.println("tag: " + cache[i][j][0]);
                System.out.println("data: " + cache[i][j][2]);
                System.out.println("valid bit: " + cache[i][j][1]);
                System.out.println("timer: " + cache[i][j][3]);
            }
        }
    }


    //line al, tagL1 i kontrol et. cachlerin ikisinde de tagL1 i bulamazsan ramdan datayı alıp önce l2ye sonra l1e koy.
    //eklemeye çalıştığında cachler doluysa timer ı en az olanı sil, datayı oraya ekle, tagL1 ine de hesapladığın tagL1 i koy. timer'ını da upload et.

    //ram dosyasını array e ve arrayList koy ordan çek. file dan okumak zor ve uzun oluyor.



}
