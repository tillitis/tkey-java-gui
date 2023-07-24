package main;/*
 * Copyright (C) 2022, 2023 - Tillitis AB
 * SPDX-License-Identifier: GPL-2.0-only
 */

import com.tillitis.FwCmd;
import com.tillitis.SerialConnHandler;
import com.tillitis.TkeyClient;

import java.util.Arrays;

import static com.tillitis.CmdLen.*;
import static com.tillitis.TkeyClient.*;

public class Signer {
    private static final FwCmd cmdGetPubkey       = new FwCmd(0x01, "cmdGetPubkey", CmdLen1, (byte) 3);
    private static final FwCmd rspGetPubkey       = new FwCmd(0x02, "rspGetPubkey", CmdLen128, (byte) 3);
    private static final FwCmd cmdSetSize         = new FwCmd(0x03, "cmdSetSize", CmdLen32, (byte) 3);
    private static final FwCmd rspSetSize         = new FwCmd(0x04, "rspSetSize", CmdLen4, (byte) 3);
    private static final FwCmd cmdSignData        = new FwCmd(0x05, "cmdSignData", CmdLen128, (byte) 3);
    private static final FwCmd rspSignData        = new FwCmd(0x06, "rspSignData", CmdLen4, (byte) 3);
    private static final FwCmd cmdGetSig          = new FwCmd(0x07, "cmdGetSig", CmdLen1, (byte) 3);
    private static final FwCmd rspGetSig          = new FwCmd(0x08, "rspGetSig", CmdLen128, (byte) 3);
    private static final FwCmd cmdGetNameVersion  = new FwCmd(0x09, "cmdGetNameVersion", CmdLen1, (byte) 3);
    private static final FwCmd rspGetNameVersion  = new FwCmd(0x0a, "rspGetNameVersion", CmdLen32, (byte) 3);
    private static final FwCmd cmdSignPhData = new FwCmd(0x0b, "cmdSignPhData", CmdLen128, (byte) 3);
    private static final FwCmd rspSignPhData = new FwCmd(0x0c, "rspSignPhnData", CmdLen4, (byte) 3);
    private static TkeyClient tk1 = new TkeyClient();
    private static final byte statusOK = 0x00;

    static SerialConnHandler handler = null;

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * GetAppNameVersion gets the name and version of the running app in the same style as the stick itself.
     */
    public static String getAppNameVersion() throws Exception {
        clearIOFull();
        Thread.sleep(200);
        byte[] data = getData(cmdGetNameVersion,rspGetNameVersion);
        return unpackName(data);
    }

    /**
     * GetPubkey fetches the public key of the signer.
     */
    public static byte[] getPubKey() throws Exception {
        clearIOFull();
        Thread.sleep(200);
        byte[] data = getData(cmdGetPubkey,rspGetPubkey);
        return Arrays.copyOfRange(data,1,32);
    }

    /**
     * getSig gets the ed25519 signature from the signer app, if available.
     */
    public static byte[] getSig() throws Exception {
        byte[] data = getData(cmdGetSig,rspGetSig);
        if(data[1] != statusOK){
            System.out.println("Status not ok");
        }
        return Arrays.copyOfRange(data,2,66);
    }

    /**
     * Sign signs the message in data and returns an ed25519 signature.
     */
    public static byte[] sign(byte[] in) throws Exception {
        Thread.sleep(200);
        clearIOFull();

        setSize(in.length);
        int offset = 0;
        for(int nsent = 0; offset < in.length; offset+= nsent){
            nsent = signLoad(Arrays.copyOfRange(in, offset, in.length));
        }
        if(offset > in.length) throw new Exception("Transmitted more than expected");

        return getSig();
    }

    /**
     * SetSize sets the size of the data to sign.
     */
    public static void setSize(int size) throws Exception {
        byte[] tx = tk1.newFrameBuf(cmdSetSize,2);
        tx[2] = (byte) size;
        tx[3] = (byte) (size >> 8);
        tx[4] = (byte) (size >> 16);
        tx[5] = (byte) (size >> 24);
        tk1.write(tx);

        Thread.sleep(500);

        byte[] rx = tk1.readFrame(rspSetSize,2);
        if(rx[1] != statusOK){
            System.out.println("Status not ok");
        }
    }

    /**
     * signload loads a chunk of a message to sign and waits for a response from the signer.
     */
    private static int signLoad(byte[] content) throws Exception {
        byte[] tx = tk1.newFrameBuf(cmdSignData,2);

        byte[] payload = new byte[cmdSignData.getCmdLen().getBytelen()-1];
        int copied = Math.min(payload.length, content.length);
        System.arraycopy(content, 0, payload, 0, copied);

        if (copied < payload.length) {
            byte[] padding = new byte[payload.length - copied];
            System.arraycopy(padding, 0, payload, copied, padding.length-1);
        }
        System.arraycopy(payload, 0, tx, 2, payload.length);

        tk1.write(tx);

        byte[] rx;
        try {
            Thread.sleep(10);
            rx = tk1.readFrame(rspSignData, 2);
        }catch (Exception e){
            throw new Exception(e);
        }
        if(rx[2] != statusOK){
            System.out.println("Status not ok");
        }
        return copied;
    }

    /**
     * SignPh signs a SHA512 pre-hashed message in data and returns an ed25519ph signature.
     */
    public static byte[] signPh(byte[] data) throws Exception {
        Thread.sleep(200);
        clearIOFull();

        byte[] tx = tk1.newFrameBuf(cmdSignPhData,3);
        System.arraycopy(data,0,tx,2,data.length);

        tk1.write(tx);
        byte[] rx = tk1.readFrame(rspSignPhData,3);

        if(rx[2] != statusOK){
            System.out.println("Status not ok");
        }
        return getSig();
    }
}
