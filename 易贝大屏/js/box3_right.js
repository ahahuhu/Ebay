var dom = document.getElementById("box3_right");
var myChart11 = echarts.init(dom);
var app = {};

// option11 = null;
var option11 = {
	tooltip: {
		trigger: 'axis',
		axisPointer: {
			type: 'cross',
			label: {
				backgroundColor: '#6a7985'
			}
		}
	},
	legend: {
		textStyle: { //图例文字的样式
			color: '#fff',
			fontSize: 12
		},
		data: ['每日销售预测', '每日求购情况', '每日发布情况']
	},
	toolbox: {
		// feature: {
		// 	saveAsImage: {}
		// }
	},
	grid: {
		left: '3%',
		right: '4%',
		bottom: '3%',
		containLabel: true
	},
	textStyle: { //图例文字的样式
		color: '#fff',
		fontSize: 12
	},
	xAxis: [{
		type: 'category',
		boundaryGap: false,
		// data: ['7.2', '7.3', '7.4', '7.5', '7.6', '7.7', '7.8']
		data:getLastFiveDays()
	}],
	yAxis: [{
		type: 'value'
	}],
	series: [{
			name: '每日销售预测',
			type: 'line',
			stack: '总量',
			areaStyle: {},
			data: [1, 2, 3]
		},
		{
			name: '每日求购情况',
			type: 'line',
			stack: '总量',
			areaStyle: {
				normal: {}
			},
			data: [] // 初始化为空数组，稍后填充数据
		},
		{
			name: '每日发布情况',
			type: 'line',
			stack: '总量',
			label: {
				normal: {
					show: true,
					position: 'top'
				}
			},
			areaStyle: {
				normal: {}
			},
			data: [] // 初始化为空数组，稍后填充数据
		}
	]
};;

var counter = 0; // 初始化计数器


function getLastFiveDays() {
  let xAxisData = [];
  let today = new Date();
  for (let i = 6; i >= 0; i--) {
    let date = new Date();
    date.setDate(today.getDate() - i);
    let month = (date.getMonth() + 1).toString().padStart(2, '0'); // 月份
    let day = date.getDate().toString().padStart(2, '0'); // 日期
    xAxisData.push(`${month}-${day}`);
  }
  return xAxisData;
}



function setupAutoUpdate(interval) {
	setInterval(fetchData, interval);
	myChart11.setOption(option11, true);

}

function fetchData11() {
	
	counter++; // 每次fetchData调用时增加计数器
	Promise.all([
		fetch('http://localhost:8016/BigScreen/SevenSalesForecast').then(response => {
			if (!response.ok) {
				throw new Error('Network response was not ok for sales data');
			}
			return response.json();
		}),
		fetch('http://localhost:8016/BigScreen/SevenXuQiu').then(response => {
			if (!response.ok) {
				throw new Error('Network response was not ok for purchase data');
			}
			return response.json();
		}),
		fetch('http://localhost:8016/BigScreen/SevenXianZhi').then(response => {
			if (!response.ok) {
				throw new Error('Network response was not ok for publish data');
			}
			return response.json();
		})
	]).then(([salesResponse, purchaseResponse, publishResponse]) => {
		// 假设每个响应都有相同的结构
		console.log('Sales Data:', salesResponse.data); // 打印销售数据
		console.log('Purchase Data:', purchaseResponse.data); // 打印求购数据
		console.log('Publish Data:', publishResponse.data); // 打印发布数据
		option11.series[0].data = salesResponse.data;
		option11.series[0].data[6] = salesResponse.data[6] + counter; // 注意索引是从0开始的
		console.log(option11.series[0].data);
		option11.series[1].data = purchaseResponse.data;
		option11.series[1].data[6] = purchaseResponse.data[6]+counter;
		option11.series[2].data = publishResponse.data;
		option11.series[2].data[6] = publishResponse.data[6]+counter;
		// option11.xAxis.data = xAxisData;
		myChart11.setOption(option11, true);
	}).catch(error => {
		console.error('Error fetching data:', error);
	});
}
fetchData11();
// 初始化图表
if (option11 && typeof option11 == "object") {
	console.log(option11.series[0].data);
	// myChart11.setOption(option11, true);
}
// 调用函数获取数据并更新图表

//setupAutoUpdate(2000);