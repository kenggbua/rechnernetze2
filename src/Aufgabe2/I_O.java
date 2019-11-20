import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class I_O {


    public static void main(String[] args){

        if(args.length < 2){
            System.out.println("Wrong number of arguments");
            throw new IllegalArgumentException("wrong number of arguments");
        }

        String source = args[0];
        String output = args[1];



        try {
            FileInputStream inputFile = new FileInputStream(source);
            FileOutputStream outputFile = new FileOutputStream(output);
            ZipOutputStream outputZipped = new ZipOutputStream(outputFile);
            Reader reader = new InputStreamReader(inputFile, StandardCharsets.UTF_8);
            Writer writer = new OutputStreamWriter(outputZipped, StandardCharsets.ISO_8859_1);
            outputZipped.putNextEntry(new ZipEntry("Aufgabe2/output.txt"));

            int read = 0;
            while (read != -1){
                read = reader.read();
                if ((char)read == '\n'){
                    writer.write('\r');
                }
                writer.write(read);
            }



            reader.close();
            inputFile.close();

            writer.close();
            outputZipped.close();
            outputFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}
