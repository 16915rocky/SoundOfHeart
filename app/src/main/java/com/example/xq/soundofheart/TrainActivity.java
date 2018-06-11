package com.example.xq.soundofheart;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xq.soundofheart.bean.PracticeResultBean;
import com.example.xq.soundofheart.db.ResultReportDao;
import com.example.xq.soundofheart.utils.CommonMethodsUtil;
import com.example.xq.soundofheart.utils.ConstantValueUtil;
import com.example.xq.soundofheart.widget.TickView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Rocky on 2018/6/6 0006.
 */

public class TrainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.lt_guide)
    LinearLayout ltGuide;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.img_sel1)
    ImageView imgSel1;
    @BindView(R.id.tick_view_accent1)
    TickView tickViewAccent1;
    @BindView(R.id.img_sel2)
    ImageView imgSel2;
    @BindView(R.id.tick_view_accent2)
    TickView tickViewAccent2;
    @BindView(R.id.current_question_num)
    TextView currentQuestionNum;
    @BindView(R.id.ct_time)
    Chronometer ctTime;
    @BindView(R.id.lt_replay)
    LinearLayout ltReplay;
    @BindView(R.id.lt_stop)
    LinearLayout ltStop;
    @BindView(R.id.lt_sign_out)
    LinearLayout ltSignOut;
    private HandlerThread trainHt;
    private Handler trainHandler;
    private boolean isContinuePlay = true;
    private MediaPlayer mMediaPlayer;
    private static final int MSG_PLAYER = 0x00;
    private int currentPlayingAudio = 0;
    private AudioManager audio;
    private String currentAudioCorrStr;
    private int[] moduleAudio;
    private String[] moduleAudioCorrStr, imgTCorrTag, imgFCorrTag;
    private int[] audioiCorrImgT, audioiCorrImgF;
    private Handler changeImgHandler = new Handler();
    private Boolean isAlreadySelect = false;
    private int correctNum=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
        ButterKnife.bind(this);
        initData();
        initView();
        initEvent();
        initBackThread();
        trainHandler.sendEmptyMessageDelayed(MSG_PLAYER, 2000);
        //为了能够手动调节音量
        audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
    }


    private void initData() {
        Map<String, Object> maps = CommonMethodsUtil.randomSort(ConstantValueUtil.audio, ConstantValueUtil.audioCorrStr, ConstantValueUtil.audioiCorrImgT, ConstantValueUtil.audioiCorrImgF, ConstantValueUtil.imgTCorrTag, ConstantValueUtil.imgFCorrTag);
        moduleAudio = (int[]) maps.get("audio");
        moduleAudioCorrStr = (String[]) maps.get("audioCorrStr");
        audioiCorrImgT = (int[]) maps.get("audioiCorrImgT");
        audioiCorrImgF = (int[]) maps.get("audioiCorrImgF");
        imgTCorrTag = (String[]) maps.get("imgTCorrTag");
        imgFCorrTag = (String[]) maps.get("imgFCorrTag");
    }

    private void initView() {
        imgSel1.setImageResource(audioiCorrImgT[0]);
        imgSel1.setTag(imgTCorrTag[0]);
        imgSel2.setImageResource(audioiCorrImgF[0]);
        imgSel2.setTag(imgFCorrTag[0]);
        ctTime.setBase(SystemClock.elapsedRealtime());//计时器清零
        ctTime.start();
    }

    public void setImgProperty(int num) {
        imgSel1.setImageResource(audioiCorrImgT[num]);
        imgSel1.setTag(imgTCorrTag[num]);
        imgSel2.setImageResource(audioiCorrImgF[num]);
        imgSel2.setTag(imgFCorrTag[num]);
    }

    private void initEvent() {
        imgSel1.setOnClickListener(this);
        imgSel2.setOnClickListener(this);
        imgSel1.setClickable(false);
        imgSel2.setClickable(false);
        ltReplay.setOnClickListener(this);
        ltStop.setOnClickListener(this);
        ltSignOut.setOnClickListener(this);

    }


    private void initBackThread() {
        trainHt = new HandlerThread("train");
        trainHt.start();
        trainHandler = new Handler(trainHt.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_PLAYER:
                        if (currentPlayingAudio < ConstantValueUtil.audio.length) {
                            changeImgHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tickViewAccent1.setVisibility(View.GONE);
                                    tickViewAccent2.setVisibility(View.GONE);
                                    tickViewAccent1.setChecked(false);
                                    tickViewAccent2.setChecked(false);
                                    imgSel1.setClickable(false);
                                    imgSel2.setClickable(false);
                                    setImgProperty(currentPlayingAudio);
                                    currentQuestionNum.setText("(" + (currentPlayingAudio + 1) + ")");
                                }
                            });
                            isAlreadySelect = false;
                            createPlayer(moduleAudio[currentPlayingAudio]);
                            currentAudioCorrStr = moduleAudioCorrStr[currentPlayingAudio];

                            imgSel1.setClickable(true);
                            imgSel2.setClickable(true);

                        }else{
                          try{
                              ResultReportDao resultReportDao = new ResultReportDao(TrainActivity.this);
                              float v = correctNum / (float) 25;
                              float value =new BigDecimal(v).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
                              resultReportDao.addPracticeResult(value);
                              ResultReportDao resultReportDao2 = new ResultReportDao(TrainActivity.this);
                              List<PracticeResultBean> practiceResultBeans = resultReportDao2.queryPracticeResult();
                             /* Intent intent = new Intent();
                              intent.setClass(TrainActivity.this,TrainResultActivity.class);
                              startActivity(intent);*/
                          }catch (Exception e){
                              Log.e("error",e.getMessage()+"");
                          }
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void createPlayer(int playContent) {
        mMediaPlayer = MediaPlayer.create(TrainActivity.this, playContent);
        mMediaPlayer.setVolume(1, 1);
        mMediaPlayer.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                audio.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE,
                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                audio.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER,
                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_sel1:
                if (!isAlreadySelect) {
                    isAlreadySelect = true;
                    currentPlayingAudio++;
                    recordTrF((String) imgSel1.getTag(), true);

                }
                break;
            case R.id.img_sel2:
                if (!isAlreadySelect) {
                    isAlreadySelect = true;
                    currentPlayingAudio++;
                    recordTrF((String) imgSel2.getTag(), false);
                }
                break;
            case R.id.lt_replay:{
                createPlayer(moduleAudio[currentPlayingAudio-1]);
                }
                break;
            case R.id.lt_stop:{
                if(!isContinuePlay){
                    ctTime.stop();
                    isContinuePlay=true;
                }else{
                    ctTime.start();
                }
            }

                break;
            case R.id.lt_sign_out:{
                trainHandler.removeCallbacksAndMessages(null);
                finish();
            }
            break;
            default:
                break;
        }
    }

    //计算对错方法
    public void recordTrF(String selectImgTag, boolean leftImg) {
        String currentImgTCorrTag = imgTCorrTag[currentPlayingAudio - 1];
        if (selectImgTag.equals(currentImgTCorrTag)) {
            //答对题数
            correctNum++;
            if (leftImg) {
                tickViewAccent1.setVisibility(View.VISIBLE);
                tickViewAccent1.setChecked(true);
            } else {
                tickViewAccent2.setVisibility(View.VISIBLE);
                tickViewAccent2.setChecked(true);
            }
            trainHandler.sendEmptyMessageDelayed(MSG_PLAYER, 5000);
        } else {
            createPlayer(moduleAudio[currentPlayingAudio - 1]);
            if (leftImg) {
                tickViewAccent2.setVisibility(View.VISIBLE);
                tickViewAccent2.setChecked(true);
            } else {
                tickViewAccent1.setVisibility(View.VISIBLE);
                tickViewAccent1.setChecked(true);
            }
            trainHandler.sendEmptyMessageDelayed(MSG_PLAYER, 5000);
        }
    }

}

