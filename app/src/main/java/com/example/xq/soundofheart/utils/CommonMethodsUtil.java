package com.example.xq.soundofheart.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Rocky on 2018/6/7 0007.
 */

public class CommonMethodsUtil {

    public static Map<String, Object> randomSort(int[] audio,String[] audioCorrStr,int[] audioiCorrImgT,int[] audioiCorrImgF,String[] imgTCorrTag,String[] imgFCorrTag){
        Map<String, Object> maps = new HashMap<>();
        Random random = new Random();
        for(int i=0;i<audio.length;i++){
            int p = random.nextInt(i + 1);
            //音频内容交换
            int tmp=audio[i];
            audio[i]=audio[p];
            audio[p]=tmp;
            //对应映射的字符串交换
            String tmpStr=audioCorrStr[i];
            audioCorrStr[i]=audioCorrStr[p];
            audioCorrStr[p]=tmpStr;

            //对应映射的正确图片位置
            int tmpImgT=audioiCorrImgT[i];
            audioiCorrImgT[i]=audioiCorrImgT[p];
            audioiCorrImgT[p]=tmpImgT;
            //对应映射的错误图片位置
            int tmpImgF=audioiCorrImgF[i];
            audioiCorrImgF[i]=audioiCorrImgF[p];
            audioiCorrImgF[p]=tmpImgF;
            //对应映射的正确图片的Tag
            String tmpTImgTag=imgTCorrTag[i];
            imgTCorrTag[i]=imgTCorrTag[p];
            imgTCorrTag[p]=tmpTImgTag;
            //对应映射的错误图片的Tag
            String tmpFImgTag=imgFCorrTag[i];
            imgFCorrTag[i]=imgFCorrTag[p];
            imgFCorrTag[p]=tmpFImgTag;
        }
        maps.put("audio",audio);
        maps.put("audioCorrStr",audioCorrStr);
        maps.put("audioiCorrImgT",audioiCorrImgT);
        maps.put("audioiCorrImgF",audioiCorrImgF);
        maps.put("imgTCorrTag",imgTCorrTag);
        maps.put("imgFCorrTag",imgFCorrTag);
        return maps;
    }
}
