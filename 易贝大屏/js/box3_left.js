var dom13 = document.getElementById("box3_left");
var myChart13 = echarts.init(dom13);
var app13 = {};
option13 = null;
app13.title = '极坐标系下的堆叠柱状图';

option13 = {
	textStyle:{//图例文字的样式
                color:'#dbdbdb',
                fontSize:10
           },
    angleAxis: {
    },
    radiusAxis: {
        type: 'category',
        data: ['周一', '周二', '周三', '周四'],
        z: 10
    },
    polar: {
    },
    series: [{
        type: 'bar',
        data: [1, 2, 3, 4],
        coordinateSystem: 'polar',
        name: 'A',
        stack: 'a'
    }, {
        type: 'bar',
        data: [2, 4, 6, 8],
        coordinateSystem: 'polar',
        name: 'B',
        stack: 'a'
    }, {
        type: 'bar',
        data: [1, 2, 3, 4],
        coordinateSystem: 'polar',
        name: 'C',
        stack: 'a'
    }],
    legend: {
        show: true,
        textStyle:{//图例文字的样式
                color:'#dbdbdb',
                fontSize:10
           },
        data: ['A', 'B', 'C']
    }
};
;
if (option13 && typeof option13 === "object") {
    myChart13.setOption(option13, true);
}