package Aufgabe3;

import java.nio.ByteBuffer;

public class ByteArray {





    public static void main(String[] args) {
        byte[] bytes = {1, 2, 3, 4, 5, 6, 7};
        byte[] msg = createMsg(true, true, 42,bytes);

        for (byte x : msg) {
            System.out.print(x);
            System.out.print(" ");
        }

    }


    static byte[] createMsg (boolean isData, boolean isUrgent, int sequenceNumber, byte[] payload)throws IllegalArgumentException{
        if(sequenceNumber > (Math.pow(2,16)-1)){
            throw new IllegalArgumentException("number too large");
        }if(payload.length == 0){
            throw new IllegalArgumentException("payload is empty");
        }


        byte flag = isData && isUrgent ? (byte) 3 : isData ? (byte)2 : isUrgent ? (byte)1 : (byte)0;
        //8Byte + payload length
        ByteBuffer msg = ByteBuffer.allocate(8 + payload.length);
        //to make the byte full (00010) << 3
        msg.put((byte) (2<<3));
        msg.put(flag);
        msg.putShort((short) sequenceNumber);
        //bitdarstellung
        msg.putInt(payload.length << 3);
        msg.put(payload);
        return msg.array();



    }




}
