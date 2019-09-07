package com.example.cmgandroidappdemo.DeviceCommunication;

import java.util.ArrayList;

public class DataMediator {
    private static int fullpacketlength = 26;

    private byte[] draft = new byte[0];
    private OnDataValidListener onDataValidListener;

    private ArrayList<Byte> buffer = new ArrayList<>();

    public interface OnDataValidListener {
        void onDataValid(byte[] packet);
    }




    public DataMediator(){
    }

    /*
        public void update(byte[] newdata){

            if(packetValid(newdata)){
                //command valid
                if(onDataValidListener!= null) onDataValidListener.onDataValid(newdata);
            }else {
                //attempt to construct valid packet when looping
                draft = combineArrays(draft,newdata);
                if(packetValid(draft)){
                    if(onDataValidListener!= null) onDataValidListener.onDataValid(draft);
                    draft = new byte[]{};
                }else if(packetCorpted(draft)){

                }
            }
        }

    /*
        public void updatebuffer(byte[] newdata){
            for(byte a:newdata){
                Byte b = (Byte) a;
                if(a == (byte) SportHelper.COMMAND_START_FLAG){
                    buffer.add(b);
                }else if (buffer.size()>0 && buffer.get(0) == (byte) SportHelper.COMMAND_START_FLAG){
                    buffer.add(b);
                    if(buffer.size() == 26 && buffer.get(buffer.size()-1) == (byte) SportHelper.COMMAND_END_FLAG){
                        byte[] packet = new byte[buffer.size()];
                        for(int i = 0; i<buffer.size(); i++){
                            packet[i] = buffer.get(i);
                        }
                        buffer.clear();
                        if(onDataValidListener!= null) onDataValidListener.onDataValid(packet);
                    }else if(buffer.size()>500){
                        buffer.clear();
                    }
                }
            }
        }
    */
    public byte[] getBuffer(){
        byte[] packet = new byte[buffer.size()];
        for(int i = 0; i<buffer.size(); i++){
            packet[i] = buffer.get(i);
        }
        return packet;
    }



/*
    private boolean packetValid(byte[] packet){
        return packet[0]==(byte) SportHelper.COMMAND_START_FLAG
                &&packet[packet.length-1]==(byte)SportHelper.COMMAND_END_FLAG
                &&packet.length==fullpacketlength;
    }

    private boolean packetCorrupted(byte[] packet){
        return packet.length>fullpacketlength;
    }
*/

    public static String getReadableHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 3];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = "0123456789ABCDEF".toCharArray()[v >>> 4];
            hexChars[j * 3 + 1] = "0123456789ABCDEF".toCharArray()[v & 0x0F];
            hexChars[j*3+2] = ' ';
        }
        return new String(hexChars);
    }

    public static String getHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = "0123456789ABCDEF".toCharArray()[v >>> 4];
            hexChars[j * 3 + 1] = "0123456789ABCDEF".toCharArray()[v & 0x0F];
            //hexChars[j*3+2] = ' ';
        }
        return new String(hexChars);
    }

    public String convertHexToString(String hex){

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for( int i=0; i<hex.length()-1; i+=2 ){

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char)decimal);

            temp.append(decimal);
        }
        //System.out.println("Decimal : " + temp.toString());

        return sb.toString();
    }

    byte[] combineArrays(byte[]... arrays){
        int fulllength = 0;
        for(byte[] arr:arrays){
            fulllength+= arr.length;
        }
        byte[] full = new byte[fulllength];
        int currentlength = 0;
        for(byte[] arr:arrays){
            System.arraycopy(arr,0,full,currentlength,arr.length);
            currentlength+=arr.length;
        }
        return full;
    }

    public void setOnDataValidListener(OnDataValidListener onDataValidListener){
        this.onDataValidListener = onDataValidListener;
    }

}
