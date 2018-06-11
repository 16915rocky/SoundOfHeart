package com.example.xq.soundofheart.utils;

import com.example.xq.soundofheart.R;

/**
 * Created by Rocky on 2018/6/6 0006.
 */

public class ConstantValueUtil {
    public static  int[] audio={R.raw.bao1,R.raw.chi4,/*R.raw.da4,R.raw.dao1,R.raw.dong1,R.raw.duo1,R.raw.fang4,R.raw.fei4,R.raw.gao1,R.raw.gua1*/};
    public static  String[] audioCorrStr={"bao1","chi4","da4","dao1","dong1","duo1","fang4","fei4","gao1","gua1"};
    //音频对应正确的图片
    public static  int[]  audioiCorrImgT={R.mipmap.bao1,R.mipmap.chi4,R.mipmap.da4,R.mipmap.dao1,R.mipmap.dong1,R.mipmap.duo1,R.mipmap.fang4,R.mipmap.fei4,R.mipmap.gao1,R.mipmap.gua1};
    //音频对应错误的图片
    public static  int[]  audioiCorrImgF={R.mipmap.bao4,R.mipmap.chi1,R.mipmap.da1,R.mipmap.dao4,R.mipmap.dong4,R.mipmap.duo4,R.mipmap.fang1,R.mipmap.fei1,R.mipmap.gao4,R.mipmap.gua4};
    //正确imageView对应tag
    public static  String[] imgTCorrTag={"bao1","bao4","chi4","da4","dong1","duo1","fang4","fei4","gao1","gua1"};
    //错误imageView对应tag
    public static  String[] imgFCorrTag={"bao4","bao1","chi1","da1","dong4","duo4","fang1","fei1","gao4","gua4"};

}
