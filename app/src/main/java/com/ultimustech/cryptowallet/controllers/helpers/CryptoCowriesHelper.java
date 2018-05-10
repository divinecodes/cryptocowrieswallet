package com.ultimustech.cryptowallet.controllers.helpers;

import com.csknust.project.cryptowallet.utils.StringUtils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;

import org.bouncycastle.*;

public class CryptoCowriesHelper {
    private static PrivateKey privateKey;
    private static PublicKey publicKey;

    /**
     * generate a wallet address using the cryptocowries sdk;
     * @return String value of the publicKey
     */
    public static String generateWalletAddress(){
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        generateKeyPair();
        String strToHash = privateToString(privateKey) + publicToString(publicKey);
        return StringUtils.applyShaGetWalletAddr(strToHash);
    }

    private static void generateKeyPair(){
        try{
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

//            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
//
//            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
//            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
//
//            //initialize the key generator and generate the KeyPair
//            keyGen.initialize(ecSpec, random);
//            KeyPair keyPair = keyGen.generateKeyPair();

            //set the public and private keys from the key pair
//            privateKey = keyPair.getPrivate();
//            publicKey = keyPair.getPublic();

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

            keyGen.initialize(256, random);

            KeyPair pair = keyGen.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void signKeys(PrivateKey privKey){
        try{

            Signature dsa = Signature.getInstance("SHA1withECDSA");
            dsa.initSign(privKey);

        }catch(Exception e){
            throw  new RuntimeException(e);
        }
    }

    private static String privateToString(PrivateKey privKey){
        String str = "";

        byte[] key = privKey.getEncoded();

        //byte array to string ;
        StringBuilder builder = new StringBuilder();

        for(byte b: key){
            builder.append(String.format("%02x", b));
        }

        str = builder.toString();
        return str;
    }

    private static String publicToString(PublicKey pubKey){
        String str = "";

        byte[] key = pubKey.getEncoded();
        //byte array to string ;
        StringBuilder builder = new StringBuilder();

        for(byte b: key){
            builder.append(String.format("%02x", b));
        }

        str = builder.toString();
        return str;
    }

}
