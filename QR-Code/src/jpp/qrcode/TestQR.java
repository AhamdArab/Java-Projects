package jpp.qrcode;

import jpp.qrcode.*;
import jpp.qrcode.decode.DataDecoder;
import jpp.qrcode.decode.DataDestructurer;
import jpp.qrcode.decode.DataExtractor;
import jpp.qrcode.decode.Decoder;
import jpp.qrcode.encode.*;
import jpp.qrcode.io.TextReader;
import jpp.qrcode.io.TextWriter;
import jpp.qrcode.reedsolomon.ReedSolomonException;

import javax.xml.crypto.Data;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestQR {
    public static void main(String[] args) {

        /*byte[] data = new byte[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87};
        Version version = Version.fromNumber(7);
        ErrorCorrection errorCorrection = ErrorCorrection.QUARTILE;
        ErrorCorrectionInformation errorCorrectionInformation = version.correctionInformationFor(errorCorrection);
        DataStructurer.structure(data,errorCorrectionInformation);*/
        String content = "https://wuecampus2.uni-wuerzburg.de/moodle/";
        ErrorCorrection errorCorrection = ErrorCorrection.HIGH;
        QRCode qrCode = Encoder.createFromString(content,errorCorrection);
        System.out.println(Decoder.decodeToString(qrCode));
        System.out.println(qrCode.matrixToString());
/*
        try {
            FileInputStream inputStream = new FileInputStream(new File("examples/WueCampus_H.txt"));
            boolean[][] data = TextReader.read(inputStream);
            int size = data.length;
            int v = (size - 17) / 4;
            Version version = Version.fromNumber(v);
            ErrorCorrection errorCorrection = ErrorCorrection.HIGH;
            ReservedModulesMask reservedModulesMask = ReservedModulesMask.forVersion(version);
            MaskApplier.applyTo(data,MaskPattern.MASK001.maskFunction(),reservedModulesMask);
            ErrorCorrectionInformation errorCorrectionInformation = version.correctionInformationFor(errorCorrection);
            byte[] bytes = DataExtractor.extract(data,reservedModulesMask,errorCorrectionInformation.totalByteCount());
            byte[] dataDes = DataDestructurer.destructure(bytes,errorCorrectionInformation);
            String res = DataDecoder.decodeToString(dataDes,version,errorCorrection);
            System.out.println(res);
        } catch (IOException | ReedSolomonException e) {
            e.printStackTrace();
        }*/

     /*   try {

            ErrorCorrection errorCorrection = ErrorCorrection.QUARTILE;
            ReservedModulesMask reservedModulesMask = ReservedModulesMask.forVersion(version);
            MaskApplier.applyTo(data,MaskPattern.MASK100.maskFunction(),reservedModulesMask);
            QRCode qrCode = QRCode.createValidatedFromBooleans(data);
            System.out.println(qrCode.matrixToString());
            MaskSelector.maskWithBestMask(data,errorCorrection,reservedModulesMask);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
       /* byte[] bytes = new byte[]{41,42,43};
        String res = new String(bytes, StandardCharsets.ISO_8859_1);
       // System.out.println(res);
        byte[] data = new byte[]{(byte) 128,0,0,0,0,0,0,0,0};*/
      /*  Version version = Version.fromNumber(5);
        ErrorCorrectionInformation errorCorrectionInformation = version.correctionInformationFor(ErrorCorrection.QUARTILE);
        System.out.println(errorCorrectionInformation.totalDataByteCount());*/
        // System.out.println(DataDecoder.readEncoding(data));
        // System.out.println(DataDecoder.decodeToString(data,Version.fromNumber(1),ErrorCorrection.HIGH));
      /*  Version version = Version.fromNumber(5);
        ErrorCorrection errorCorrection = ErrorCorrection.HIGH;
        ErrorCorrectionInformation errorCorrectionInformation = version.correctionInformationFor(errorCorrection);
        System.out.println(errorCorrectionInformation.totalDataByteCount());
        String str ="Fi?M8/I/s0n+!\\8z,]us6fR*Z0\\[DaMramJiKTH+s\",q0NUyWFUmy]z+h]067/BlU$&=7<m>}17^SB`tk|qsi6lrE#kdfd>~$scNMp$y'W{fiz0R:rlkpv*0%[Y|s$W&6_Vk|j2fofqZD)RyTlTs%Q/5fn#5Skn0;NVB0}+lca/O\\9Mj9bep|fZ{!O'I(SH>b)B";
        DataEncoder.encodeForCorrectionLevel(str,ErrorCorrection.HIGH );*/

       /* boolean[][] data = new boolean[21][21];
        for (int i = 0 ; i < 21 ; i++)
            for (int j = 0 ; j < 21 ; j++)
                data[i][j] = false;
        PatternPlacer.placeOrientation(data,Version.fromNumber(1));
        PatternPlacer.placeTiming(data,Version.fromNumber(1));*/
      /*  Version version = Version.fromNumber(3);
        ErrorCorrection errorCorrection = ErrorCorrection.QUARTILE;
        ErrorCorrectionInformation errorCorrectionInformation = version.correctionInformationFor(errorCorrection);
         byte[] data = new byte[]{(byte)145,(byte)190,(byte)241,(byte)194,13,(byte)253,107,119,84,(byte)243,(byte)169,(byte)142,23,(byte)186,18,102,(byte)192,117,(byte)241,41,(byte)155,(byte)221,(byte)181,51,59,102,(byte)165,(byte)131,76,(byte)246,(byte)235,16,(byte)207,64,111,17,116,(byte)238,115,(byte)187,(byte)227,(byte)244,9,(byte)255,103,(byte)197,51,120,61,(byte)240,49,(byte)189,75,(byte)219,44,121,78,(byte)163,19,57,(byte)177,16,65,59,22,(byte)200,54,(byte)193,(byte)156,(byte)157};
        DataBlock[] dataBlocks = DataDestructurer.deinterleave(data,errorCorrectionInformation);
        System.out.println("Out:");
        for (int i = 0 ; i < dataBlocks.length;i++){
            byte[] dataBytes = dataBlocks[i].dataBytes();
            byte[] err = dataBlocks[i].correctionBytes();
            System.out.println("DataBlock " + i + ":");
            System.out.println("dataBytes:" + dataBytes.length);
            for (int j = 0 ; j < dataBytes.length;j++)
                System.out.print(dataBytes[j]+" ");
            System.out.println();
            System.out.println("correctionBytes:" + err.length);
            for (int j = 0 ; j < err.length;j++)
                System.out.print(err[j]+" ");
            System.out.println();
        }*/
        /* try {
            File file = new File("examples/Hallo_L.txt");
            Scanner reader = new Scanner(file);
            List<String[]> all = new ArrayList<>();
            while (reader.hasNextLine()){
                String line = reader.nextLine();
                line = line.trim();
                String[] words = line.split("\\s+");
                if (words[0].length() == 0 || words[0].charAt(0) =='#' )
                    continue;
                all.add(words);
            }
            int size = all.size();
            if (size == 0) {
                throw new IOException("input stream contains wrong data");
            }
            boolean[][] data = new boolean[all.size()][all.size()];
            int index = 0;
            for (String[] line : all){
                for (int i = 0 ; i < line.length ; i++){
                    int cell = Integer.parseInt(line[i]);
                    data[index / size][index % size] = (cell == 1);
                    index++;
                }
            }
         *//*   Version version = Version.fromNumber((data.length - 17) / 4) ;
            MaskPattern maskPattern = MaskPattern.MASK110;
            ErrorCorrection errorCorrection = ErrorCorrection.HIGH;
            QRCode qrCode = new QRCode(data, version, maskPattern, errorCorrection);
            String asString = qrCode.matrixToString();
            System.out.println(asString);
            System.out.println();
            System.out.println();*//*
            QRCode qrCode1 = QRCode.createValidatedFromBooleans(data);
            Version version = qrCode1.version();
            File file1 = new File("src/jpp/qrcode/io/output.txt");
            OutputStream outputStream = new FileOutputStream(file1);
            TextWriter.write(outputStream,qrCode1.data());
            ReservedModulesMask reservedModulesMask =  ReservedModulesMask.forVersion(version);
            byte[] res = DataExtractor.extract(qrCode1.data(), reservedModulesMask, 10);

            //System.out.println(qrCode1.matrixToString());
           // System.out.println();
           /// MaskApplier.applyTo(qrCode1.data(),qrCode1.maskPattern().maskFunction(),reservedModulesMask);
            DataPositions dataPositions = new DataPositions(reservedModulesMask);

            boolean test[][] = new boolean[version.size()][version.size()];
*//*
            for (int i = 0 ; i < test.length; i++)
                for (int j = 0 ; j < test.length; j++)
                    test[i][j] = false;
            MaskPattern maskPattern = MaskPattern.MASK110;
            ErrorCorrection errorCorrection = ErrorCorrection.HIGH;
            QRCode qrCode = new QRCode(test, version, maskPattern, errorCorrection);
            System.out.println(qrCode.matrixToString());
            System.out.println();
            do {
                int i = dataPositions.i();
                int j = dataPositions.j();
                test[i][j] = true;
                System.out.println(qrCode.matrixToString());
                System.out.println();
                System.out.println();
            }while (dataPositions.next());
          //  System.out.println(qrCode1.matrixToString());*//*
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }
}
