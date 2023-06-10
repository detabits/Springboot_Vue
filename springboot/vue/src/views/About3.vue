<template>
<div>
  <h1>hello, world</h1>
  <el-button class="button1" type="primary" @click="tiaoZhuan()">点击回家</el-button>





  <el-row>
    <el-col :span="12">
      <div id="main" style="width: 500px; height: 400px"></div>
    </el-col>
    <el-col :span="12">
      <div id="pie" style="width: 500px; height: 400px"></div>
    </el-col>
  </el-row>





</div>
</template>

<script>
import * as echarts from 'echarts'


export default {
  name: "Home",
  methods: {
    tiaoZhuan() {
      this.$router.push('/front/home')
      this.$message({
        showClose: true,
        message: 'Welcome',
        type: 'success'
      });
    }
  },

  data() {
    return{

    }
  },
  mounted() { //页面元素渲染之后再触发

    var option = {
      title: {
        text: '各季度会员数量统计',
        subtext: '趋势图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },

      xAxis: {
        type: 'category',
        data: ["第一季度","第二季度","第三季度","第四季度"]
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: "星巴克",
          data: [],
          type: 'bar'
        },
        {
          name: "星巴克",
          data: [],
          type: 'line'
        },
        {
          name: "瑞幸咖啡",
          data: [],
          type: 'bar'
        },
        {
          name: "瑞幸咖啡",
          data: [],
          type: 'line'
        }
      ]
    };


    //饼图
    var pieOption = {
      title: {
        text: '各季度会员统计图',
        subtext: '比例图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          name: "星巴克",
          type: 'pie',
          radius: '70%',
          label:{            //饼图图形上的文本标签
            normal:{
              show:true,
              position:'inner', //标签的位置
              textStyle : {
                fontWeight : 300 ,
                fontSize : 16 ,   //文字的字体大小
                color : "#fff"
              },
              formatter:'{c}({d}%)'
            }
          },
          data: [],    //填空
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };


    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);

    var pieDom = document.getElementById('pie');
    var pieChart = echarts.init(pieDom);

    this.request.get("/echarts/members").then(res =>{
      //填空
      option.xAxis.data = res.data.x
      option.series[0].data =res.data;
      option.series[1].data =res.data;

      option.series[2].data =[5,6,7,8];
      option.series[3].data =[5,6,7,8];
      //数据准备完毕再set

      myChart.setOption(option);

      pieOption.series[0].data = [
        {name:"第一季度",value: res.data[0]},
        {name:"第二季度",value: res.data[1]},
        {name:"第三季度",value: res.data[2]},
        {name:"第四季度",value: res.data[3]},
      ]
      pieChart.setOption(pieOption)
    })






    pieChart.setOption(pieOption)

    option && myChart.setOption(option);

  }
}

</script>

<style scoped>

</style>