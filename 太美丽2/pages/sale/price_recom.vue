<template>
	<view class="optimized-container">
		<view class="input-group">
			<text class="label-text">商品分类</text>
			<picker mode="selector" :range="categories" @change="onCategoryChange">
				<view class="picker">
					{{ goods.category !== null ? categories[goods.category] : '请选择查询的商品分类' }}
				</view>
			</picker>
		</view>
		<view class="input-group">
			<text class="label-text">商品崭新程度</text>
			<input class="input-field" placeholder="请输入崭新程度/如:99%即输入99"
				placeholder-style="color: #999; font-size: 16px;" v-model="goods.new" type="text" />
		</view>
		<button class="submit-button" @click="handle">查询</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				categories: ['书籍', '电子数码', '运动户外', '生活用品', '食品', '其他'],
				goods: {
					category: null,
					new: '',
				},
				sub: ''
			}
		},
		methods: {
			onCategoryChange(e) { // 下拉切换
				this.goods.category = e.detail.value;
				console.log(this.goods.category);
			},
			handle() {
				const x = this.goods.new
				if (x < 0 || x > 100) {
					this.sub = "书籍的新旧程度必须在0到100之间";
					uni.showToast({
						title: this.sub,
						icon: 'none',    //如果要纯文本，不要icon，将值设为'none'
						duration: 4000    //持续时间为 2秒
					})  
				} else {
					// 指数衰减模型，假设x为百分比，100%表示新书
					var decayRate = 0.06; // 衰减率，可以根据实际情况调整
					if (this.goods.category == 1) {
						decayRate = 0.08;
					}
					else if (this.goods.category == 0) {
						decayRate = 0.03;
					}
					else if (this.goods.category == 2) {
						decayRate = 0.05;
					}
					else if (this.goods.category == 3) {
						decayRate = 0.04;
					}
					else if (this.goods.category == 4) {
						decayRate = 0.1;
					}
					decayRate *= 0.6;
					this.sub = Math.round(100 - 100 * Math.exp(-decayRate * (100 - x)));
					uni.showToast({
						title: '推荐折价率：' + this.sub + '%',
						icon: 'none',    //如果要纯文本，不要icon，将值设为'none'
						duration: 4000    //持续时间为 2秒
					})  
				}
			},
		}
	}
</script>

<style>
	.optimized-container {
		background-image: linear-gradient(120deg, #ff9e9e 0%, #ffcc80 100%);
		padding: 20px;
		background-color: #f9f9f9;
		min-height: 100vh;
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.input-group {
		width: 100%;
		background-color: #fff;
		padding: 20px;
		border-radius: 10px;
		margin-bottom: 20px;
		box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
	}

	.label-text {
		font-size: 18px;
		margin-bottom: 10px;
		color: #333;
	}

	.input-field,
	.picker {
		width: 95%;
		padding: 15px;
		border: 1px solid #ddd;
		border-radius: 5px;
		font-size: 16px;
	}

	.picker {
		background-color: #fff;
		color: #d9480f;
	}

		.submit-button {
			width: 100%;
			padding: 15px;
			background-image: linear-gradient(120deg, #ff5722 0%, #ff9800 100%);
			color: #fff;
			text-align: center;
			border-radius: 10px;
			font-size: 18px;
			font-weight: bold;
			margin-top: 20px;
			cursor: pointer;
			box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
		}
	</style>
