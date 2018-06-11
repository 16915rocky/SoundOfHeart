package com.example.xq.soundofheart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.xq.soundofheart.bean.PracticeResultBean;
import com.example.xq.soundofheart.db.ResultReportDao;
import com.example.xq.soundofheart.utils.StatusBarUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rocky on 2018/6/9 0009.
 */

public class TrainResultActivity extends AppCompatActivity {
    @BindView(R.id.lineChart_result)
    LineChart lineChartResult;
    private Float[] datas=new Float[]{12.1f,13.2f,14.3f};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        StatusBarUtil.setTransparent(this);
        initLineChart(lineChartResult);
        try{
            ResultReportDao resultReportDao = new ResultReportDao(this);
            List<PracticeResultBean> practiceResultBeans = resultReportDao.queryPracticeResult();
            List<Entry> datas = getDatas(practiceResultBeans);
            setLineChartDate(datas);
        }catch (Exception e){
            Log.e("rdError",e.getMessage());
        }

    }

    public void initLineChart(LineChart mLineChart){
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setNoDataText("沒有数据");
        mLineChart.setDrawGridBackground(false);
        mLineChart.setDrawBorders(false);
        mLineChart.setBorderColor(Color.WHITE);
        mLineChart.setBorderWidth(3f); //设置 chart 边界线的宽度，单位 dp。
        mLineChart.setTouchEnabled(true);     //能否点击
        mLineChart.setDragEnabled(true);   //能否拖拽
        mLineChart.setScaleEnabled(false);  //能否缩放
        mLineChart.animateX(1000);//绘制动画 从左到右
        mLineChart.setDoubleTapToZoomEnabled(false);//设置是否可以通过双击屏幕放大图表。默认是true
        mLineChart.setHighlightPerDragEnabled(false);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        mLineChart.setDragDecelerationEnabled(false);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲
        MyMarkerView mv = new MyMarkerView(this,
                R.layout.custom_marker_view);
        mv.setChartView(mLineChart); // For bounds control
        mLineChart.setMarker(mv);        //设置 marker ,点击后显示的功能 ，布局可以自定义

        XAxis xAxis = mLineChart.getXAxis();       //获取x轴线
        xAxis.setDrawAxisLine(true);//是否绘制轴线
        xAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        xAxis.setTextColor(Color.WHITE);   //设置字体颜色
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        xAxis.setTextSize(12f);//设置文字大小
        xAxis.setAxisMinimum(0f);//设置x轴的最小值 //`
        xAxis.setSpaceMin(1f);

        xAxis.setGranularity(1f);
       //xAxis.setAxisMaximum(1f);//设置最大值 //
        //xAxis.setLabelCount(1);  //设置X轴的显示个数
        xAxis.setAvoidFirstLastClipping(false);//图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘
        xAxis.setAxisLineColor(Color.WHITE);//设置x轴线颜色
        xAxis.setAxisLineWidth(0.5f);//设置x轴线宽度
        YAxis leftAxis = mLineChart.getAxisLeft();
        YAxis axisRight = mLineChart.getAxisRight();
        leftAxis.enableGridDashedLine(10f, 10f, 0f);  //设置Y轴网格线条的虚线，参1 实线长度，参2 虚线长度 ，参3 周期
        leftAxis.setGranularity(20f); // 网格线条间距
        axisRight.setEnabled(false);   //设置是否使用 Y轴右边的
        leftAxis.setEnabled(true);     //设置是否使用 Y轴左边的
        leftAxis.setGridColor(Color.parseColor("#7189a9"));  //网格线条的颜色
        leftAxis.setDrawLabels(true);        //是否显示Y轴刻度
        leftAxis.setStartAtZero(true);        //设置Y轴数值 从零开始
        leftAxis.setDrawGridLines(true);      //是否使用 Y轴网格线条
        leftAxis.setTextSize(12f);            //设置Y轴刻度字体
        leftAxis.setTextColor(Color.WHITE);   //设置字体颜色
        leftAxis.setAxisLineColor(Color.WHITE); //设置Y轴颜色
        leftAxis.setAxisLineWidth(0.5f);
        leftAxis.setDrawAxisLine(true);//是否绘制轴线
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "%"+value;
            }
        });
    /*    leftAxis.setMinWidth(0f);
        leftAxis.setMaxWidth(200f);*/
        leftAxis.setAxisMinimum(0);
        leftAxis.setAxisMaximum(100);
        leftAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
        Legend l = mLineChart.getLegend();//图例
        l.setEnabled(false);   //是否使用 图例

    }
    public List<Entry> getDatas(List<PracticeResultBean> mList){
        List<Entry> mValues = new ArrayList<>();
        for(int i=0;i<mList.size();i++){
            mValues.add(new Entry(i,mList.get(i).getCorrectRate()));
        }
       /* mValues.add(new Entry(1, 15));
        mValues.add(new Entry(2, 25));
        mValues.add(new Entry(3, 19));
        mValues.add(new Entry(4, 25));
        mValues.add(new Entry(5, 16));
        mValues.add(new Entry(6, 40));
        mValues.add(new Entry(7, 27));
        mValues.add(new Entry(8, 27));
        mValues.add(new Entry(9, 50));
        mValues.add(new Entry(10, 27));
        mValues.add(new Entry(11, 60));
        mValues.add(new Entry(12, 80));
        mValues.add(new Entry(13, 80));
        mValues.add(new Entry(14, 50));
        mValues.add(new Entry(15, 80));
        mValues.add(new Entry(16, 90));
        mValues.add(new Entry(17, 100));*/
        return mValues;
    }

    private void setLineChartDate(List<Entry> mValues) {

        if (mValues.size() == 0) return;
        LineDataSet set1;
        //判断图表中原来是否有数据
        if (lineChartResult.getData() != null &&
                lineChartResult.getData().getDataSetCount() > 0) {
            //获取数据1
            set1 = (LineDataSet) lineChartResult.getData().getDataSetByIndex(0);
            set1.setValues(mValues);
            //刷新数据
            lineChartResult.getData().notifyDataChanged();
            lineChartResult.notifyDataSetChanged();
        } else {
            //设置数据1  参数1：数据源 参数2：图例名称
            set1 = new LineDataSet(mValues, "测试数据1");
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setColor(Color.WHITE);
            set1.setCircleColor(Color.parseColor("#AAFFFFFF"));
            set1.setHighLightColor(Color.WHITE);//设置点击交点后显示交高亮线的颜色
            set1.setHighlightEnabled(true);//是否使用点击高亮线
            set1.setDrawCircles(true);
            set1.setDrawValues(false);
            set1.setValueTextColor(Color.WHITE);
            set1.setLineWidth(1f);//设置线宽
            set1.setCircleRadius(2f);//设置焦点圆心的大小
            set1.setHighlightLineWidth(0.5f);//设置点击交点后显示高亮线宽
            set1.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
            set1.setValueTextSize(12f);//设置显示值的文字大小
            set1.setDrawFilled(true);//设置使用 范围背景填充

            //格式化显示数据
            final DecimalFormat mFormat = new DecimalFormat("###,###,##0");
            set1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return mFormat.format(value);
                }
            });
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.color.translucence);
                set1.setFillDrawable(drawable);//设置范围背景填充
            } else {
                set1.setFillColor(R.color.translucence);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the datasets
            //创建LineData对象 属于LineChart折线图的数据集合
            LineData data = new LineData(dataSets);
            // 添加到图表中
            lineChartResult.setData(data);
            //绘制图表
            lineChartResult.invalidate();
        }
    }
     class MyMarkerView extends MarkerView {

        private TextView tvContent;

        public MyMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);

            // find your layout components
           tvContent = (TextView) findViewById(R.id.tv_marker);
        }

        // callbacks everytime the MarkerView is redrawn, can be used to update the
        // content (user-interface)
        @Override
        public void refreshContent(Entry e, Highlight highlight) {

            tvContent.setText("("+Math.round(e.getX())+" , " +e.getY()+"%)");
            // this will perform necessary layouting
            super.refreshContent(e, highlight);
        }

        private MPPointF mOffset;

        @Override
        public MPPointF getOffset() {

            if(mOffset == null) {
                // center the marker horizontally and vertically
                mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
            }

            return mOffset;
        }
    }

}
