<template>
	<view class="container">
		<view class="input-group">
			<text class="label-text">求购商品名称</text>
			<input class="input-field" placeholder="请输入需要的商品名称" placeholder-style="color: #999; font-size: 16px;"
				v-model="goods.name" type="text" />
		</view>

		<view class="input-group">
			<text class="label-text">求购商品分类</text>
			<picker mode="selector" :range="categories" @change="onCategoryChange">
				<view class="picker">
					{{ goods.category !== null ? categories[goods.category] : '请选择求购商品分类' }}
				</view>
			</picker>
		</view>

		<view class="input-group">
			<text class="label-text">求购商品描述</text>
			<textarea class="textarea-field" placeholder="描述一下需要商品的品牌、崭新程度..." v-model="goods.description"
				@input="handleInput" />
		</view>

		<view class="input-group">
			<text class="label-text">期望价格（单位：元）</text>
			<input class="input-field" placeholder="¥0.00" v-model="goods.price" type="number" />
		</view>

		<view class="input-group shipping-group">
			<text class="label-text">求购人所在位置</text><input class="location-label" placeholder="请输入您所在校区和楼栋"
				placeholder-style="color: #999; font-size: 16px;" v-model="goods.address" />
		</view>

		<button class="submit-button" @click="handleSubmit">发布</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				categories: ['书籍', '电子数码', '运动户外', '生活用品', '食品', '其他'],
				goods: {
					userId: 12,
					name: "",
					category: null,
					description: '',
					price: '',
					address: '',
				},
			}
		},
		onLoad() {
			const value = uni.getStorageSync("userinfo")
			this.goods.userId = value.id
		},
		methods: {
			onCategoryChange(e) { // 下拉切换
				this.goods.category = e.detail.value;
				console.log(this.goods.category);
			},
			handleInput(event) { // 输入长文字
				this.goods.description = event.target.value;
			},
			handleSubmit() {
				uni.request({
					url: "http://localhost:8016/addSeeker",
					data: this.goods,
					method: "POST",
					success: (res) => {
						console.log(res)
						if (res.statusCode == 200) {
							uni.showToast({
								title: '发布成功',
								icon: 'success',
								duration: 1000 // 这里设置duration为1000ms，但实际上toast的显示时间不会影响到setTimeout的延迟  
							});
							// 使用setTimeout延迟1秒后执行返回操作  
							setTimeout(() => {
								// 返回上一个页面，uni-app中通常使用uni.navigateBack()  
								uni.navigateBack();
							}, 1000); // 延迟时间为1000毫秒，即1秒  

						}
					}
				})

			},
		}
	}
</script>

<style>
	.container {
		background-image: linear-gradient(120deg, #f6d365 0%, #fda085 100%);
		padding: 20px;
		background-color: #f9f9f9;
		min-height: 100vh;
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.header {
		width: 100%;
		padding: 20px 0;
		text-align: center;
		background-color: #1065a3;
		color: white;
		font-size: 24px;
		font-weight: bold;
		border-radius: 8px;
		margin-bottom: 20px;
	}

	.input-group {
		width: 100%;
		background-color: #fff;
		padding: 20px;
		border-radius: 10px;
		margin-bottom: 20px;
		box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	}

	.label-text {
		font-size: 18px;
		margin-bottom: 10px;
		color: #333;
	}

	.input-field,
	.textarea-field,
	.picker,
	.location-label {
		width: 95%;
		padding: 15px;
		border: 1px solid #ddd;
		border-radius: 5px;
		font-size: 16px;
	}

	.textarea-field {
		min-height: 100px;
		resize: none;
	}

	.picker {
		background-color: #fff;
		color: #1065a3;
	}

	.radio-container {
		display: flex;
		flex-direction: column;
		gap: 10px;
	}

	.radio-item {
		margin-top: 10px;
		font-size: 16px;
		display: flex;
		align-items: center;
		gap: 5px;
	}

	.submit-button {
		width: 100%;
		padding: 15px;
		background-color: #555500;
		color: #fff;
		text-align: center;
		border-radius: 10px;
		font-size: 18px;
		font-weight: bold;
		margin-top: 20px;
		cursor: pointer;
	}
</style>