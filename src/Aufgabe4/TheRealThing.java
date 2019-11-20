package Aufgabe4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TheRealThing extends Thread{

    private static float  result = 0;
    private static float[] array;
    private String filename;
    private int start;
    private int end;

    /**
     * Creates a new TheRealThing thread which operates
     * on the indexes start to end.
     */
    private TheRealThing(String filename, int start, int end) {
        this.filename = filename;
        this.start = start;
        this.end = end;
    }
        private float eine_komplizierte_Berechnung(float[] array) {
            float calc = 0;
            for (float number : array) {
                calc += number;
            }
            return calc;
        }
    public void run() {
        float[] newArray = new float[end-start+1];
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = array[start+i];
        }
        add(eine_komplizierte_Berechnung(newArray));
    }

    private synchronized void add(float add) {
        result += add;
    }
        public static void main(String[] args) throws IOException {
            String pathToFile = "/Users/kevinwiltschnig/Desktop/bak/ipam-master/rechnernetze/src/Aufgabe4/myArrayData.dat";
            int numThreads = 12;
            int arraySize = 70;
            array = readInput(pathToFile);


            TheRealThing[] threads = new TheRealThing[numThreads];
            int count = arraySize/numThreads;
            for (int i = 0; i < numThreads; i++) {
                if (i == numThreads - 1){
                    threads[i] = new TheRealThing(pathToFile, i * count,arraySize-1);
                }
                else {
                    threads[i] = new TheRealThing(pathToFile, i * count, (i+1)*count-1);
                }
                threads[i].start();
            }

            try {
                for (TheRealThing thread : threads) {
                    thread.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Final result: " + result);
        }

        private static float[] readInput(String path) throws IOException {
        float[] tmp = new float[0];
        int arraySize;
            FileReader reader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            int linecounter = 1;
            while(line != null){
                if(linecounter > 3){
                    tmp[linecounter-4] = Float.parseFloat(line);
                }
                if(linecounter == 3){
                    arraySize = Integer.parseInt(line);
                    tmp = new float[arraySize];
                }

                linecounter++;
                line = bufferedReader.readLine();

            }

            bufferedReader.close();
            reader.close();
            return tmp;
        }






}
