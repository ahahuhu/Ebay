var dom = document.getElementById("box2");
var myChart1 = echarts.init(dom);
var app = {};
var posList = [
    'left', 'right', 'top', 'bottom',
    'inside',
    'insideTop', 'insideLeft', 'insideRight', 'insideBottom',
    'insideTopLeft', 'insideTopRight', 'insideBottomLeft', 'insideBottomRight'
];

// 定义了一组用于配置文本标签属性的参数对象 app.configParameters

app.configParameters = {
    rotate: { // rotate 参数控制文本标签的旋转角度
        min: -90, // 最小旋转角度为 -90 度
        max: 90 // 最大旋转角度为 90 度
    },
    align: { // align 参数控制文本标签的水平对齐方式
        options: { // 可选值及其对应的描述
            left: 'left', // 左对齐
            center: 'center', // 居中对齐
            right: 'right' // 右对齐
        }
    },
    verticalAlign: { // verticalAlign 参数控制文本标签的垂直对齐方式
        options: { // 可选值及其对应的描述
            top: 'top', // 顶部对齐
            middle: 'middle', // 居中对齐
            bottom: 'bottom' // 底部对齐
        }
    },
    position: { // position 参数控制文本标签的位置
        options: echarts.util.reduce(posList, function(map, pos) {
            map[pos] = pos; // 将 posList 中的每个位置作为选项，并使用相同的描述
            return map;
        }, {})
    },
    distance: { // distance 参数控制文本标签与图表元素的距离
        min: 0, // 最小距离为 0 像素
        max: 100 // 最大距离为 100 像素
    }
};


app.config = {
    rotate: 90, // 旋转角度为90度
    align: 'left', // 文本水平对齐方式为左对齐
    verticalAlign: 'middle', // 文本垂直对齐方式为居中对齐
    position: 'insideBottom', // 文本位置为柱状图内部底部
    distance: 15, // 文本与图表元素的距离为15像素
    onChange: function() { // onChange属性为一个函数，处理配置变化时的逻辑
        var labelOption = {
            normal: {
                rotate: app.config.rotate, // 使用当前配置中的旋转角度
                align: app.config.align, // 使用当前配置中的水平对齐方式
                verticalAlign: app.config.verticalAlign, // 使用当前配置中的垂直对齐方式
                position: app.config.position, // 使用当前配置中的文本位置
                distance: app.config.distance // 使用当前配置中的距离
            }
        };
        // 可以在这里处理配置变化后的其他逻辑，例如更新图表
    }
};


var labelOption = {
    normal: {
        show: true,
        position: app.config.position,
        distance: app.config.distance,
        align: app.config.align,
        verticalAlign: app.config.verticalAlign,
        rotate: app.config.rotate,
        // formatter: '{c}  {name|{a}}',
		formatter: '{c}',
        fontSize: 15,
        rich: {
            name: {
                textBorderColor: '#383838'
            }
        }
    }
};

// 获取最近五天的日期，并格式化为 MM-DD
// function getLastFiveDays() {
//     let dates = [];
//     let today = new Date();
//     for (let i = 4; i >= 0; i--) {
//         let date = new Date();
//         date.setDate(today.getDate() - i);
//         let month = (date.getMonth() + 1).toString().padStart(2, '0'); // 月份
//         let day = date.getDate().toString().padStart(2, '0'); // 日期
//         dates.push(`${month}-${day}`);
//     }
//     return dates;
// }

var option2 = {
    color: ['#ffaa00', '#006699', '#4cabce', '#e5323e'],
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
    },

    legend: {
        textStyle: { // 图例文字的样式
            color: '#dbdbdb',
            fontSize: 15
        },
        data: ['闲置的商品数', '求购的商品数']
    },
    textStyle: { // 图例文字的样式
        color: '#dbdbdb',
        fontSize: 15
    },
    calculable: true,
    xAxis: [{
        type: 'category',
        axisTick: {
            show: false
        },
         data: ['总计', '书籍', '电子数码', '运动户外', '生活用品', '食品', '其他'],
		 axisLabel: {
		       // 设置 X 轴文字的颜色和大小
		       // color: 'white', // 文字颜色
		       // fontSize: 12, // 文字大小
		       interval: 0 // 确保所有标签都显示
		     }
    }],
    yAxis: [{
        type: 'value'
    }],
    series: [{
            name: '闲置的商品数',
            type: 'bar',
            barGap: 0,
            label: labelOption,
            data: [500, 332, 301, 334, 390,200,100]
        },
        {
            name: '求购的商品数',
            type: 'bar',
            label: labelOption,
            data: [220, 182, 191, 234, 290,200,100]
        }
    ]
};
// 使用 fetch 进行网络请求
function fetchSeekingData() {
	fetch('http://localhost:8016/BigScreen/todaySeeker')
		.then(response => response.json())
		.then(data => {
			// 更新图表数据
			console.log(data.result);	
			console.log(option2.series[0].data);
			option2.series[1].data = data.result;
			if (option2 && typeof option2 === "object") {
					myChart1.setOption(option2, true);
					console.log(option2.series[0]);
				} 
		})
		.catch(error => {
			console.error('请求失败', error);
		});
}

// 使用 fetch 进行网络请求
function fetchPostingData() {
	fetch('http://localhost:8016/BigScreen/todayProduct')
		.then(response => response.json())
		.then(data => {
			// 更新图表数据
			console.log(data.result);	
			console.log(option2.series[1]);
			option2.series[0].data = data.result;
			if (option2 && typeof option2 === "object") {
					myChart1.setOption(option2, true);
					console.log(option2.series[0]);
				} 
			
		})
		.catch(error => {
			console.error('请求失败', error);
		});
}

fetchSeekingData();
fetchPostingData();

	
// 周期定时器,用于实时更新大屏数据
timer = setInterval(() => {
	Promise.all([fetchSeekingData(), fetchPostingData()]).then(() => {
		// 使用新的option设置图表
	if (option2 && typeof option2 === "object") {
		myChart1.setOption(option2, true);
		console.log(option2.series[0]);
	} else {
		console.log("赋值失败")
	}
	});
}, 3000);