// 计算从今天之前十天每十二小时的时间点
var dates = [];
var now = new Date();
var dayMilliseconds = 12 * 60 * 60 * 1000; // 12小时的毫秒数

for (var i = 0; i < 20; i++) {
	var date = new Date(now - dayMilliseconds * i);
	dates.push(date.toLocaleString()); // 转换为本地时间字符串
}

// 初始化图表
var chartDom = document.getElementById('box5');
var myChart5555 = echarts.init(chartDom);

function fetchData() {
	Promise.all([
			fetch('http://localhost:8016/BigScreen/recent12HourUploadProduct'),
			fetch('http://localhost:8016/BigScreen/recent12HourUploadSeeker')
		])
		.then(responses => {
			return Promise.all(responses.map(response => {
				if (!response.ok) {
					throw new Error(`HTTP error! status: ${response.status}`);
				}
				return response.json();
			}));
		})
		.then(datas => {
			// 假设 datas[0] 是求购数据，datas[1] 是产品数据
			var seekData = datas[0].data; // 确保数据顺序与日期对应
			var productData = datas[1].data; // 确保数据顺序与日期对应

			// 找到两个数据集中的最大值
			var maxDataValue = Math.max(...seekData, ...productData);

			// 调整y轴的最大值
			option5555.yAxis.max = maxDataValue;

			option5555.series[0].data = productData; // 商品数
			option5555.series[1].data = seekData; // 需求数

			console.log(datas);
			// 确保myChart5555已经初始化
			if (myChart5555) {
				myChart5555.setOption(option5555);
			} else {
				console.error('ECharts 实例尚未初始化');
			}
		})
		.catch(error => {
			console.error('请求失败:', error);
		});
}

var option5555 = {
	legend: {
		data: ['商品数', '需求数'],
		textStyle: {
			color: 'white' // 设置图例文字颜色为白色
		}
	},
	xAxis: {
		type: 'category',
		data: dates.reverse(),
		axisLine: {
			lineStyle: {
				color: 'white'
			}
		},
		axisLabel: {
			color: 'white',
			formatter: function(value, index) {
				return new Date(value).toLocaleDateString('zh-cn', {
					month: '2-digit',
					day: '2-digit'
				});
			},
			interval: 2,
			rotate: 45
		},
		splitLine: {
			lineStyle: {
				color: 'white'
			}
		}
	},
	yAxis: {
		type: 'value',
		min: 0,
		max: 100, // 初始化y轴最大值为一个较大的数
		axisLine: {
			lineStyle: {
				color: 'white'
			}
		},
		axisLabel: {
			color: 'white'
		},
		splitLine: {
			lineStyle: {
				color: 'white'
			}
		}
	},
	series: [{
			name: '商品数', // 设置系列名称
			data: [],
			type: 'line',
			lineStyle: {
				color: 'red'
			}
		},
		{
			name: '需求数', // 设置系列名称
			data: [], // 新增曲线的数据
			type: 'line',
			lineStyle: {
				color: 'blue'
			}
		}
	]
};

// 获取初始数据
fetchData();

// 设置定时器，定时刷新数据
setInterval(fetchData, 5000);